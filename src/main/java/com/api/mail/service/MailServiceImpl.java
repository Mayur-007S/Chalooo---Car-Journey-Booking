package com.api.mail.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService{

	private JavaMailSender javaMailSender;
	
	public MailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmail(String to, String subject, String username)
			throws MessagingException, IOException {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
		
		helper.setTo(to);
		helper.setSubject(subject);
		
		/*
		 * String htmlContext = new String(Files.readAllBytes(Paths.get(
		 * "D:\\mayur111\\FullStackWebDevelopment" +
		 * "\\NEW_JAVA2025\\SpringBootMailSender\\src" +
		 * "\\main\\resources\\templates\\index.html")));
		 */
		String context = """ 
						Loging Successfully.
		Welcome aboard! Your Chaloo App account is ready.
				{username}, Welcome to Chaloo App.
				
		We're absolutely thrilled to welcome you to the 
		Chaloo App family. Your account registration 
		is complete, and you are officially ready to start.
		
		Click the button below to jump straight into your 
		dashboard and explore all the powerful 
		tools designed just for you.

		We're here to support you every step of the way! 
		If you have any questions or run into trouble, 
		please don't hesitate to reply directly to this 
		email.
		
		Cheers,
        The Chaloo Team		
				""";
		
		context.replace("{username}", username);
		
		helper.setText(context, true);
		
		javaMailSender.send(message);
	}

}
