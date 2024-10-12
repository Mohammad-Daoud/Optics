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
    private String odFrame;
    private String odExtra;

    private String osSph;
    private String osCyl;
    private String osAxis;
    private String osAdd;
    private String osLens;
    private String osVa;
    private String osFrame;
    private String osExtra;

    private String kReading;
    private String contactLens;
    private String ipd;
    private String note;

    public void updateExamDetails(Exam updatedExam) {
        this.odSph = updatedExam.getOdSph();
        this.odCyl = updatedExam.getOdCyl();
        this.odAxis = updatedExam.getOdAxis();
        this.odAdd = updatedExam.getOdAdd();
        this.odLens = updatedExam.getOdLens();
        this.odVa = updatedExam.getOdVa();
        this.odFrame = updatedExam.getOdFrame();
        this.odExtra = updatedExam.getOdExtra();

        this.osSph = updatedExam.getOsSph();
        this.osCyl = updatedExam.getOsCyl();
        this.osAxis = updatedExam.getOsAxis();
        this.osAdd = updatedExam.getOsAdd();
        this.osLens = updatedExam.getOsLens();
        this.osVa = updatedExam.getOsVa();
        this.osFrame = updatedExam.getOsFrame();
        this.osExtra = updatedExam.getOsExtra();

        this.kReading = updatedExam.getKReading();
        this.contactLens = updatedExam.getContactLens();
        this.ipd = updatedExam.getIpd();
        this.note = updatedExam.getNote();
    }
}

