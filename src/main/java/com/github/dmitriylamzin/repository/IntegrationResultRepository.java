package com.github.dmitriylamzin.repository;

import com.github.dmitriylamzin.domain.IntegrationResult;

public interface IntegrationResultRepository {
    boolean saveIntegrationResult(IntegrationResult integrationResult);
    IntegrationResult getIntegrationResult();
}
