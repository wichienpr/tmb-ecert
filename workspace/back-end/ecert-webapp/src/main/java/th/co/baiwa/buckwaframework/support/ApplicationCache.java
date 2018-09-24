package th.co.baiwa.buckwaframework.support;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationCache {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationCache.class);
	
//	private static final ConcurrentHashMap<String, ParameterGroupWrapper> PARAMETER_GROUP_MAP = new ConcurrentHashMap<String, ParameterGroupWrapper>();
//	private static final ConcurrentHashMap<Long, ParameterInfo> PARAMETER_INFO_MAP = new ConcurrentHashMap<Long, ParameterInfo>();
//	private static final ConcurrentHashMap<String, Message> MESSAGE_MAP = new ConcurrentHashMap<String, Message>();
//	private static final ConcurrentHashMap<String, Object> COMMON_CACHE = new ConcurrentHashMap<String, Object>();
	
//	private final ParameterGroupRepository parameterGroupRepository;
//	private final ParameterInfoRepository parameterInfoRepository;
//	private final MessageRepository messageRepository;
	
	@Autowired
	public ApplicationCache(
//			ParameterGroupRepository parameterGroupRepository,
//			ParameterInfoRepository parameterInfoRepository,
//			MessageRepository messageRepository
			) {
		super();
//		this.parameterGroupRepository = parameterGroupRepository;
//		this.parameterInfoRepository = parameterInfoRepository;
//		this.messageRepository = messageRepository;
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
		
//		loadParameterGroup();
		
//		loadMessage();
		
		logger.info("ApplicationCache Reloaded");
	}
	
//	private void loadParameterGroup() {
//		logger.info("load ParamterGroup-Info loading...");
//		
//		PARAMETER_GROUP_MAP.clear();
//		PARAMETER_INFO_MAP.clear();
//		
//		List<ParameterGroup> paramGroupList = parameterGroupRepository.findAll();
//		List<ParameterInfo> paramInfoList = null;
//		for (ParameterGroup paramGroup : paramGroupList) {
//			logger.debug("load ParameterGroup [id] : " + paramGroup.getParamGroupId() + ",\t[parameterGroupCode] : " + paramGroup.getParamGroupCode());
//			
//			paramInfoList = parameterInfoRepository.findByParamGroupId(paramGroup.getParamGroupId());
//			for (ParameterInfo paramInfo : paramInfoList) {
//				PARAMETER_INFO_MAP.put(paramInfo.getParamInfoId(), paramInfo);
//			}
//
//			PARAMETER_GROUP_MAP.put(paramGroup.getParamGroupCode(), new ParameterGroupWrapper(paramGroup, paramInfoList));
//		}
//		
//		logger.info("load ParamterGroup-Info loaded [" + PARAMETER_GROUP_MAP.size() + "-" + PARAMETER_INFO_MAP.size() + "]");
//	}
	
	
	
//	private void loadMessage() {
//		logger.info("load Message loading...");
//		
//		MESSAGE_MAP.clear();
//		
//		List<Message> messageList = messageRepository.findAll();
//		for (Message message : messageList) {
//			MESSAGE_MAP.put(message.getMessageCode(), message);
//		}
//		
//		logger.info("load Message loaded [" + MESSAGE_MAP.size() + "]");
//	}
	
}
