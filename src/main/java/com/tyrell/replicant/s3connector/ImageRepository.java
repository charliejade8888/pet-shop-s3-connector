package com.tyrell.replicant.s3connector;

import com.tyrell.replicant.s3connector.Image;
import org.springframework.data.repository.CrudRepository;

interface ImageRepository extends CrudRepository<Image, Long> {
    Image findByTitle(String title);
}
