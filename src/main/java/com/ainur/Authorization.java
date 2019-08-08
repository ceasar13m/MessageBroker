package com.ainur;

public interface Authorization {
    public boolean addUser(User user);
    public boolean isLoginPasswordValid(String login, String password);
    public boolean isUserExists(String login, String password);
}
