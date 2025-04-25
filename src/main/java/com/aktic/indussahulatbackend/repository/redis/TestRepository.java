package com.aktic.indussahulatbackend.repository.redis;

import com.aktic.indussahulatbackend.model.redis.Test;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<Test, String> {
    // No implementation needed — Spring Data Redis will auto-implement this
}