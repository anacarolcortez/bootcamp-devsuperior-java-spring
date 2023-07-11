package com.javaspringclass.catalog.repository;

import com.javaspringclass.catalog.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GameRepository extends JpaRepository<Game, Long> {
}
