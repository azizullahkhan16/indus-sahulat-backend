package com.aktic.indussahulatbackend.repository.redis;

import com.aktic.indussahulatbackend.model.redis.RedisEventLiveLocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisEventLiveLocationRepository extends CrudRepository<RedisEventLiveLocation, Long> {
}
