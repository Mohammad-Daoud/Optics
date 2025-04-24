package com.project.optics.controllers;

import com.project.optics.models.Client;

import com.project.optics.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public String searchClients(@RequestParam("query") String query,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                Model model) {
        Page<Client> clientsPage = searchService.searchClients(query, PageRequest.of(page, size));
        model.addAttribute("clientsPage", clientsPage);
        model.addAttribute("needToShow",true);
        model.addAttribute("query", query);
        return "clients";
    }

    @GetMapping("/api/search")
    @ResponseBody
    public List<Client> searchClients(@RequestParam("query") String query) {
        return searchService.searchClients(query, null).getContent();
    }

}
