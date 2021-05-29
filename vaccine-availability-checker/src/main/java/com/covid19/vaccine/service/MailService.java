package com.covid19.vaccine.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	@Value("${spring.mail.sub}")
	private String mailSubject;

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

	private MailService() {
	}

	public void sendMail(final String to, final String body) {
		@SuppressWarnings("unchecked")
		final RetryPolicy retryPolicy = new RetryPolicy().withBackoff(2, 3, TimeUnit.MINUTES)
				.retryOn(EmailException.class).withMaxRetries(3);

		Failsafe.with(retryPolicy)
				.onRetry(r -> log.warn("Mail Send failed.. Retrying once again.. Error :: {}", r.getMessage()))
				.onFailure(e -> log.error("Error while sending mail :: {}", e.getMessage()))
				.run(() -> sendMailWithRetry(to, body));
	}

	/*
	 * public void sendMailWithRetry(final String to, final String body) throws
	 * EmailException {
	 * 
	 * try { final Email email = new HtmlEmail(); log.info("Host and Port : {}  {}",
	 * mailServerHost, mailServerPort); log.info("Username and Password : {}  {}",
	 * mailServerUsername, mailServerPassword); email.setHostName(mailServerHost);
	 * // email.setSmtpPort(465); email.setAuthenticator(new
	 * DefaultAuthenticator(mailServerUsername, mailServerPassword));
	 * email.setSSLOnConnect(true); email.setSSLCheckServerIdentity(true);
	 * email.setSocketConnectionTimeout(2000); email.setDebug(true);
	 * email.setFrom("admin@vaccine-help.com", "Vaccine Checker App" );
	 * email.setSubject(mailSubject); email.setMsg(body); email.addTo(to);
	 * email.send(); log.info("Email Sent Successfully"); } catch (EmailException e)
	 * { log.error("Error while sending mail : {}", e); } }
	 */

	private void setMailServerProperties() {
		final Properties emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", mailServerPort);
		emailProperties.put("mail.smtp.auth", mailServerAuth);
		emailProperties.put("mail.smtp.starttls.enable", mailServerStartTls);
		emailProperties.put("mail.debug", "true");
		mailSession = Session.getDefaultInstance(emailProperties, null);
	}

	private MimeMessage draftEmailMessage(final String to, final String body)
			throws MessagingException, UnsupportedEncodingException {
		final String[] toEmails = { to };
		final MimeMessage emailMessage = new MimeMessage(mailSession);
		for (final String toEmail : toEmails) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
		}
		emailMessage.setFrom(new InternetAddress("admin@vaccine-checker.com", "Vaccine Checker App"));
		emailMessage.setSubject(mailSubject);
		emailMessage.setContent(body, "text/html");
		return emailMessage;
	}

	private void sendMailWithRetry(final String to, final String body) throws MessagingException {
		setMailServerProperties();
		try {
			final Transport transport = mailSession.getTransport(transportPtotocol);
			transport.connect(mailServerHost, mailServerUsername, mailServerPassword);
			final MimeMessage emailMessage = draftEmailMessage(to, body);
			transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
			transport.close();
			log.info("Email sent successfully.");
		} catch (final MessagingException | UnsupportedEncodingException e) {
			log.error("Error while sending mail : {}", e);
		}
	}

}
