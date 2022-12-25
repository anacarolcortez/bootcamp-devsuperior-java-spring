package com.heapster.msclientes.repository;

import com.heapster.msclientes.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
