package com.tyrell.replicant.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    TODO_IMAGE("bucket"); // spring-amazon-storage-bill
    private final String bucketName;
}
