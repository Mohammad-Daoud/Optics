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
        return clientRepository.searchByNameOrPhoneOrId(query, pageable);
    }
}
