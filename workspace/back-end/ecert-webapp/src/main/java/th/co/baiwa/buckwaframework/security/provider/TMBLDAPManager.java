package th.co.baiwa.buckwaframework.security.provider;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.List;

import javax.management.relation.RoleInfoNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.sql.rowset.spi.TransactionalWriter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
		ldapTemplate.setIgnorePartialResultException(true);

		LdapQuery query = query().where("objectclass").is("organizationalPerson").and("sAMAccountName").is(username);
		
	         
		try {

			List<TMBPerson> res = ldapTemplate.search(query, new AttributesMapper<TMBPerson>() {
				public TMBPerson mapFromAttributes(Attributes attrs) throws NamingException {

					TMBPerson tmbPerson = new TMBPerson();
					tmbPerson.setUseranme(username);
					tmbPerson.setPassword(password);
					tmbPerson.setTmbcn(attrs.get("cn").get().toString());
					tmbPerson.setUserid(username);
//					tmbPerson.setName(attrs.get("displayName").get().toString());
//					name th
					tmbPerson.setName(attrs.get("msDS-PhoneticDisplayName").get().toString());
					List<String> memberOfs = new ArrayList<>();
					Attribute memberOf = attrs.get("memberOf");
					if(memberOf != null) {
						for (int i = 0; i < memberOf.size(); i++) {
							Object str = memberOf.get(i);
							//check NSLL Project
							if(str.toString().indexOf(ADConstant.AD_PROJECT) != -1){
								memberOfs.add(str.toString().split("CN=")[1].split(",")[0]);							
							}
//							memberOfs.add(str.toString().split("CN=")[1].split(",")[0]);
							
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
					
					Attribute group = attrs.get("extensionAttribute4");
					if(group != null) {
						String[] grouparr = StringUtils.split( group.get().toString(), "|");
						tmbPerson.setGroup(StringUtils.trim(grouparr[0]));
					}
					
					Attribute position = attrs.get("extensionAttribute1");
					if(position != null) {
						String[] positionarr = StringUtils.split( position.get().toString(), "|");
						tmbPerson.setPosition(StringUtils.trim(positionarr[0]));
					}
					
					Attribute officeCode = attrs.get("extensionAttribute2");
					if(officeCode != null) {
						String[] officeCodearr = StringUtils.split( officeCode.get().toString(), "|");
						tmbPerson.setOfficeCode(StringUtils.trim(officeCodearr[1]));
					}
					
					Attribute department = attrs.get("extensionAttribute3");
					if(department != null) {
						String[] departmentarr = StringUtils.split( department.get().toString(), "|");
						tmbPerson.setDepartment(StringUtils.trim(departmentarr[0]));
						tmbPerson.setDepartmentCode(StringUtils.trim(departmentarr[1]));
					}

					
					Attribute belongto = attrs.get("msExchExtensionCustomAttribute2");
					if(belongto != null) {
						tmbPerson.setBelongto(belongto.get().toString());
					}
					
					Attribute telephone = attrs.get("telephoneNumber");
					if(telephone != null) {
						tmbPerson.setTelephoneNo(telephone.get().toString());
					}
					
					Attribute mail = attrs.get("mail");
					if(mail != null) {
						tmbPerson.setEmail(mail.get().toString());
					}
					
					// check role name for return only role
					int value= 0;
					String roleName = "";
//					if (memberOfs != null) {
//						for (int i = 0; i < memberOfs.size(); i++) {
//							if(getValueRoleByRoleName(memberOfs.get(i).toString())> value) {
//								value = getValueRoleByRoleName(memberOfs.get(i).toString());
//							}
//						}
//						
//						if (value == 0) {
//							roleName = memberOfs.get(0).toString();
//						}else {
//							roleName = getRoleNameByValue(value);
//						}
//					}
//					List<String> listOfRole = new ArrayList<>();
//					listOfRole.add(roleName);
					
					
					tmbPerson.setMemberOfs(memberOfs);
					return tmbPerson;
				}
			});

			if (res.isEmpty()) {
				throw new Exception("Invalid Username and Password[not found in dir ldap]");
			}
			if (res.get(0).getMemberOfs().size() > 1) {
				throw new InternalAuthenticationServiceException("User ldap have more one role");
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
	
	public int  getValueRoleByRoleName(String roleName) {
		if(ADConstant.ROLE_SUPER.equals(roleName)) {
			return 4;
		}else if (ADConstant.ROLE_MAKER.equals(roleName)) {
			return 3;
		}else if (ADConstant.ROLE_CHECKER.equals(roleName)) {
			return 2;
		}else if(ADConstant.ROLE_REQUESTER.equals(roleName)) {
			return 1;
		}
		return 0;
	}
	
	public String  getRoleNameByValue(int value) {
		if(value == 4 ) {
			return ADConstant.ROLE_SUPER ;
		}else if (value == 3 ) {
			return ADConstant.ROLE_MAKER;
		}else if (value == 2 ) {
			return ADConstant.ROLE_CHECKER ;
		}else if(value == 1 ) {
			return ADConstant.ROLE_REQUESTER;
		}
		return null;
	}
	
}
