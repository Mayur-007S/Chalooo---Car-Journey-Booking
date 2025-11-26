package com.api.service;

import com.api.model.APILogs;

public interface APILogsService {

	APILogs addLogs(String transactionId, String conversationId,
            String requestURL, int status,
            String requestBody, String responseBody);
	
}
