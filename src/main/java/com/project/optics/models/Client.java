package com.project.optics.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @NotBlank (message = "first name is required")
    private String firstName;

    private String secondName;
    private String thirdName;

    @NotBlank(message = "last name is required")
    private String lastName;

    private String address;
    private String phoneNumber;
    private String poBox;

    @Column(updatable = false)
    private LocalDate dateOfCreation;

    private int age;
    private String occupation;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Exam> exams = new ArrayList<>();

    private String imageUrl;

}
