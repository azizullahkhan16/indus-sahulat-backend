package com.aktic.indussahulatbackend.config;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.repository.ambulanceProvider.AmbulanceProviderRepository;
import com.aktic.indussahulatbackend.repository.company.CompanyRepository;
import com.aktic.indussahulatbackend.repository.hospital.HospitalRepository;
import com.aktic.indussahulatbackend.repository.question.QuestionRepository;
import com.aktic.indussahulatbackend.repository.questionnaire.QuestionnaireRepository;
import com.aktic.indussahulatbackend.repository.role.RoleRepository;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializerConfig {
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final HospitalRepository hospitalRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceProviderRepository ambulanceProviderRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmbulanceDriverRepository ambulanceDriverRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionRepository questionRepository;

    @Bean
    @Order(1)
    CommandLineRunner insertRoles() {
        return args -> {
            try {
                if (!roleRepository.existsByRoleName("ROLE_PATIENT")) {
                    Role patientRole = Role.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .roleName("ROLE_PATIENT")
                            .description("This is a patient role")
                            .build();
                    roleRepository.save(patientRole);
                    log.info("PATIENT role inserted successfully.");
                }

                // create role for Ambulance Driver
                if (!roleRepository.existsByRoleName("ROLE_AMBULANCE_DRIVER")) {
                    Role ambulanceDriverRole = Role.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .roleName("ROLE_AMBULANCE_DRIVER")
                            .description("This is an ambulance driver role")
                            .build();
                    roleRepository.save(ambulanceDriverRole);
                    log.info("AMBULANCE_DRIVER role inserted successfully.");
                }

                // create role for Ambulance Provider
                if (!roleRepository.existsByRoleName("ROLE_AMBULANCE_PROVIDER")) {
                    Role ambulanceProviderRole = Role.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .roleName("ROLE_AMBULANCE_PROVIDER")
                            .description("This is an ambulance provider role")
                            .build();
                    roleRepository.save(ambulanceProviderRole);
                    log.info("AMBULANCE_PROVIDER role inserted successfully.");
                }
            } catch (Exception e) {
                log.error("Error inserting roles: " + e.getMessage());
            }
        };
    }


    @Bean
    @Order(2)
    CommandLineRunner insertCompanies() {
        return args -> {
            try {
                if (!companyRepository.existsByName("Indus Healthcare")) {
                    Company company1 = Company.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .name("Indus Healthcare")
                            .phone("+92 21 12345678")
                            .email("contact@indushealthcare.com")
                            .website("https://www.indushealthcare.com")
                            .description("A leading healthcare provider in Pakistan.")
                            .build();
                    companyRepository.save(company1);
                    log.info("Company 'Indus Healthcare' inserted successfully.");
                }

                if (!companyRepository.existsByName("Rescue 1122")) {
                    Company company2 = Company.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .name("Rescue 1122")
                            .phone("+92 42 98765432")
                            .email("info@rescue1122.pk")
                            .website("https://www.rescue1122.pk")
                            .description("Pakistan's premier emergency ambulance service.")
                            .build();

                    companyRepository.save(company2);
                    log.info("Company 'Rescue 1122' inserted successfully.");
                }
            } catch (Exception e) {
                log.error("Error inserting companies: " + e.getMessage());
            }
        };
    }

    @Bean
    @Order(3)
    CommandLineRunner insertHospitals() {
        return args -> {
            try {
                if (!hospitalRepository.existsByName("Indus Hospital Karachi")) {
                    Hospital hospital1 = Hospital.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .name("Indus Hospital Karachi")
                            .phone("+92 21 12345679")
                            .email("karachi@indushospital.org")
                            .website("https://www.indushospital.org")
                            .address(new Location(24.8607F, 67.0011F))
                            .image("https://example.com/indus_karachi.jpg")
                            .build();

                    hospitalRepository.save(hospital1);
                    log.info("Hospital 'Indus Hospital Karachi' inserted successfully.");
                }

                if (!hospitalRepository.existsByName("Agha Khan Karachi")) {
                    Hospital hospital2 = Hospital.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .name("Aga Khan Karachi")
                            .phone("+92 42 12345678")
                            .email("akhan@rescue1122.pk")
                            .website("https://www.rescue1122.pk")
                            .address(new Location(31.5204F, 74.3587F)) // Replace with actual latitude and longitude
                            .image("https://example.com/rescue_1122_karachi.jpg")
                            .build();

                    hospitalRepository.save(hospital2);
                    log.info("Hospital 'Aga Khan Karachi' inserted successfully.");
                }
            } catch (Exception e) {
                log.error("Error inserting hospitals: " + e.getMessage());
            }
        };
    }

    @Bean
    @Order(4)
    CommandLineRunner insertAmbulances() {
        return args -> {
            try {
                Company company1 = companyRepository.findByName("Indus Healthcare").orElseThrow(() ->
                        new IllegalArgumentException("Company 'Indus Healthcare' not found."));

                Company company2 = companyRepository.findByName("Rescue 1122").orElseThrow(() ->
                        new IllegalArgumentException("Company 'Rescue 1122' not found."));

                if (!ambulanceRepository.existsByLicensePlate("ABC-123")) {
                    Ambulance ambulance1 = Ambulance.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .company(company1)
                            .make("Toyota")
                            .model("Hiace")
                            .year("2020")
                            .licensePlate("ABC-123")
                            .ambulanceType(AmbulanceType.NORMAL)
                            .color("White")
                            .image("https://example.com/toyota_hiace.jpg")
                            .build();

                    ambulanceRepository.save(ambulance1);
                    log.info("Ambulance with license plate 'ABC-123' inserted successfully.");
                }

                if (!ambulanceRepository.existsByLicensePlate("OPQ-098")) {
                    Ambulance ambulance3 = Ambulance.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .company(company1)
                            .make("Toyota")
                            .model("Hiace")
                            .year("2024")
                            .licensePlate("OPQ-098")
                            .ambulanceType(AmbulanceType.ADVANCED)
                            .color("Red")
                            .image("https://example.com/toyota_hiace.jpg")
                            .build();

                    ambulanceRepository.save(ambulance3);
                    log.info("Ambulance with license plate 'OPQ-098' inserted successfully.");
                }

                if (!ambulanceRepository.existsByLicensePlate("XYZ-456")) {
                    Ambulance ambulance2 = Ambulance.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .company(company2)
                            .make("Ford")
                            .model("Transit")
                            .year("2021")
                            .licensePlate("XYZ-456")
                            .ambulanceType(AmbulanceType.ADVANCED)
                            .color("Red")
                            .image("https://example.com/ford_transit.jpg")
                            .build();

                    ambulanceRepository.save(ambulance2);
                    log.info("Ambulance with license plate 'XYZ-456' inserted successfully.");
                }
            } catch (Exception e) {
                log.error("Error inserting ambulances: " + e.getMessage());
            }
        };
    }

    @Bean
    @Order(5)
    CommandLineRunner insertQuestionnaire() {
        return args -> {
            try {
                // Check if the questionnaire already exists
                if (!questionnaireRepository.existsByTitle("Emergency Response Questionnaire")) {
                    Questionnaire questionnaire = Questionnaire.builder()
                            .id(1L)
                            .title("Emergency Response Questionnaire")
                            .description("A questionnaire to assess the condition of a patient during emergencies.")
                            .build();

                    questionnaireRepository.save(questionnaire);
                    log.info("Questionnaire 'Emergency Response Questionnaire' inserted successfully.");
                }
            } catch (Exception e) {
                log.error("Error inserting questionnaire: " + e.getMessage(), e);
            }
        };
    }

    @Bean
    @Order(6)
    CommandLineRunner insertQuestions() {
        return args -> {
            try {
                // Fetch the questionnaire by title
                Questionnaire questionnaire = questionnaireRepository.findByTitle("Emergency Response Questionnaire")
                        .orElseThrow(() -> new IllegalArgumentException("Questionnaire 'Emergency Response Questionnaire' not found."));

                // Check if the questions already exist
                if (questionRepository.existsByQuestionnaireId(questionnaire.getId())) {
                    log.info("Questions for 'Emergency Response Questionnaire' already exist.");
                    return;
                }

                // Define questions and options
                List<Question> questions = List.of(
                        Question.builder()
                                .id(snowflakeIdGenerator.nextId())
                                .question("Does The Patient Have Any Known Medical Conditions?")
                                .questionnaire(questionnaire) // Associate existing questionnaire
                                .options(List.of(
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Diabetes").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Heart Disease").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Cancer").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Allergies").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Other").build()
                                ))
                                .build(),
                        Question.builder()
                                .id(snowflakeIdGenerator.nextId())
                                .question("Is The Patient Conscious And Breathing?")
                                .questionnaire(questionnaire)
                                .options(List.of(
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Yes").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("No").build()
                                ))
                                .build(),
                        Question.builder()
                                .id(snowflakeIdGenerator.nextId())
                                .question("What Are The Patient's Current Symptoms Or Condition?")
                                .questionnaire(questionnaire)
                                .options(List.of(
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Unresponsive").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Severe Bleeding").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Chest Pain").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Difficulty Breathing").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Other").build()
                                ))
                                .build(),
                        Question.builder()
                                .id(snowflakeIdGenerator.nextId())
                                .question("What Is The Nature Of The Emergency?")
                                .questionnaire(questionnaire)
                                .options(List.of(
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Accident").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Heart Attack").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Stroke Injury").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Other").build()
                                ))
                                .build(),
                        Question.builder()
                                .id(snowflakeIdGenerator.nextId())
                                .question("Are There Any Additional Services Needed?")
                                .questionnaire(questionnaire)
                                .options(List.of(
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Neonatal").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Severe Bleeding").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Trauma Care").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Oxygen Supply").build(),
                                        Option.builder().id(snowflakeIdGenerator.nextId()).optionText("Other").build()
                                ))
                                .build()
                );

                // Save questions
                questionRepository.saveAll(questions);
                log.info("Questions for 'Emergency Response Questionnaire' inserted successfully.");
            } catch (Exception e) {
                log.error("Error inserting questions: " + e.getMessage(), e);
            }
        };
    }

}
