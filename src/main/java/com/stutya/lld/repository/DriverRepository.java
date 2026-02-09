package com.stutya.lld.repository;

import com.stutya.lld.domain.Driver;
import com.stutya.lld.domain.DriverStatus;

import java.util.List;
import java.util.Optional;

public interface DriverRepository {
    Optional<Driver> findById(String id);

    void save(Driver driver);

    List<Driver> findByStatus(DriverStatus status);
}
