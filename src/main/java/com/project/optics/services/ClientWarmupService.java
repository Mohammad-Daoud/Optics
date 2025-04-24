package com.project.optics.services;

import com.project.optics.models.Client;
import com.project.optics.repositories.ClientRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class ClientWarmupService {

    private final ClientRepository clientRepository;

    public ClientWarmupService(ClientRepository clientRepository) {
        System.out.println("ClientWarmupService is running...");
        this.clientRepository = clientRepository;
    }

    @PostConstruct
    public void populateSearchNameOnStartup() {
        System.out.println("ClientWarmupService is running...");
        // Fetch all clients
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            // Populate the searchName field
            StringBuilder searchNameBuilder = new StringBuilder();

            if (client.getFirstName() != null) {
                searchNameBuilder.append(client.getFirstName().replaceAll("\\s+", " ").trim()).append(" ");
            }
            if (client.getSecondName() != null) {
                searchNameBuilder.append(client.getSecondName().replaceAll("\\s+", " ").trim()).append(" ");
            }
            if (client.getThirdName() != null) {
                searchNameBuilder.append(client.getThirdName().replaceAll("\\s+", " ").trim()).append(" ");
            }
            if (client.getLastName() != null) {
                searchNameBuilder.append(client.getLastName().replaceAll("\\s+", " ").trim());
            }

            client.setSearchName(searchNameBuilder.toString().replaceAll("\\s+", " ").toLowerCase());
        }

        // Save all updated clients back to the database
        clientRepository.saveAll(clients);
    }
}