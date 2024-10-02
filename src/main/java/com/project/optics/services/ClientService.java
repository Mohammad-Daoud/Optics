package com.project.optics.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.project.optics.models.Client;
import com.project.optics.repositories.ClientRepository;

import java.time.LocalDate;

@Service
public class ClientService {


    private final ClientRepository clientRepository;


    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public void addClient(Client client) {
        client.setDateOfCreation(LocalDate.now());
        clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    public void updateClient(Client client) {
        // Find the existing client and update its details
        Client existingClient = clientRepository.findById(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        existingClient.setFirstName(client.getFirstName());
        existingClient.setSecondName(client.getSecondName());
        existingClient.setThirdName(client.getThirdName());
        existingClient.setLastName(client.getLastName());
        existingClient.setAddress(client.getAddress());
        existingClient.setPhoneNumber(client.getPhoneNumber());
        existingClient.setPoBox(client.getPoBox());
        existingClient.setAge(client.getAge());
        existingClient.setOccupation(client.getOccupation());
        existingClient.setImageUrls(client.getImageUrls());
        clientRepository.save(existingClient);
    }




}
