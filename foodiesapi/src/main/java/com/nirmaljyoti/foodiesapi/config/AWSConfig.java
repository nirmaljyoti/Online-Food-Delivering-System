package com.nirmaljyoti.foodiesapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {

	@Value("AKIA5H2IQN5ZSFL3GU5V")
	private String accessKey;

	@Value("qmE9OVkaG2OkudI1r5y0D5Y2Y+UK1LVsX+aEvhOO")
	private String secretKey;

	@Value("eu-north-1")
	private String region;

	@Bean
	public S3Client s3Client() {
		return S3Client.builder().region(Region.of(region))
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
				.build();
	}

}
