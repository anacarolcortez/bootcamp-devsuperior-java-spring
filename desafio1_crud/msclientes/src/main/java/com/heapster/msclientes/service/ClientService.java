package com.heapster.msclientes.service;

import com.heapster.msclientes.dto.ClientDTO;
import com.heapster.msclientes.model.Client;
import com.heapster.msclientes.repository.ClientRepository;
import com.heapster.msclientes.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
        Page<Client> clients = repository.findAll(pageRequest);
        return clients.map(c -> new ClientDTO(c));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Optional<Client> client = repository.findById(id);
        Client entity = client.orElseThrow(() -> new ResourceNotFoundException("Client id not found"));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO create(ClientDTO clientDTO){
        Client client = new Client(clientDTO);
        client = repository.save(client);
        return new ClientDTO(client);
    }

    public ClientDTO update(Long id, ClientDTO clientDTO){
        try{
            Client client = repository.getReferenceById(id);
            client = client.update(clientDTO);
            client = repository.save(client);
            return new ClientDTO(client);
        } catch (EntityNotFoundException err){
            throw new ResourceNotFoundException("Client id not found" + id);
        }
    }
}
