package com.heapster.dscatalog.dtos;

import com.heapster.dscatalog.services.validations.UserInsertValid;
import com.heapster.dscatalog.services.validations.UserUpdateValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO {

    public UserUpdateDTO(){
        super();
    }

}
