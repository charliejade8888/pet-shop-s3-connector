package com.tyrell.replicant.s3connector;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
class FileStore {
    private final S3Client s3Client;

    public void upload(String path,
                       String fileName,
                       Optional<Map<String, String>> optionalMetaData,
                       InputStream inputStream) {
        try {
            PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                    .bucket("spring-amazon-storage-bill")
                    .key(fileName);

            // Add metadata if present
            optionalMetaData.ifPresent(map -> {
                if (!map.isEmpty()) {
                    requestBuilder.metadata(map);
                }
            });

            PutObjectRequest putObjectRequest = requestBuilder.build();

            // Read all bytes from input stream for RequestBody
            byte[] content = inputStream.readAllBytes();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        } catch (AwsServiceException | IOException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public byte[] download(String path, String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("spring-amazon-storage-bill")
                    .key(key)
                    .build();

            return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
        } catch (AwsServiceException e) {
            throw new IllegalStateException("Failed to download the file", e);
        }
    }
}
