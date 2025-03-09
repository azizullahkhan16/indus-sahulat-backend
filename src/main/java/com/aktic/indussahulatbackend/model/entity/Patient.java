package com.aktic.indussahulatbackend.model.entity;

import com.aktic.indussahulatbackend.model.enums.BloodType;
import com.aktic.indussahulatbackend.model.enums.GenderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Patient {
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "CNIC", unique = true, nullable = false)
    private String CNIC;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "weight", nullable = false)
    private float weight;

    @Column(name = "height", nullable = false)
    private float height;

    @Column(name = "blood_type", nullable = false)
    private BloodType bloodType;

    @Column(name="gender", nullable = false)
    private GenderType gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "image")
    private String image;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.isVerified = this.isVerified != null && this.isVerified;
    }

}
