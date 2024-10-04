package com.project.optics.controllers;

import com.project.optics.models.Client;
import com.project.optics.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String viewAllClients(Model model,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Client> clientsPage = clientService.getAllClients(PageRequest.of(page, size));
        model.addAttribute("clientsPage", clientsPage);
        return "clients";
    }

    @GetMapping("/add")
    public String showAddClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "add-client";
    }

    @PostMapping("/add")
    public String addClient(@ModelAttribute("client") @Valid Client client, BindingResult result, Model model) {

        // Check if a client with the same name combination already exists
        if (clientService.clientExists(client.getFirstName(), client.getSecondName(), client.getThirdName(), client.getLastName())) {
            result.rejectValue("firstName", "error.client", "A client with a similar name already exists.");
        }

        if (result.hasErrors()) {
            return "add-client";  // Return to the form if validation fails
        }

        clientService.addClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/view")
    public String viewClient(@RequestParam("id") Long id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));
        return "view-client";
    }

    @GetMapping("/delete")
    public String deleteClient(@RequestParam("id") Long id) {
        clientService.deleteClientById(id);
        return "redirect:/clients";
    }

    @GetMapping("/edit")
    public String showEditClientForm(@RequestParam("id") Long clientId, Model model) {
        model.addAttribute("client", clientService.getClientById(clientId));
        return "edit-client";
    }

    @PostMapping("/edit")
    public String editClient(@ModelAttribute("client") @Valid Client client,
                             BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "edit-client";
        }
        clientService.updateClient(client);
        return "redirect:/clients/view?id=" + client.getId();
    }
}
