package com.devsuperior.bds02.utils.exceptions;

public class DatabaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String msg){
        super(msg);
    }

}
