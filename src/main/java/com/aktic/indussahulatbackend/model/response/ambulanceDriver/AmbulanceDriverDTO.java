package com.aktic.indussahulatbackend.model.response.ambulanceDriver;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AmbulanceDriverDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String CNIC;
    private String phone;
    private Integer age;
    private String image;
}
