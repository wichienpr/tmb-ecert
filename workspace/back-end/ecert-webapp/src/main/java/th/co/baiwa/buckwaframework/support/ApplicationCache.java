package th.co.baiwa.buckwaframework.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tmb.ecert.common.dao.CertificateDao;
import com.tmb.ecert.common.dao.ListOfValueDao;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.ListOfValue;

@Component
public class ApplicationCache {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationCache.class);
	// ListOfValue `ECERT_LISTOFVALUE`
	private static final ConcurrentHashMap<Integer, List<ListOfValue>> LOV_GROUP_VALUE = new ConcurrentHashMap<Integer, List<ListOfValue>>();
	private static final List<ListOfValue> LOV_TYPE_VALUE = new ArrayList<ListOfValue>();
	// Certificate `ECERT_CERTIFICATE`
	private static final ConcurrentHashMap<String, List<Certificate>> CER_GROUP_VALUE = new ConcurrentHashMap<String, List<Certificate>>();
	private static final List<Certificate> CER_TYPE_VALUE = new ArrayList<Certificate>();

	// DAO
	private final ListOfValueDao lovDao;
	private final CertificateDao cerDao;

	@Autowired
	public ApplicationCache(CertificateDao cerDao, ListOfValueDao lovDao) {
		super();
		this.lovDao = lovDao;
		this.cerDao = cerDao;
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

	public static List<Certificate> getCetByType(String typeCode) {
		return CER_GROUP_VALUE.get(typeCode);
	}

	/** Certificate */

	
	/** Common Cache */

	/********************* Method for Get Cache - End *********************/

	/** Reload */
	@PostConstruct
	public synchronized void reloadCache() {
		logger.info("ApplicationCache Reloading...");
		loadLov();
		loadCer();
		logger.info("ApplicationCache Reloaded");
	}

	private void loadCer() {
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

	private void loadLov() {
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

}
