package com.covid19.vaccine.service;

import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
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

	public void sendMailWithRetry(final String to, final String body) throws EmailException {

		final Email email = new HtmlEmail();
		email.setHostName(mailServerHost);
		email.setSmtpPort(mailServerPort);
		email.setAuthenticator(new DefaultAuthenticator(mailServerUsername, mailServerPassword));
		email.setSSLOnConnect(true);
		email.setSSLCheckServerIdentity(true);
		email.setSocketConnectionTimeout(2000);
		email.setDebug(true);
		email.setFrom("admin@vaccine-help.com");
		email.setSubject(mailSubject);
		email.setMsg(body);
		email.addTo(to);
		email.send();
		log.info("Email Sent Successfully");
	}

}
