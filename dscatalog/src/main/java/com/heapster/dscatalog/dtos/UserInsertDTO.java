package com.heapster.dscatalog.dtos;

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
