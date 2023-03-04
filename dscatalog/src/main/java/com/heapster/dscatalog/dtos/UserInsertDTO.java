package com.heapster.dscatalog.dtos;

import com.heapster.dscatalog.entities.Role;
import com.heapster.dscatalog.entities.User;
import com.heapster.dscatalog.services.validations.UserInsertValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Senha deve ser preenchida")
    @Size(min=5, max=15, message = "Senha deve conter de 5 a 15 caracteres")
    private String password;

    public UserInsertDTO(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
