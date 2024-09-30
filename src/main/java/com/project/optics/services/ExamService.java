package com.project.optics.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.optics.models.Exam;
import com.project.optics.models.Client;
import com.project.optics.repositories.ExamRepository;
import com.project.optics.repositories.ClientRepository;

import java.time.LocalDate;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ExamService(ExamRepository examRepository, ClientRepository clientRepository) {
        this.examRepository = examRepository;
        this.clientRepository = clientRepository;
    }


    public Exam getExamById(Long id) {
        return examRepository.findById(id).orElse(null);
    }
    public Exam addExam(Exam exam, Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        exam.setClient(client); // Associate the exam with the client
        exam.setDateLastExam(LocalDate.now());
        return examRepository.save(exam);
    }

    public Exam updateExam(Exam exam) {
        // Find the existing exam and update its details
        Exam existingExam = examRepository.findById(exam.getId()).orElseThrow(() -> new RuntimeException("Exam not found"));
        exam.setClient(existingExam.getClient());
        existingExam.setDateLastExam(exam.getDateLastExam());

        existingExam.setOdSph(exam.getOdSph());
        existingExam.setOsSph(exam.getOsSph());

        existingExam.setOdCyl(exam.getOdCyl());
        existingExam.setOsCyl(exam.getOsCyl());

        existingExam.setOdAxis(exam.getOdAxis());
        existingExam.setOsAxis(exam.getOsAxis());

        existingExam.setOdAdd(exam.getOdAdd());
        existingExam.setOsAdd(exam.getOsAdd());

        existingExam.setOdLens(exam.getOdLens());
        existingExam.setOsLens(exam.getOsLens());


        existingExam.setOdVa(exam.getOdVa());
        existingExam.setOsVa(exam.getOsVa());

        existingExam.setContactLens(exam.getContactLens());
        existingExam.setIpd(exam.getIpd());
        existingExam.setKReading(exam.getKReading());
        return examRepository.save(existingExam);
    }

    public void deleteExam(Exam exam) {
        examRepository.deleteById(exam.getId());
    }
}

