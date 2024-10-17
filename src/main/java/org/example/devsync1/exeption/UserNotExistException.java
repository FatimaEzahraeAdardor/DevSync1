package org.example.devsync1.exeption;

public class UserNotExistException extends RuntimeException{
    public UserNotExistException(String message){
        super(message);
    }
}
