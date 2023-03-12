package com.heapster.dscatalog.services;

import com.heapster.dscatalog.dtos.*;
import com.heapster.dscatalog.entities.Category;
import com.heapster.dscatalog.entities.Product;
import com.heapster.dscatalog.entities.Role;
import com.heapster.dscatalog.entities.User;
import com.heapster.dscatalog.repositories.CategoryRepository;
import com.heapster.dscatalog.repositories.ProductRepository;
import com.heapster.dscatalog.repositories.RoleRepository;
import com.heapster.dscatalog.repositories.UserRepository;
import com.heapster.dscatalog.services.exceptions.DataBaseException;
import com.heapster.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        Page<User> users = repository.findAll(pageable);
        return users.map(u -> new UserDTO(u, u.getRoles()));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        Optional<User> obj = repository.findById(id);
        User user = obj.orElseThrow(() -> new ResourceNotFoundException("Product id not found " + id));
        return new UserDTO(user, user.getRoles());
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userDTO){
        User user = new User();
        copyDTOToEntity(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user = repository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userDTO){
        try {
            User user = repository.getReferenceById(id);
            copyDTOToEntity(userDTO, user);
            user = repository.save(user);
            return new UserDTO(user);
        } catch (EntityNotFoundException err){
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }

    public void delete(Long id){
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException err){
            throw new ResourceNotFoundException("Id not found" + id);
        } catch (DataIntegrityViolationException err) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDTOToEntity(UserDTO userDTO, User user){
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        user.getRoles().clear();
        for (RoleDTO roleDTO: userDTO.getRoles()){
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            user.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if (user == null){
            logger.error("Error: user not found: " + username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("User is logged");
        return user;
    }
}
