package com.aktic.indussahulatbackend.model.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "test")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Test implements Serializable {
    @Id
    private String id;
    private String name;
    private int age;
}
