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
import java.util.ArrayList;
import java.util.List;


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
    public String addClient(@ModelAttribute("client") @Valid Client client,
                            @RequestParam("imageFiles") MultipartFile[] imageFiles, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "add-client";
        }
        // Handle multiple image uploads
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                String imageUrl = clientService.saveClientImage(imageFile);
                imageUrls.add(imageUrl);  // Add the image URL to the list
            }
        }
        // Set the list of image URLs in the client
        client.setImageUrls(imageUrls);
        clientService.addClient(client);
        return "redirect:/clients/view?id=" + client.getId(); // Redirect to client view
    }
    @GetMapping("/view")
    public String viewClient(@RequestParam("id") Long id, Model model) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            // Handle the case where the client is not found
            return "redirect:/clients"; // Redirect to clients list if client is not found
        }
        model.addAttribute("client", client);
        return "view-client"; // Return the Thymeleaf template to view client details
    }

    @GetMapping("/delete")
    public String deleteClient(@RequestParam("id") Long id) {
        clientService.deleteClientById(id);
        return "redirect:/clients"; // Redirect to the client list page after deletion
    }

    @GetMapping("/search")
    public String searchClients(@RequestParam("query") String query,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                Model model) {
        Page<Client> clientsPage = clientService.searchClients(query, PageRequest.of(page, size));
        model.addAttribute("clientsPage", clientsPage);
        model.addAttribute("query", query); // Keep the search query in the model
        return "clients";
    }

    @GetMapping("/edit")
    public String showEditClientForm(@RequestParam("id") Long clientId, Model model) {
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return "redirect:/clients"; // Redirect if client not found
        }
        model.addAttribute("client", client);
        return "edit-client"; // Thymeleaf template for editing client
    }

    // Handle form submission for editing a client
    @PostMapping("/edit")
    public String editClient(@ModelAttribute("client") @Valid Client client,@RequestParam("imageFile") MultipartFile imageFile,
                             BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            return "edit-client"; // Show the form again if there are validation errors
        }
        // Handle image upload
 /*       if (!imageFile.isEmpty()) {
            String imageUrl = clientService.saveClientImage(imageFile);
            client.setImageUrls(imageUrl);
        }*/
        clientService.updateClient(client);
        return "redirect:/clients/view?id=" + client.getId(); // Redirect to client detail page
    }


}

