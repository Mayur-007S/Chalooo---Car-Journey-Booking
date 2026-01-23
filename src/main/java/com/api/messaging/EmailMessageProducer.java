/*
 * package com.api.messaging;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.jms.core.JmsTemplate; import
 * org.springframework.stereotype.Service;
 * 
 * import com.api.mail.service.MailService; import com.api.model.EmailRequest;
 * 
 * @Service public class EmailMessageProducer {
 * 
 * 
 * private JmsTemplate jmsTemplate;
 * 
 * private Logger log = LoggerFactory.getLogger(EmailMessageProducer.class);
 * 
 * public EmailMessageProducer(JmsTemplate jmsTemplate) { this.jmsTemplate =
 * jmsTemplate; }
 * 
 * public void sendEmailMessage(EmailRequest emailRequest) {
 * jmsTemplate.convertAndSend("email_queue", emailRequest);
 * log.info("📨 Sent message to email.queue for: {} ", emailRequest.getTo()); }
 * 
 * }
 */