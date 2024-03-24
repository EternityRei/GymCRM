package org.example.gymcrm.service;

public interface UserService {
    String createUsername(String firstname, String lastname);
    void updatePassword(String id, String newPassword);
    boolean modifyAccountStatus(String id);
    String generateRandomPassword();
}