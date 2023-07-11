package com.javaspringclass.catalog.controller;

import com.javaspringclass.catalog.dto.GameMinDTO;
import com.javaspringclass.catalog.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<GameMinDTO> listarGames(){
        return gameService.listarGames();
    }
}
