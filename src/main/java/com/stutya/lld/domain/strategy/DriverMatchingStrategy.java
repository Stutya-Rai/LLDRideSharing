package com.stutya.lld.domain.strategy;

import com.stutya.lld.domain.Driver;
import com.stutya.lld.domain.Location;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findMatchingDrivers(Location pickup, List<Driver> candidates, int maxResults);
}