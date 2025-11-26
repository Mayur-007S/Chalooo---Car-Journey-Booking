package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.model.APILogs;
import com.api.repository.APILogsRepository;
import com.api.service.APILogsService;

@Service
public class APILogsServiceImpl implements APILogsService {

	@Autowired
	private APILogsRepository apiLogsRepository;
	private Logger log = LoggerFactory.getLogger(APILogsRepository.class);
	private List<?> response;
	
	@Override
	public APILogs addLogs(String transactionId, String conversationId,
            String requestURL, int status,
            String requestBody, String responseBody) 
	{
		log.info("Inside API Logs Save Method");
		APILogs apiLogs = new APILogs();
		apiLogs.setConversationId(conversationId);
		apiLogs.setTransactionId(transactionId);
		apiLogs.setRequest(requestBody);
		if(responseBody.length() > 255) {
			responseBody = responseBody.substring(0, 255);
		}
		apiLogs.setResponse(responseBody);
		apiLogs.setNotification(String.valueOf(status));
		apiLogs.setContract(requestURL);
		
		
		return apiLogsRepository.save(apiLogs);
	}

}
