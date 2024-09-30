package com.project.optics.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private LocalDate dateLastExam;

    // Measurements for both eyes (OD/OS)
    private String odSph;
    private String odCyl;
    private String odAxis;
    private String odAdd;
    private String odLens;
    private String odVa;

    private String osSph;
    private String osCyl;
    private String osAxis;
    private String osAdd;
    private String osLens;
    private String osVa;

    private String kReading;
    private String contactLens;
    private String ipd;
}

