package com.project.optics.services;

import com.project.optics.exceptions.ClientNotFoundException;
import com.project.optics.models.Client;
import com.project.optics.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ClientImageService {

    private static final String UPLOAD_DIR = "upload/images";
    private final ClientRepository clientRepository;

    @Autowired
    public ClientImageService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public String saveClientImage(MultipartFile imageFile) throws IOException {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filepath = Paths.get(UPLOAD_DIR, filename);
        Files.copy(imageFile.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
        return "/images/" + filename;
    }

    public void deleteImage(Long clientId, String imageUrl) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        client.getImageUrls().remove(imageUrl);
        clientRepository.save(client);

        Path fileToDelete = Paths.get(UPLOAD_DIR, imageUrl.replace("/images/", ""));
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImages(Long clientId, List<MultipartFile> imageFiles) throws IOException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                String imageUrl = saveClientImage(imageFile);
                client.getImageUrls().add(imageUrl);
            }
        }

        clientRepository.save(client);
    }
}
