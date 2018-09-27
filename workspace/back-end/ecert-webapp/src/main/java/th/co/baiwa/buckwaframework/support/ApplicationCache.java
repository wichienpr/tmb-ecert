package th.co.baiwa.buckwaframework.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tmb.ecert.common.lov.service.ListOfValueService;
import com.tmb.ecert.domain.ListOfValue;

@Component
public class ApplicationCache {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationCache.class);
	private static final ConcurrentHashMap<Integer, List<ListOfValue>> LOV_GROUP_VALUE = new ConcurrentHashMap<Integer, List<ListOfValue>>();
	private static final List<ListOfValue> LOV_TYPE_VALUE = new ArrayList<ListOfValue>();
//	private static final ConcurrentHashMap<String, ParameterGroupWrapper> PARAMETER_GROUP_MAP = new ConcurrentHashMap<String, ParameterGroupWrapper>();
//	private static final ConcurrentHashMap<Long, ParameterInfo> PARAMETER_INFO_MAP = new ConcurrentHashMap<Long, ParameterInfo>();
//	private static final ConcurrentHashMap<String, Message> MESSAGE_MAP = new ConcurrentHashMap<String, Message>();
//	private static final ConcurrentHashMap<String, Object> COMMON_CACHE = new ConcurrentHashMap<String, Object>();
	
//	private final ParameterGroupRepository parameterGroupRepository;
//	private final ParameterInfoRepository parameterInfoRepository;
//	private final MessageRepository messageRepository;
	private final ListOfValueService lovService;
	
	@Autowired
	public ApplicationCache(
//			ParameterGroupRepository parameterGroupRepository,
//			ParameterInfoRepository parameterInfoRepository,
//			MessageRepository messageRepository,
			ListOfValueService lovService
			) {
		super();
//		this.parameterGroupRepository = parameterGroupRepository;
//		this.parameterInfoRepository = parameterInfoRepository;
//		this.messageRepository = messageRepository;
		this.lovService = lovService;
	}
	
	/********************* Method for Get Cache - Start *********************/
	
	/** Parameter Group & Parameter Info */
//	public static ParameterGroup getParameterGroupByCode(String paramGroupCode) {
//		ParameterGroup result = PARAMETER_GROUP_MAP.get(paramGroupCode).getParameterGroup();
//		return result;
//	}
//	
//	public static ParameterInfo getParameterInfoById(Long paramInfoId) {
//		return PARAMETER_INFO_MAP.get(paramInfoId);
//	}
//	
//	public static ParameterInfo getParameterInfoByCode(String paramGroupCode, String paramInfoCode) {
//		return PARAMETER_GROUP_MAP.get(paramGroupCode).getParameterInfoCodeMap().get(paramInfoCode);
//	}
	/** ListOfValue */
	public static List<Object> getLovAll() {
		List<ListOfValue> types = LOV_TYPE_VALUE;
		List<Object> lovs = new ArrayList<>();
		for(ListOfValue type: types) {
			lovs.add(LOV_GROUP_VALUE.get(type.getType()));
		}
		return lovs;
	}
	
	public static List<ListOfValue> getLovByType(Integer type) {
		return LOV_GROUP_VALUE.get(type);
	}
	
	static final class ParameterGroupWrapper {
		
//		private ParameterGroup parameterGroup;
//		private Map<String, ParameterInfo> parameterInfoCodeMap = new HashMap<String, ParameterInfo>();
//		
//		public ParameterGroupWrapper(ParameterGroup paramGroup, List<ParameterInfo> paramInfoList) {
//			this.parameterGroup = paramGroup;
//			for (ParameterInfo paramInfo : paramInfoList) {
//				parameterInfoCodeMap.put(paramInfo.getParamCode(), paramInfo);
//			}
//		}
//		
//		public ParameterGroup getParameterGroup() {
//			return parameterGroup;
//		}
//		
//		public void setParameterGroup(ParameterGroup parameterGroup) {
//			this.parameterGroup = parameterGroup;
//		}
//		
//		public Map<String, ParameterInfo> getParameterInfoCodeMap() {
//			return parameterInfoCodeMap;
//		}
//		
//		public void setParameterInfoCodeMap(Map<String, ParameterInfo> parameterInfoCodeMap) {
//			this.parameterInfoCodeMap = parameterInfoCodeMap;
//		}
		
	}
	
	/** Message */
//	public static Map<String, Message> getMessages() {
//		return MESSAGE_MAP;
//	}
//	
//	public static Message getMessage(String messageCode) {
//		return MESSAGE_MAP.get(messageCode);
//	}
//	
//	public static String getMessage(String messageCode, String lang) {
//		Message message = MESSAGE_MAP.get(messageCode);
//		String msgDesc = null;
//		if (MESSAGE_LANG.EN.equals(lang)) {
//			msgDesc = message.getMessageEn();
//		} else if (MESSAGE_LANG.TH.equals(lang)) {
//			msgDesc = message.getMessageTh();
//		}
//		return msgDesc;
//	}
	
	/** Common Cache */
//	public static Object getCommonCache(String cacheName) {
//		return COMMON_CACHE.get(cacheName);
//	}
	
	/********************* Method for Get Cache - End *********************/
	
	/** Reload */
	@PostConstruct
	public synchronized void reloadCache() {
		logger.info("ApplicationCache Reloading...");
		loadLov();
		logger.info("ApplicationCache Reloaded");
	}
	
	private void loadLov() {
		logger.info("load `ListOfValue` loading...");
		LOV_GROUP_VALUE.clear();
		List<ListOfValue> types = lovService.lovAllType();
		List<ListOfValue> lovs = new ArrayList<>();
		for(ListOfValue type: types) {
			lovs = lovService.lovByType(type.getType());
			LOV_TYPE_VALUE.add(type);
			LOV_GROUP_VALUE.put(type.getType(), lovs);
		}
	}
	
}
