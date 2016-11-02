package com.github.dmitriylamzin.service;

import java.util.List;

public interface IntegrationService {
    List<String> integrate();
    String save(String... args);
}
