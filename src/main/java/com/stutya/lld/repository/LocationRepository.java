package com.stutya.lld.repository;

import com.stutya.lld.domain.Location;

public interface LocationRepository {
    void saveLocation(String driverId, Location location);

    Location getLatestLocation(String driverId);
}