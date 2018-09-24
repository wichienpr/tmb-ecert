package th.co.baiwa.buckwaframework.preferences.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
	
//	private final MessageRepository messageRepository;
//	
//	@Autowired
//	public MessageService(MessageRepository messageRepository) {
//		this.messageRepository = messageRepository;
//	}
//	
//	public List<Message> getMessageList(Message message, Pageable pageable) {
//		logger.info("getMessageAll");
//		
//		return messageRepository.findByCriteria(message, pageable);
//	}
//	
//	public Message getMessageById(Long messageId) {
//		logger.info("getMessage messageId={}", messageId);
//		
//		return messageRepository.findById(messageId).get();
//	}
//	
//	public int countMessage() {
//		logger.info("countMessage");
//		
//		return (int) messageRepository.count();
//	}
//	
//	public Message insertMessage(Message message) {
//		logger.info("insertMessage");
//		
//		messageRepository.save(message);
//		
//		return message;
//	}
//	
//	public void updateMessage(Message message) {
//		logger.info("updateMessage");
//		
//		messageRepository.save(message);
//	}
//	
//	public void deleteMessage(List<Long> ids) {
//		logger.info("deleteMessage");
//		
//		messageRepository.delete(ids);
//	}
	
}
