package com.aktic.indussahulatbackend.service.redis;

import com.aktic.indussahulatbackend.model.redis.Test;
import com.aktic.indussahulatbackend.repository.redis.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final TestRepository testRepository;

    public Test saveUser(Test user) {
        return testRepository.save(user); // Saves to Redis
    }

    public Optional<Test> getUserById(String id) {
        return testRepository.findById(id); // Fetches from Redis
    }

    public void deleteUser(String id) {
        testRepository.deleteById(id); // Deletes from Redis
    }

    public Iterable<Test> getAllUsers() {
        return testRepository.findAll(); // Returns all users from Redis
    }
}
