package th.co.baiwa.buckwaframework.preferences.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ParameterInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(ParameterInfoService.class);
	
//	private final ParameterInfoRepository parameterInfoRepository;
//	
//	@Autowired
//	public ParameterInfoService(ParameterInfoRepository parameterInfoRepository) {
//		this.parameterInfoRepository = parameterInfoRepository;
//	}
	
//	public List<ParameterInfo> getParameterInfoList(Integer start, Integer length) {
//		logger.info("getParameterInfoList");
//		
//		return parameterInfoRepository.findAll();
//	}
//	
//	public ParameterInfo getParameterInfoById(Long paramInfoId) {
//		logger.info("getParameterInfoById paramInfoId={}", paramInfoId);
//		
//		return parameterInfoRepository.findById(paramInfoId).get();
//	}
//	
//	public long countParameterInfo() {
//		logger.info("countParameterInfo");
//		
//		return parameterInfoRepository.count();
//	}
//	
//	public ParameterInfo insertParameterInfo(ParameterInfo parameterInfo) {
//		logger.info("insertParameterInfo");
//		
//		parameterInfoRepository.save(parameterInfo);
//		
//		return parameterInfo;
//	}
//	
//	public void updateParameterInfo(ParameterInfo parameterInfo) {
//		logger.info("updateParameterInfo");
//		
//		parameterInfoRepository.save(parameterInfo);
//	}
//	
//	public void deleteParameterInfo(Long paramInfoId) {
//		logger.info("deleteParameterInfo");
//		
//		parameterInfoRepository.deleteById(paramInfoId);
//	}
	
}
