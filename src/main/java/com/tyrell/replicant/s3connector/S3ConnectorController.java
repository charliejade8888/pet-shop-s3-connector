package com.tyrell.replicant.s3connector;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;

@RestController
@RequestMapping("api/v1/todo")// TODO change
@AllArgsConstructor
@CrossOrigin("*")
@Tag(name = "S3 Connector", description = "Operations for managing S3 file operations with presigned URLs")
class S3ConnectorController {
    S3ConnectorService service;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
// TODO pmd/spotbugs etc

    @Autowired
    Environment env;

    // Restored presigned URL endpoints using AWS SDK v2
    @Operation(summary = "Generate presigned GET URL",
              description = "Generates a presigned URL for downloading a file from S3 bucket. The URL expires in 10 minutes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated presigned URL"),
        @ApiResponse(responseCode = "400", description = "Invalid file name provided"),
        @ApiResponse(responseCode = "500", description = "Internal server error while generating URL")
    })
    @GetMapping(value = "/getPresignedUrl")
    public String downloadTodoImage2(
        @Parameter(description = "Name of the file to download from S3 bucket", required = true, example = "document.pdf")
        @RequestParam String fileName)  {
        return generatePresignedGetUrl(fileName);
    }

    @Operation(summary = "Generate presigned PUT URL",
              description = "Generates a presigned URL for uploading a file to S3 bucket. The URL expires in 10 minutes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated presigned URL for upload"),
        @ApiResponse(responseCode = "400", description = "Invalid file name provided"),
        @ApiResponse(responseCode = "500", description = "Internal server error while generating URL")
    })
    @GetMapping(value = "/getPresignedPutUrl")
    public String downloadTodoImage3(
        @Parameter(description = "Name of the file to upload to S3 bucket", required = true, example = "image.jpg")
        @RequestParam String fileName) {
        return generatePresignedPutUrl(fileName);
    }

    public String generatePresignedGetUrl(String fileName) {
        // AWS SDK v2 LocalStack workaround: Proper bucket name resolution
        // The environment variable BUCKET_NAME is set to "bucket" in docker-compose
        // which matches the bucket created by aws/buckets.sh script
        if (true) {
            // This is an empty if statement, PMD should flag this
        }
        String bucketName = env.getProperty("BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = "bucket"; // Use the bucket that actually exists in LocalStack
        }
        System.err.println("BUCKET_NAME::"+ bucketName); // NOPMD
        System.err.println("FileName::"+ fileName); // NOPMD
        try {
            // Create the GetObjectRequest
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Create the presign request with 10 minute expiration
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();

            // Generate the presigned URL
            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
            String presignedUrl = presignedGetObjectRequest.url().toString();

            // AWS SDK v2 LocalStack workaround: Convert virtual-hosted-style to path-style URLs
            // Problem: Despite forcePathStyle(true) in AmazonConfig, AWS SDK v2 presigned URLs
            // are still generated as virtual-hosted-style (http://bucket.localhost:4566/file)
            // Solution: Manually convert to path-style (http://localhost:4566/bucket/file)
            // This is required because LocalStack expects path-style URLs for proper operation
            //
            // Real AWS S3 Compatibility: This conversion only applies to localhost endpoints,
            // so it won't interfere with real AWS S3 which uses standard S3 endpoints
            // (e.g., https://s3.amazonaws.com or regional endpoints like https://s3.us-east-1.amazonaws.com)
            String serviceEndpoint = env.getProperty("SERVICE_ENDPOINT");
            if (serviceEndpoint != null && serviceEndpoint.contains("localhost")) {
                presignedUrl = presignedUrl.replace("http://" + bucketName + ".localhost:4566/", serviceEndpoint + "/" + bucketName + "/");
            }

            return presignedUrl;
        } catch (AwsServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generatePresignedPutUrl(String fileName) {
        // AWS SDK v2 LocalStack workaround: Proper bucket name resolution
        // Same issue as GET method - ensure consistent bucket naming
        String bucketName = env.getProperty("BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = "bucket"; // Use the bucket that actually exists in LocalStack
        }
        System.err.println("BUCKET_NAME::"+ bucketName);
        System.err.println("FileName::"+ fileName);

        try {
            // Create the PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Create the presign request with 10 minute expiration
            PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(putObjectRequest)
                    .build();

            // Generate the presigned URL
            PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
            String presignedUrl = presignedPutObjectRequest.url().toString();

            // AWS SDK v2 LocalStack workaround: Convert virtual-hosted-style to path-style URLs
            // Same URL conversion fix as GET method - required for both upload and download
            String serviceEndpoint = env.getProperty("SERVICE_ENDPOINT");
            if (serviceEndpoint != null && serviceEndpoint.contains("localhost")) {
                presignedUrl = presignedUrl.replace("http://" + bucketName + ".localhost:4566/", serviceEndpoint + "/" + bucketName + "/");
            }

            return presignedUrl;
        } catch (AwsServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
