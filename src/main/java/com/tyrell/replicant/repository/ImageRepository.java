package com.tyrell.replicant.repository;

import com.tyrell.replicant.model.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
    Image findByTitle(String title);
}
