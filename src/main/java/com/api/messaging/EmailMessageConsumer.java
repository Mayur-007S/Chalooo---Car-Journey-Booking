/*
 * package com.api.messaging;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.jms.annotation.JmsListener; import
 * org.springframework.stereotype.Service;
 * 
 * import com.api.mail.service.MailService; import com.api.model.EmailRequest;
 * 
 * @Service public class EmailMessageConsumer {
 * 
 * private MailService mailService;
 * 
 * private Logger log = LoggerFactory.getLogger(EmailMessageConsumer.class);
 * 
 * public EmailMessageConsumer(MailService mailService) { this.mailService =
 * mailService; }
 * 
 * @JmsListener(destination = "email_queue") public void
 * consumeEmailMessage(EmailRequest emailRequest) { try {
 * mailService.sendLoginEmail(emailRequest.getTo(), emailRequest.getBody(),
 * emailRequest.getFrom());
 * 
 * log.info("✅ Email successfully processed for: " + emailRequest.getTo()); }
 * catch (Exception e) { log.info("❌ Error sending email: " + e.getMessage()); }
 * } }
 */