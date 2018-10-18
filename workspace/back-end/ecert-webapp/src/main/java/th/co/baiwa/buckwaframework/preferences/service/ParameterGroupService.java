package th.co.baiwa.buckwaframework.preferences.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ParameterGroupService {
	
	private static final Logger logger = LoggerFactory.getLogger(ParameterGroupService.class);
	
//	private final ParameterGroupRepository parameterGroupRepository;
//	
//	@Autowired
//	public ParameterGroupService(ParameterGroupRepository parameterGroupRepository) {
//		this.parameterGroupRepository = parameterGroupRepository;
//	}
	
//	public List<ParameterGroup> getParameterGroupList(Integer start, Integer length) {
//		logger.info("getParameterGroupList");
//		
//		return parameterGroupRepository.findAll();
//	}
//	
//	public ParameterGroup getParameterGroupById(Long paramGroupId) {
//		logger.info("getParameterGroupById paramGroupId={}", paramGroupId);
//		
//		return parameterGroupRepository.findById(paramGroupId).get();
//	}
//	
//	public long countParameterGroup() {
//		logger.info("countParameterGroup");
//		
//		return parameterGroupRepository.count();
//	}
//	
//	public ParameterGroup insertParameterGroup(ParameterGroup parameterGroup) {
//		logger.info("insertParameterGroup");
//		
//		parameterGroupRepository.save(parameterGroup);
//		
//		return parameterGroup;
//	}
//	
//	public void updateParameterGroup(ParameterGroup parameterGroup) {
//		logger.info("updateParameterGroup");
//		
//		parameterGroupRepository.save(parameterGroup);
//	}
//	
//	public void deleteParameterGroup(Long paramGroupId) {
//		logger.info("deleteParameterGroup");
//		
//		parameterGroupRepository.deleteById(paramGroupId);
//	}
	
}
