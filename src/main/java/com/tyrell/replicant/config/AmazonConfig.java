package com.tyrell.replicant.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// TODO BS security
@Configuration
public class AmazonConfig {

    @Autowired
    private MyConfigurationProperties props; // TODO switch to un-autowired constructor injection

    @Bean
    public AmazonS3 F() {

        // TODO BS get running w gradle 9/update native gradle
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(props.getAccessKey(), props.getSecretKey());

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(props.getServiceEndpoint(), props.getSigningRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

    }
}
