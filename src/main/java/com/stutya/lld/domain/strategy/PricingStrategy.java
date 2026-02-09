package com.stutya.lld.domain.strategy;

import com.stutya.lld.domain.Location;

public interface PricingStrategy {
    long calculateFare(Location pickup, Location dropoff, double distanceKm, long durationSec);
}
