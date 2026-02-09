package com.stutya.lld.repository;

import com.stutya.lld.domain.Rider;

import java.util.Optional;

public interface RiderRepository {
    Optional<Rider> findById(String id);

    void save(Rider rider);
}
