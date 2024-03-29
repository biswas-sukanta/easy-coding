package com.covid19.vaccine.service;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.covid19.vaccine.entity.EmailFrequency;
import com.covid19.vaccine.repository.EmailFrequencyRepository;

import lombok.extern.slf4j.Slf4j;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

@Service
@Slf4j
public class MailService {

	@Value("${spring.mail.host}")
	private String mailServerHost;

	@Value("${spring.mail.port}")
	private Integer mailServerPort;

	@Value("${spring.mail.username}")
	private String mailServerUsername;

	@Value("${spring.mail.password}")
	private String mailServerPassword;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String mailServerAuth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String mailServerStartTls;

	@Value("${spring.mail.properties.mail.transport.protocol}")
	private String transportPtotocol;

	Session mailSession;
	
	@Autowired
	EmailFrequencyRepository emailFrequencyRepository;
	
	@Autowired
	private Environment env;

	private MailService() {
	}

	public void sendMail(final String to, final String body, final String subject) {
		@SuppressWarnings("unchecked")
		final RetryPolicy retryPolicy = new RetryPolicy().withBackoff(2, 3, TimeUnit.MINUTES)
				.retryOn(EmailException.class).withMaxRetries(3);

		Failsafe.with(retryPolicy)
				.onRetry(r -> log.warn("Mail Send failed.. Retrying once again.. Error :: {}", r.getMessage()))
				.onFailure(e -> log.error("Error while sending mail :: {}", e.getMessage()))
				.run(() -> sendMailWithRetry(to, body, subject));
	}

	private void setMailServerProperties() {
		final Properties emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", mailServerPort);
		emailProperties.put("mail.smtp.auth", mailServerAuth);
		emailProperties.put("mail.smtp.starttls.enable", mailServerStartTls);
		emailProperties.put("mail.debug", "true");
		mailSession = Session.getDefaultInstance(emailProperties, null);
	}

	private MimeMessage draftEmailMessage(final String to, final String body, final String subject)
			throws MessagingException, UnsupportedEncodingException {
		final String[] toEmails = { to };
		final MimeMessage emailMessage = new MimeMessage(mailSession);
		for (final String toEmail : toEmails) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
		}
		emailMessage.setFrom(new InternetAddress("support@vaccinechecker.online", "VaccineChecker App"));
		emailMessage.setSubject(subject);
		emailMessage.setContent(body, "text/html");
		return emailMessage;
	}

	private void sendMailWithRetry(final String to, final String body, final String mailSubject) {
		setMailServerProperties();
		CompletableFuture.runAsync(() -> {
			try {
				final MimeMessage emailMessage = draftEmailMessage(to, body, mailSubject);
				final Transport transport = mailSession.getTransport(transportPtotocol);
				sendingMail(transport, emailMessage);
			} catch (MessagingException | UnsupportedEncodingException e) {
				log.error("Error while sending mail. {}", e);
			}
		});
	}

	/**
	 * @param transport
	 * @param emailMessage
	 * @throws MessagingException
	 */
	private void sendingMail(final Transport transport, final MimeMessage emailMessage) throws MessagingException {
		try {
			
			int hour = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getHour();
			log.info("Current Hour : {}", hour);

			EmailFrequency emailRecord = emailFrequencyRepository.findByHour(hour);
			
			if (null != emailRecord) {
				mailServerUsername = emailRecord.getEmailId();
				mailServerPassword = emailRecord.getPassword();
				log.info("Current Hour from DB, Email ID : {} Hour : {} ", emailRecord.getHour(), hour);
			}
			
			transport.connect(mailServerHost, mailServerUsername, mailServerPassword);
			transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
			log.info("Email sent successfully.");
		} catch (Exception e) {
			String username = env.getProperty("spring.mail.username");
			String password = env.getProperty("spring.mail.password");
			log.error("Error while sending mail. Now routing via alternate emailId {}, Exception : {}", username, e);
			transport.connect(mailServerHost, username, password);
			transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
			log.info("Email sent successfully via alternate way");
		} finally {
			transport.close();
		}
	}

}
