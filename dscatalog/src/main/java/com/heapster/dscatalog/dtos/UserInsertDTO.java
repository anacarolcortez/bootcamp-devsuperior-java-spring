package com.heapster.dscatalog.dtos;

import com.heapster.dscatalog.entities.Role;
import com.heapster.dscatalog.entities.User;

import java.util.Set;

public class UserInsertDTO extends UserDTO {

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
