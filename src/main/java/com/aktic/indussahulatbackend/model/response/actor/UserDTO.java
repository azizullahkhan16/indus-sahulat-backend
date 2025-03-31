package com.aktic.indussahulatbackend.model.response.actor;

import com.aktic.indussahulatbackend.model.common.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String cnic;
    private Boolean isVerified;
    private Instant createdAt;
    private Instant updatedAt;
    private String role;
    private String image;
    private Integer age;

    public UserDTO(UserBase user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.cnic = user.getCNIC();
        this.phone = user.getPhone();
        this.isVerified = user.getIsVerified();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.role = user.getRole().getRoleName();
        this.image = user.getImage();
        this.age = user.getAge();

    }
}
