package com.la;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.InputStream;
import java.security.KeyStore;

@SpringBootApplication
public class LiteraryAssociationApp {
    public static void main(String[] args) {
        SpringApplication.run(LiteraryAssociationApp.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        KeyStore keyStore;
        HttpComponentsClientHttpRequestFactory requestFactory = null;

        try {
            keyStore = KeyStore.getInstance("jks");
            ClassPathResource classPathResource = new ClassPathResource("literary-association.jks");
            InputStream inputStream = classPathResource.getInputStream();
            keyStore.load(inputStream, "123456".toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .loadKeyMaterial(keyStore, "123456".toCharArray()).build(),
                    NoopHostnameVerifier.INSTANCE);

            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setMaxConnTotal(Integer.valueOf(5))
                    .setMaxConnPerRoute(Integer.valueOf(5))
                    .build();

            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(Integer.valueOf(10000));
            requestFactory.setConnectTimeout(Integer.valueOf(10000));

            restTemplate.setRequestFactory(requestFactory);
        } catch (Exception exception) {
            System.out.println("Exception occured while creating restTemplate "+exception);
            exception.printStackTrace();
        }
        return restTemplate;
    }
}
