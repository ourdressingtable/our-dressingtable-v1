package com.ourdressingtable.auth.email.service;

public interface EmailService {
    void send(String to, String subject, String body);
}
