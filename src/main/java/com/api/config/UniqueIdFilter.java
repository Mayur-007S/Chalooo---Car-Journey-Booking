package com.api.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.api.service.impl.APILogsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UniqueIdFilter extends OncePerRequestFilter {

	private static final String TRANSACTION_ID = "transactionId";
	private static final String CONVERSATION_ID = "correlationId";

	@Autowired
	private APILogsServiceImpl apiLogsServiceImpl;

	private Logger log = LoggerFactory.getLogger(UniqueIdFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    String transactionId = request.getHeader(TRANSACTION_ID);
	    if (!StringUtils.hasText(transactionId)) {
	        transactionId = UUID.randomUUID().toString().replace("-", "");
	    }

	    String conversationId = request.getHeader(CONVERSATION_ID);
	    if (!StringUtils.hasText(conversationId)) {
	        conversationId = UUID.randomUUID().toString().replace("-", "");
	    }

	    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request,100);
	    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

	    MDC.put(TRANSACTION_ID, transactionId);
	    MDC.put(CONVERSATION_ID, conversationId);

	    try {
	        long startTime = System.currentTimeMillis();
	        filterChain.doFilter(wrappedRequest, wrappedResponse);

	        String requestBody = getStringValue(wrappedRequest.getContentAsByteArray(), request.getCharacterEncoding());
	        String responseBody = getStringValue(wrappedResponse.getContentAsByteArray(), response.getCharacterEncoding());

	        log.info("URL: {}, METHOD: {}, STATUS: {}, REQUEST: {}, RESPONSE: {}",
	                startTime, request.getRequestURI(), request.getMethod(),
	                response.getStatus(), requestBody, responseBody);
	        if(requestBody.isEmpty()) {
	        	requestBody = request.getParameter(responseBody);
	        }
	        apiLogsServiceImpl.addLogs(transactionId, conversationId,
	                request.getRequestURI(), response.getStatus(),
	                requestBody, responseBody);

	        wrappedResponse.copyBodyToResponse();
	    } finally {
	        MDC.clear();
	    }
	}


	public String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
		// TODO Auto-generated method stub
		try {
			return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
