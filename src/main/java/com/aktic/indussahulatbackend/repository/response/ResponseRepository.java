package com.aktic.indussahulatbackend.repository.response;

import com.aktic.indussahulatbackend.model.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Integer>
{

}
