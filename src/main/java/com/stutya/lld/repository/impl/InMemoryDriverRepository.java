package com.stutya.lld.repository.impl;

import com.stutya.lld.domain.Driver;
import com.stutya.lld.domain.DriverStatus;
import com.stutya.lld.repository.DriverRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryDriverRepository implements DriverRepository {
    private final Map<String, Driver> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Driver> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void save(Driver driver) {
        storage.put(driver.getId(), driver);
    }

    @Override
    public List<Driver> findByStatus(DriverStatus status) {
        return storage.values().stream()
                .filter(driver -> driver.getStatus() == status)
                .collect(Collectors.toList());
    }
}
