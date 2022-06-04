/**
 * @author Sukanta Biswas
 *
 */
package com.covid19.vaccine.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

	@Value("${spring.mail.host}")
	private String mailServerHost;

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

	@Bean
	public RestTemplate restTemplate() {
		final CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		return new RestTemplate(requestFactory);
	}

	/*
	 * @Bean public RestTemplate restTemplate(RestTemplateBuilder builder) throws
	 * Exception {
	 * 
	 * String keyPassphrase = "apisetu"; KeyStore keyStore =
	 * KeyStore.getInstance("JKS"); keyStore.load(new
	 * FileInputStream("apisetu.jks"), keyPassphrase.toCharArray()); SSLContext
	 * sslContext = SSLContexts.custom().loadKeyMaterial(keyStore,
	 * keyPassphrase.toCharArray()).build(); HttpClient httpClient =
	 * HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(
	 * NoopHostnameVerifier.INSTANCE).build();
	 * 
	 * 
	 * final SSLContext sslContext = SSLContextBuilder.create()
	 * .loadKeyMaterial(keyStore("/selfsigned.jks", "apiseti".toCharArray()),
	 * "apiseti".toCharArray()) .loadTrustMaterial(null, new
	 * TrustSelfSignedStrategy()).build();
	 * 
	 * 
	 * final HttpClient client = HttpClients.custom().setSslcontext(sslContext)
	 * .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
	 * 
	 * return builder.requestFactory(() -> new
	 * HttpComponentsClientHttpRequestFactory(httpClient)).build(); }
	 */

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

}
