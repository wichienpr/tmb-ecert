package th.co.baiwa.buckwaframework.security.provider;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import th.co.baiwa.buckwaframework.security.constant.ADConstant;
import th.co.baiwa.buckwaframework.security.domain.TMBPerson;


@Service("tmbldapManager")
public class TMBLDAPManager {

	@Value("${ldap.url}")
	private String url;
	
	@Value("${ldap.base}")
	private String base;

	@Value("${ldap.domain}")
	private String domain;
	
	public TMBPerson isAuthenticate(String username, String password) throws Exception {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(url);
		contextSource.setBase(base);
		contextSource.setUserDn(username  +"@" + domain);
		contextSource.setPassword(password);
		contextSource.afterPropertiesSet();
		LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
		ldapTemplate.afterPropertiesSet();

		LdapQuery query = query().where("objectclass").is("organizationalPerson").and("sAMAccountName").is(username);
		
	         
		try {

			List<TMBPerson> res = ldapTemplate.search(query, new AttributesMapper<TMBPerson>() {
				public TMBPerson mapFromAttributes(Attributes attrs) throws NamingException {

					TMBPerson tmbPerson = new TMBPerson();
					tmbPerson.setUseranme(username);
					tmbPerson.setPassword(password);
					tmbPerson.setTmbcn(attrs.get("cn").get().toString());
					tmbPerson.setUserid(username);
					tmbPerson.setName(attrs.get("displayName").get().toString());
					List<String> memberOfs = new ArrayList<>();
					Attribute memberOf = attrs.get("memberOf");
					if(memberOf != null) {
						for (int i = 0; i < memberOf.size(); i++) {
							Object str = memberOf.get(i);
							//check NSLL Project
							if(str.toString().indexOf(ADConstant.AD_PROJECT) != -1){
								memberOfs.add(str.toString().split("CN=")[1].split(",")[0]);							
							}
						}
					}else {
						//Not have role in AD
					}
					
					//branchCode
					Attribute branchCode = attrs.get("extensionAttribute11");
					if(branchCode != null) {
						String[] branch = StringUtils.split( branchCode.get().toString(), "|");
						tmbPerson.setBranchCode(StringUtils.trim(branch[1]));
					}
									
					tmbPerson.setMemberOfs(memberOfs);
					return tmbPerson;
				}
			});

			if (res.isEmpty()) {
				throw new Exception("Invalid Username and Password[not found in dir ldap]");
			}

			return res.get(0);
			
		} catch (AuthenticationException e) {
			String strError = e.getMessage();
			String[] cods = strError.split(";");
			throw new Exception(cods[0]);
		}
		
//		TMBPerson tmbPerson = new TMBPerson();
//		tmbPerson.setUseranme(username);
//		tmbPerson.setPassword(password);
//		tmbPerson.setTmbcn("cn");
//		tmbPerson.setUserid("1234");
//		return tmbPerson;
	}
	
}
