package com.heapster.msclientes.service;

import com.heapster.msclientes.dto.ClientDTO;
import com.heapster.msclientes.model.Client;
import com.heapster.msclientes.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    ClientRepository repository;

    public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
        Page<Client> clients = repository.findAll(pageRequest);
        return clients.map(c -> new ClientDTO(c));
    }
}
