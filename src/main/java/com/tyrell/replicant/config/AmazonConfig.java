package com.tyrell.replicant.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {
    @Bean
    public AmazonS3 F() {

        // TODO BS get running w gradle 9/update native gradle
        // TODO BS - pass env var to application.yml - google how to do this
        AWSCredentials awsCredentials =
                new BasicAWSCredentials("localstack", "localstack");
        return AmazonS3ClientBuilder
                .standard()
//                .withRegion("us-east-1")
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

    }
}
