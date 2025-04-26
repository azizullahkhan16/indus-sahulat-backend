package com.aktic.indussahulatbackend.repository.redis;

import com.aktic.indussahulatbackend.model.redis.RedisEventHospitalAssignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisEventHospitalAssignmentRepository extends CrudRepository<RedisEventHospitalAssignment, Long> {
}
