package com.stutya.lld.repository.impl;

import com.stutya.lld.domain.Rider;
import com.stutya.lld.repository.RiderRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRiderRepository implements RiderRepository {
    private final Map<String, Rider> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Rider> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void save(Rider rider) {
        storage.put(rider.getId(), rider);
    }
}
