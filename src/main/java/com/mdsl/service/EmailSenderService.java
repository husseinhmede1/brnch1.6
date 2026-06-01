package com.mdsl.service;


import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	
	private JavaMailSender mailSender;
	
	  @Autowired public EmailSenderService(JavaMailSender mailSender) { super();
	  this.mailSender = mailSender; }
	   
	  public void sendMail(String from,String to, String subject, String body) {
		    Thread thread =
		        new Thread(
		            new Runnable() {

		              @Override
		              public void run() {
		                try {

		                	SimpleMailMessage message = new SimpleMailMessage();

		            		message.setFrom(from);
		            		message.setTo(to);
		            		message.setSubject(subject);
		            		message.setText(body);
		                 
		            		mailSender.send(message);
		                  
		                    System.out.println("Email Sent Successfully");
		                } catch (Exception e) {
		                  e.printStackTrace();
		                }
		              }
		            });
		    thread.start();
		  }
	  
	  public void sendHtmlMail(String from, String to, String subject, String htmlBody) {
	        Thread thread = new Thread(() -> {
	            try {
	                MimeMessage message = mailSender.createMimeMessage();
	                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	                
	                helper.setFrom(from);
	                helper.setTo(to);
	                helper.setSubject(subject);
	                helper.setText(htmlBody, true);
	                
	                mailSender.send(message);
	                
                    System.out.println("Email Sent Successfully");
	            } catch (Exception e) {
	                System.out.println("Failed to send email to:"+ to);
	                e.printStackTrace();
	            }
	        });
	        thread.start();
	    }
}