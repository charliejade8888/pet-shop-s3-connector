package com.tyrell.replicant.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tyrell.replicant.repository.ImageRepository;
import com.tyrell.replicant.model.Image;
import com.tyrell.replicant.config.BucketName;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
@AllArgsConstructor
public class S3ConnectorService {
    private final FileStore fileStore;
    private final ImageRepository repository;

    public Image saveImage(String title, String description, MultipartFile file) {
        //check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        } // TODO check if apache http getmimtype usesmagic numbers to determine file type


        // TODO use magic numbers to determine file type
        // https://stackoverflow.com/questions/51438/checking-a-file-is-gif-image-or-not-in-java
        // implement determine file type
        // https://www.baeldung.com/java-magic-number
        // https://www.baeldung.com/java-file-mime-type
        // https://www.baeldung.com/java-file-extension


        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        //Save Image in S3 and then save Todo in the database
        String path = String.format("%s/%s", BucketName.TODO_IMAGE.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());
        try {
            fileStore.upload(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        Image image = Image.builder()
                .description(description)
                .title(title)
                .imagePath(path)
                .imageFileName(fileName)
                .build();
        repository.save(image);
        return repository.findByTitle(image.getTitle());
    }

    public byte[] downloadImage(Long id) {
        Image image = repository.findById(id).get();
        return fileStore.download(image.getImagePath(), image.getImageFileName());
    }


    public List<Image> getAllImages() {
        List<Image> images = new ArrayList<>();
        repository.findAll().forEach(images::add);
        return images;
    }
}
