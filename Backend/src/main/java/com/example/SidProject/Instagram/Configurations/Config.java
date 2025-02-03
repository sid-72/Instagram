package com.example.SidProject.Instagram.Configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class Config {
    @Bean
    ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper;
    }

    @Bean
    S3Client gets3Client(AwsCredentials awsCredentials,
                         @Value("${aws.region}") String region) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    @Bean
    AwsCredentials getAwsCredentials(@Value("${aws.access-key-id}") String accessKey,
                                     @Value("${aws.secret-access-key}") String secretKey) {
        return AwsBasicCredentials.create(accessKey, secretKey);

    }

    @Bean
    S3Presigner getS3Presigner(AwsCredentials awsCredentials,
                               @Value("${aws.region}") String region) {
        return S3Presigner.builder()
                .region(Region.of(region))  // Set your region
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}
