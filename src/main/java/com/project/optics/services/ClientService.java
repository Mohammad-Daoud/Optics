package com.project.optics.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.project.optics.models.Client;
import com.project.optics.repositories.ClientRepository;

import java.time.LocalDate;
import java.util.List;

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

    public boolean clientExists(String firstName, String secondName, String thirdName, String lastName) {
        List<Client> similarClients = clientRepository.findSimilarClients(
                firstName.trim(),
                secondName != null ? secondName.trim() : null,
                thirdName != null ? thirdName.trim() : null,
                lastName.trim());

        return !similarClients.isEmpty();
    }

    public void addClient(Client client) {
        removeSpacesFromClient(client);
        populateSearchName(client);
        client.setDateOfCreation(LocalDate.now());
        clientRepository.save(client);
    }

    private void removeSpacesFromClient(Client client) {
        client.setFirstName(client.getFirstName() != null ? client.getFirstName().replaceAll("\\s+", " ").trim() : null);
        client.setSecondName(client.getSecondName() != null ? client.getSecondName().replaceAll("\\s+", " ").trim() : null);
        client.setThirdName(client.getThirdName() != null ? client.getThirdName().replaceAll("\\s+", " ").trim() : null);
        client.setLastName(client.getLastName() != null ? client.getLastName().replaceAll("\\s+", " ").trim() : null);
    }
    private void populateSearchName(Client client) {
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

        client.setSearchName(searchNameBuilder.toString().replaceAll("\\s+", " ").replaceAll("\\s+", " ").toLowerCase());
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

        removeSpacesFromClient(client);
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
        populateSearchName(existingClient);
        clientRepository.save(existingClient);
    }


    public String nameContainsSpaces(String... names) {
      /*  for (String name : names) {
            if (name != null && name.contains(" ")) {
                return name;
            }
        }*/
        return null;
    }
}
