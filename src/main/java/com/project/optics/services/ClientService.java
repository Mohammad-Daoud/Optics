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
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    private final ClientRepository clientRepository;


    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Page<Client> searchClients(String query, Pageable pageable) {
        query = query.trim();
        return clientRepository.searchByNameOrPhoneOrId(query, pageable);
    }
    public Client addClient(Client client) {
        client.setDateOfCreation(LocalDate.now());
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
        existingClient.setImageUrls(client.getImageUrls());
        return clientRepository.save(existingClient);
    }

    public String saveClientImage(MultipartFile imageFile) throws IOException {
        // Logic to save the image and return the URL.
        // For simplicity, assuming you are saving it to a folder named 'images'
        String filename = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path filepath = Paths.get(UPLOAD_DIR, filename);
        Files.copy(imageFile.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
        return "/images/" + filename;  // Return the relative URL
    }


}
