package com.project.optics.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.project.optics.models.Client;
import com.project.optics.repositories.ClientRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";
    private final Path root = Paths.get(UPLOAD_DIR);

    private final ClientRepository clientRepository;


    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Page<Client> searchClients(String query, Pageable pageable) {
        return clientRepository.searchByNameOrPhoneOrId(query, pageable);
    }
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    public Client updateClient(Client client) {
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
        existingClient.setImageUrl(client.getImageUrl());
        return clientRepository.save(existingClient);
    }

    public String saveClientImage(MultipartFile imageFile) throws IOException {
        // Generate a random UUID for the image file name
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        // Ensure the upload directory exists
        Files.createDirectories(filePath.getParent());

        // Save the file locally
        Files.write(filePath, imageFile.getBytes());

        return fileName; // Return the relative path to the image
    }


}
