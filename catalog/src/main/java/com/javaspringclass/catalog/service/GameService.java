package com.javaspringclass.catalog.service;

import com.javaspringclass.catalog.dto.GameMinDTO;
import com.javaspringclass.catalog.model.Game;
import com.javaspringclass.catalog.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public List<GameMinDTO> listarGames(){
        List<Game> games = gameRepository.findAll();
        return games.stream().map(GameMinDTO::new).collect(Collectors.toList());
    }

}
