package com.heapster.dscatalog.repositories;

import com.heapster.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
