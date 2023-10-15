package com.tyrell.replicant.rest;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.tyrell.replicant.model.Image;
import com.tyrell.replicant.service.S3ConnectorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("api/v1/todo")// TODO change
@AllArgsConstructor
@CrossOrigin("*")
public class S3ConnectorController {
    S3ConnectorService service;
    private final AmazonS3 amazonS3;
// TODO pmd/spotbugs etc

    // TODO refactor like this public void uploadObject(final String bucketName, final Path path)
    @GetMapping(value = "/getPresignedUrl")
    public String downloadTodoImage2(@RequestParam String fileName)  {
        return generatePresignedGetUrl(fileName);
    }

    // TODO need to utlise this to actually upload
    // TODO refactor like this public void uploadObject(final String bucketName, final Path path)
    @GetMapping(value = "/getPresignedPutUrl")
    public String downloadTodoImage3(@RequestParam String fileName) {
        return generatePresignedPutUrl(fileName);
    } // for postman use Body type binary then select the file to upload

    @Autowired
    Environment env;
    public String generatePresignedGetUrl(String fileName) {
        String objectKey = fileName;
        String bucketProp = env.getProperty("BUCKET_NAME");
        String bucketName = bucketProp == null ? "" : bucketProp;
        System.err.println("BUCKET_NAME::"+ bucketName);
        try {
            // Set the presigned URL to expire after 10 mins.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 10;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                            .withMethod(HttpMethod.GET)
//                            .withContentType("image/jpeg")
                            .withExpiration(expiration);
            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
//            logger.info("pre-signed URL for GET operation has been generated.");
            return url.toString();
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return null;
    }

    public String generatePresignedPutUrl(String fileName) {
        String bucketProp = env.getProperty("BUCKET_NAME");
        String bucketName = bucketProp == null ? "" : bucketProp;
        System.err.println("BUCKET_NAME::"+ bucketName);
        try {
            // Set the pre-signed URL to expire after 10 mins.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 10;
            expiration.setTime(expTimeMillis);

            // Generate the pre-signed URL
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                    .withMethod(HttpMethod.PUT)
//                    .withContentType("image/jpeg")
                    .withExpiration(expiration);
            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            //logger.info("pre-signed URL for PUT operation has been generated.");
            return url.toString();
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //logger.error("URL could not be generated");
        return null;
    }



}
