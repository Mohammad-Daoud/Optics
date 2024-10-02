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

    public void updateExamDetails(Exam updatedExam) {
        this.odSph = updatedExam.getOdSph();
        this.odCyl = updatedExam.getOdCyl();
        this.odAxis = updatedExam.getOdAxis();
        this.odAdd = updatedExam.getOdAdd();
        this.odLens = updatedExam.getOdLens();
        this.odVa = updatedExam.getOdVa();

        this.osSph = updatedExam.getOsSph();
        this.osCyl = updatedExam.getOsCyl();
        this.osAxis = updatedExam.getOsAxis();
        this.osAdd = updatedExam.getOsAdd();
        this.osLens = updatedExam.getOsLens();
        this.osVa = updatedExam.getOsVa();

        this.kReading = updatedExam.getKReading();
        this.contactLens = updatedExam.getContactLens();
        this.ipd = updatedExam.getIpd();
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", client=" + client +
                ", dateLastExam=" + dateLastExam +
                ", odSph='" + odSph + '\'' +
                ", odCyl='" + odCyl + '\'' +
                ", odAxis='" + odAxis + '\'' +
                ", odAdd='" + odAdd + '\'' +
                ", odLens='" + odLens + '\'' +
                ", odVa='" + odVa + '\'' +
                ", osSph='" + osSph + '\'' +
                ", osCyl='" + osCyl + '\'' +
                ", osAxis='" + osAxis + '\'' +
                ", osAdd='" + osAdd + '\'' +
                ", osLens='" + osLens + '\'' +
                ", osVa='" + osVa + '\'' +
                ", kReading='" + kReading + '\'' +
                ", contactLens='" + contactLens + '\'' +
                ", ipd='" + ipd + '\'' +
                '}';
    }
}

