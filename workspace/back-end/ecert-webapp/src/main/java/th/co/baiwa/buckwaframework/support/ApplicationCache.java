package th.co.baiwa.buckwaframework.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tmb.ecert.common.dao.CertificateDao;
import com.tmb.ecert.common.dao.ListOfValueDao;
import com.tmb.ecert.common.dao.ParameterConfigDao;
import com.tmb.ecert.common.dao.RoleDao;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.ListOfValue;
import com.tmb.ecert.common.domain.ParameterConfig;
import com.tmb.ecert.common.domain.RoleVo;

@Component
public class ApplicationCache {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationCache.class);
	// ListOfValue `ECERT_LISTOFVALUE`
	private static final ConcurrentHashMap<Integer, List<ListOfValue>> LOV_GROUP_VALUE = new ConcurrentHashMap<Integer, List<ListOfValue>>();
	private static final List<ListOfValue> LOV_TYPE_VALUE = new ArrayList<ListOfValue>();
	// Certificate `ECERT_CERTIFICATE`
	private static final ConcurrentHashMap<String, List<Certificate>> CER_GROUP_VALUE = new ConcurrentHashMap<String, List<Certificate>>();
	private static final List<Certificate> CER_TYPE_VALUE = new ArrayList<Certificate>();
	// ParameterConfig `ECERT_PARAMETER_CONFIG`
	private static final List<ParameterConfig> PARAM_GROUP_VALUE = new ArrayList<ParameterConfig>();
	// ParameterConfig `ECERT_ROLE AND ECERT_ROLE_PERMISSION`
	private static final List<RoleVo> ROLE_GROUP_VALUE = new ArrayList<RoleVo>();

	// DAO
	private static ListOfValueDao lovDao;
	private static CertificateDao cerDao;
	private static ParameterConfigDao paramDao;
	private static RoleDao roleDao;

	@Autowired
	public ApplicationCache(CertificateDao cerDao, ListOfValueDao lovDao, ParameterConfigDao paramDao,
			RoleDao roleDao) {
		super();
		this.lovDao = lovDao;
		this.cerDao = cerDao;
		this.paramDao = paramDao;
		this.roleDao = roleDao;
	}

	/********************* Method for Get Cache - Start *********************/

	/** ListOfValue */

	public static List<Object> getLovAll() {
		List<ListOfValue> types = LOV_TYPE_VALUE;
		List<Object> lovs = new ArrayList<>();
		for (ListOfValue type : types) {
			lovs.add(LOV_GROUP_VALUE.get(type.getType()));
		}
		return lovs;
	}

	public static List<ListOfValue> getLovByType(Integer type) {
		return LOV_GROUP_VALUE.get(type);
	}
	
	public static ListOfValue getLovByCode(String code) {
		if(code!=null) {
			List<ListOfValue> types = LOV_TYPE_VALUE;
			for (ListOfValue type : types) {
				int typeCode = type.getType();
				List<ListOfValue> lovs = LOV_GROUP_VALUE.get(typeCode);
				for(ListOfValue lov: lovs) {
					if (code.equals(lov.getCode())) {
						return lov;
					}
				}
			}
		}
		
		return null;
	}

	/** ListOfValue */

	/** Certificate */

	public static List<Object> getCerAll() {
		List<Certificate> types = CER_TYPE_VALUE;
		List<Object> lovs = new ArrayList<>();
		for (Certificate type : types) {
			lovs.add(CER_GROUP_VALUE.get(type.getTypeCode()));
		}
		return lovs;
	}

	public static List<Certificate> getCerByType(String typeCode) {
		return CER_GROUP_VALUE.get(typeCode);
	}
	
	public static Certificate getCerByCode(String code) {
		List<Certificate> types = CER_TYPE_VALUE;
		for (Certificate type : types) {
			String typeCode = type.getTypeCode();
			List<Certificate> cers = CER_GROUP_VALUE.get(typeCode);
			for(Certificate cer: cers) {
				if (code.equals(cer.getCode())) {
					return cer;
				}
			}
		}
		return null;
	}

	/** Certificate */

	/** ParameterConfig */

	public static List<ParameterConfig> getParamAll() {
		return PARAM_GROUP_VALUE;
	}
	
	public static String getParamValueByName(String propertyName) {
		if (StringUtils.isBlank(propertyName)) return null;
		
		List<ParameterConfig> paramConfigs = PARAM_GROUP_VALUE;
		for (ParameterConfig param : paramConfigs) {
			if (propertyName.trim().equalsIgnoreCase(param.getPropertyName().trim())) {
				return param.getPropertyValue();
			}
		}
		return null;
	}

	/** ParameterConfig */

	/** RoleVo */

	public static List<RoleVo> getRoleAll() {
		return ROLE_GROUP_VALUE;
	}

	/** RoleVo */

	/** Common Cache */

	/********************* Method for Get Cache - End *********************/

	/** Reload */
	@PostConstruct
	public synchronized void reloadCache() {
		logger.info("ApplicationCache Reloading...");
		loadLov();
		loadCer();
		loadParam();
		loadRole();
		logger.info("ApplicationCache Reloaded");
	}
	
	public static void reloadCacheNow() {
		logger.info("ApplicationCache Reloading...");
//		LOV_GROUP_VALUE.clear();
//		List<ListOfValue> lovsTypes = lovDao.lovAllType();
//		List<ListOfValue> lovs = new ArrayList<>();
//		for (ListOfValue type : lovsTypes) {
//			lovs = lovDao.lovByType(type.getType());
//			LOV_TYPE_VALUE.add(type);
//			LOV_GROUP_VALUE.put(type.getType(), lovs);
//		}
//		CER_GROUP_VALUE.clear();
//		List<Certificate> cersTypes = cerDao.findAllTypeCode();
//		List<Certificate> cers = new ArrayList<>();
//		for (Certificate type : cersTypes) {
//			cers = cerDao.findByTypeCode(type.getTypeCode());
//			CER_TYPE_VALUE.add(type);
//			CER_GROUP_VALUE.put(type.getTypeCode(), cers);
//		}
//		PARAM_GROUP_VALUE.clear();
//		List<ParameterConfig> params = paramDao.findAll();
//		for (ParameterConfig item : params) {
//			PARAM_GROUP_VALUE.add(item);
//		}
//		ROLE_GROUP_VALUE.clear();
//		List<RoleVo> roles = roleDao.findRoleJoinRolePermission();
//		for (RoleVo item : roles) {
//			ROLE_GROUP_VALUE.add(item);
//		}
		loadLov();
		loadCer();
		loadParam();
		loadRole();
		logger.info("ApplicationCache Reloaded");
	}


	private static void loadCer() {
		logger.info("load `Certificate` loading...");
		CER_GROUP_VALUE.clear();
		List<Certificate> types = cerDao.findAllTypeCode();
		List<Certificate> cers = new ArrayList<>();
		for (Certificate type : types) {
			cers = cerDao.findByTypeCode(type.getTypeCode());
			CER_TYPE_VALUE.add(type);
			CER_GROUP_VALUE.put(type.getTypeCode(), cers);
		}
	}

	private static void loadLov() {
		logger.info("load `ListOfValue` loading...");
		LOV_GROUP_VALUE.clear();
		List<ListOfValue> types = lovDao.lovAllType();
		List<ListOfValue> lovs = new ArrayList<>();
		for (ListOfValue type : types) {
			lovs = lovDao.lovByType(type.getType());
			LOV_TYPE_VALUE.add(type);
			LOV_GROUP_VALUE.put(type.getType(), lovs);
		}
	}

	private static void loadParam() {
		logger.info("load `ParameterConfig` loading...");
		PARAM_GROUP_VALUE.clear();
		List<ParameterConfig> items = paramDao.findAll();
		for (ParameterConfig item : items) {
			PARAM_GROUP_VALUE.add(item);
		}
	}

	private static void loadRole() {
		logger.info("load `RoleVo` loading...");
		ROLE_GROUP_VALUE.clear();
		List<RoleVo> items = roleDao.findRoleJoinRolePermission();
		for (RoleVo item : items) {
			ROLE_GROUP_VALUE.add(item);
		}
	}

}
