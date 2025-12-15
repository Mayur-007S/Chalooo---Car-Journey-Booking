package com.api.mail.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.api.model.Booking;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService{

	private JavaMailSender javaMailSender;
	private SpringTemplateEngine templateEngine;
	private Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	
	public MailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
	}

	@Override
	public void sendEmail(String to, String subject, String username)
			throws MessagingException, IOException {
		log.info("Inside send email method.!!!");
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

	@Override
	public void confirmEmailtoPassenger(String passenger_email, Booking book) throws MessagingException {
		log.info("Inside confirm Email to Passenger method");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
		
		Context context = new Context();
		context.setVariable("book", book); 
		
		String html = templateEngine.process("emailTemplate", context);
		
		helper.setTo(passenger_email);
        helper.setSubject("Trip Confirmed – Chaloo");
		helper.setText(html, true);
		log.info("Email has sent to: {}",passenger_email);
		javaMailSender.send(message);
	}

}
