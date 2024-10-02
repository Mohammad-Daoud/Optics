package com.project.optics.controllers;

import com.project.optics.models.Exam;
import com.project.optics.services.ExamService;
import com.project.optics.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("client", clientService.getClientById(clientId));
        model.addAttribute("exam", new Exam());
        return "add-exam";
    }

    @PostMapping("/add")
    public String addExam(@RequestParam("id") Long clientId,
                          @ModelAttribute("exam") @Valid Exam exam,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("client", clientService.getClientById(clientId));
            return "add-exam";
        }
        examService.addExam(exam, clientId);
        return "redirect:/clients/view?id=" + clientId;
    }

    @GetMapping("/edit")
    public String showEditExamForm(@RequestParam("id") Long examId, Model model) {
        model.addAttribute("exam", examService.getExamById(examId));
        return "edit-exam";
    }

    @PostMapping("/edit")
    public String editExam(@ModelAttribute("exam") @Valid Exam exam, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-exam";
        }
        examService.updateExam(exam);
        return "redirect:/clients/view?id=" + examService.getExamById(exam.getId()).getClient().getId();
    }

    @GetMapping("/delete")
    public String deleteExam(@RequestParam("id") Long examId) {
        long clientId = examService.getClientIdByExamId(examId);
        examService.deleteExamById(examId);
        return "redirect:/clients/view?id=" + clientId;
    }
}
