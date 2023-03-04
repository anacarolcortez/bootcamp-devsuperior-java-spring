package com.heapster.dscatalog.services.validations;

import com.heapster.dscatalog.controllers.exceptions.FieldError;
import com.heapster.dscatalog.dtos.UserInsertDTO;
import com.heapster.dscatalog.dtos.UserUpdateDTO;
import com.heapster.dscatalog.entities.User;
import com.heapster.dscatalog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        var uriVars = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldError> list = new ArrayList<>();

        User existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser != null && existingUser.getId() != userId){
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