package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.utils.exceptions.ResourceNotFoundException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public EventDTO update(Long id, EventDTO eventDTO){
        try {
            Event event = repository.getOne(id);
            copyDTOToEntity(eventDTO, event);
            event = repository.save(event);
            return new EventDTO(event);
        } catch (EntityNotFoundException err){
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }

    @Transactional
    private void copyDTOToEntity(EventDTO eventDTO, Event event){
        if (eventDTO.getName() != null){
            event.setName(eventDTO.getName());
        }

        if (eventDTO.getUrl() != null){
            event.setUrl(eventDTO.getUrl());
        }

        if (eventDTO.getDate() != null){
            event.setDate(eventDTO.getDate());
        }

        if (eventDTO.getCityId() != null){
            try{
                City city = cityRepository.getOne(eventDTO.getCityId());
                event.setCity(city);
            } catch (Exception err){
                throw new EntityNotFoundException("City id not found: " + eventDTO.getCityId());
            }
        }
;
    }
}
