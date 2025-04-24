package com.project.optics.services;

import com.project.optics.models.Client;
import com.project.optics.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final ClientRepository clientRepository;

    @Autowired
    public SearchService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> searchClients(String query, Pageable pageable) {
        query = query.trim();

        if (isNumeric(query)) {
            // Check if the query is numeric, and based on the length, determine if it's an ID or phone number
            Page<Client> phoneNumberResult = clientRepository.searchByPhoneNumber(query, pageable);
            if (phoneNumberResult.isEmpty()) {
                return clientRepository.searchById(Long.valueOf(query), pageable);
            }
            return phoneNumberResult;
        } else {
            // Split the input into words (names)
            query = query.replaceAll("\\s+", " ");
            // Search based on how many names are provided
            return clientRepository.searchBySearchName(query, pageable);
            /* switch (nameParts.length) {
                case 2:
                    return clientRepository.searchByFirstAndLastName(nameParts[0], nameParts[1], pageable);
                case 3:
                    return clientRepository.searchByFirstSecondAndLastName(nameParts[0], nameParts[1], nameParts[2], pageable);
                case 4:
                    return clientRepository.searchByFullName(nameParts[0], nameParts[1], nameParts[2], nameParts[3], pageable);
                default:
                    return clientRepository.defaultSearch(query,pageable); // Default case if query doesn't match criteria
            }*/
        }
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+"); // Matches any string of digits
    }
}
