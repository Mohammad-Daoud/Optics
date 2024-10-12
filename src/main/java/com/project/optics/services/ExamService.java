package com.project.optics.services;

import com.project.optics.exceptions.ClientNotFoundException;
import com.project.optics.exceptions.ExamNotFoundException;
import com.project.optics.models.Exam;
import com.project.optics.models.Client;
import com.project.optics.repositories.ExamRepository;
import com.project.optics.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return examRepository.findById(id)
                .orElseThrow(() -> new ExamNotFoundException(id));
    }

    public Long getClientIdByExamId(Long examId) {
        return getExamById(examId).getClient().getId();
    }

    public void addExam(Exam exam, Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
        exam.setClient(client);
        exam.setDateLastExam(LocalDate.now());
        exam.setId(null);
        examRepository.save(exam);
    }

    public void updateExam(Exam exam) {
        Exam existingExam = getExamById(exam.getId());

        existingExam.updateExamDetails(exam);
        examRepository.save(existingExam);
    }

    public void deleteExamById(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ExamNotFoundException(examId);
        }
        examRepository.deleteById(examId);
    }
}
