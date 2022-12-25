package com.heapster.msclientes.controller;

import com.heapster.msclientes.dto.ClientDTO;
import com.heapster.msclientes.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    ClientService service;

    @GetMapping
    public ResponseEntity findAll(
            @RequestParam(value = "page", defaultValue="0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue="10") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue="ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue="name") String orderBy
    ){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<ClientDTO> clients = service.findAllPaged(pageRequest);
        return ResponseEntity.ok().body(clients);
    }
}
