package com.heapster.dscatalog.services.validations;

import com.heapster.dscatalog.controllers.exceptions.FieldError;
import com.heapster.dscatalog.dtos.UserInsertDTO;
import com.heapster.dscatalog.entities.User;
import com.heapster.dscatalog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldError> list = new ArrayList<>();

        User existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser != null){
            list.add(new FieldError ("email", "E-mail já cadastrado. Informe outro e-mail"));
        }


        //Insere tipos selecionados de erros dentro da lista do Bean Validation
        for (FieldError e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}