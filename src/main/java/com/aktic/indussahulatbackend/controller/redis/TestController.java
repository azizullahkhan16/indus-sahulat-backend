package com.aktic.indussahulatbackend.controller.redis;

import com.aktic.indussahulatbackend.model.redis.Test;
import com.aktic.indussahulatbackend.service.redis.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/patient/redis")
@RequiredArgsConstructor
@RestController
public class TestController {

    private final TestService testService;

    @PostMapping
    public Test addUser(@RequestBody Test user) {
        return testService.saveUser(user);
    }

    @GetMapping("/{id}")
    public Optional<Test> getUser(@PathVariable String id) {
        return testService.getUserById(id);
    }

    @GetMapping
    public Iterable<Test> getAllUsers() {
        return testService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        testService.deleteUser(id);
    }
}
