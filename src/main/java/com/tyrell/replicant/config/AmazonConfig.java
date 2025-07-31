package com.tyrell.replicant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

// TODO BS security
@Configuration
public class AmazonConfig {

    @Autowired
    private MyConfigurationProperties props; // TODO switch to un-autowired constructor injection

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                props.getAccessKey(),
                props.getSecretKey()
        );

        // AWS SDK v2 LocalStack workaround: Only force path-style for LocalStack
        // Real AWS S3 works better with virtual-hosted-style (default)
        if (props.getServiceEndpoint().contains("localhost")) {
            return S3Client.builder()
                    .endpointOverride(URI.create(props.getServiceEndpoint()))
                    .region(Region.of(props.getSigningRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .forcePathStyle(true)  // Force path-style for LocalStack
                    .build();
        } else {
            return S3Client.builder()
                    .endpointOverride(URI.create(props.getServiceEndpoint()))
                    .region(Region.of(props.getSigningRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .build();
        }
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                props.getAccessKey(),
                props.getSecretKey()
        );

        // AWS SDK v2 LocalStack note: S3Presigner doesn't have forcePathStyle() method
        // The presigned URL path-style conversion is handled manually in the controller
        return S3Presigner.builder()
                .endpointOverride(URI.create(props.getServiceEndpoint()))
                .region(Region.of(props.getSigningRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}
