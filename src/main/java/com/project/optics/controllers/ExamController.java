package com.project.optics.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.project.optics.models.Exam;
import com.project.optics.services.ExamService;
import com.project.optics.services.ClientService;
import com.project.optics.models.Client;


@Controller
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;
    private final ClientService clientService;

    @Autowired
    public ExamController(ExamService examService, ClientService clientService) {
        this.examService = examService;
        this.clientService = clientService;
    }

    @GetMapping("/add")
    public String showAddExamForm(@RequestParam("id") Long clientId, Model model) {
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            // Handle the case where the client is not found
            return "redirect:/clients"; // Redirect to clients list if client is not found
        }
        model.addAttribute("clientId", clientId);
        model.addAttribute("exam", new Exam());
        return "add-exam"; // Return the Thymeleaf template to add an exam
    }

    @PostMapping("/add")
    public String addExam(@RequestParam("clientId") Long clientId,
                          @ModelAttribute("exam") @Valid Exam exam,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientId", clientId);
            return "add-exam"; // Return the form again if there are validation errors
        }
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return "redirect:/clients"; // Redirect to clients list if client is not found
        }
        exam.setClient(client); // Associate the exam with the client
        examService.addExam(exam, clientId);
        return "redirect:/clients/view?id=" + clientId; // Redirect to the client detail page after adding an exam
    }

    @GetMapping("/edit")
    public String showEditExamForm(@RequestParam("id") Long examId, Model model) {
        Exam exam = examService.getExamById(examId);
        if (exam == null) {
            return "redirect:/clients"; // Redirect if exam not found
        }
        model.addAttribute("exam", exam);
        return "edit-exam"; // Thymeleaf template for editing exam
    }

    // Handle form submission for editing an exam
    @PostMapping("/edit")
    public String editExam(@ModelAttribute("exam") @Valid Exam exam, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit-exam"; // Show the form again if there are validation errors
        }

        examService.updateExam(exam);
        return "redirect:/clients/view?id=" + exam.getClient().getId(); // Redirect to client detail page
    }

    @GetMapping("/delete")
    public String deleteExam(@RequestParam("id") Long examId ) {
        Exam exam = examService.getExamById(examId);
        long clientID = exam.getClient().getId();
        examService.deleteExam(exam);
        return "redirect:/clients/view?id=" + clientID; // Redirect to client detail page
    }
}

