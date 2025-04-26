package com.aktic.indussahulatbackend.repository.redis;

import com.aktic.indussahulatbackend.model.redis.RedisEventAmbulanceAssignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisEventAmbulanceAssignmentRepository extends CrudRepository<RedisEventAmbulanceAssignment, Long> {
}
