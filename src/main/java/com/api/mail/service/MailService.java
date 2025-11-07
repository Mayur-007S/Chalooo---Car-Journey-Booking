package com.api.mail.service;

import java.io.IOException;
import jakarta.mail.MessagingException;

public interface MailService {

	public void sendEmail(String to, String subject, String username) throws MessagingException, IOException;

}
