package com.stutya.lld.domain;

public class FareEstimateResponse {
    private final long estimatedFare;
    private final double distanceKm;
    private final long durationSec;
    private final String currency;

    public FareEstimateResponse(long estimatedFare, double distanceKm, long durationSec, String currency) {
        this.estimatedFare = estimatedFare;
        this.distanceKm = distanceKm;
        this.durationSec = durationSec;
        this.currency = currency;
    }

    public long getEstimatedFare() {
        return estimatedFare;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public long getDurationSec() {
        return durationSec;
    }

    public String getCurrency() {
        return currency;
    }
}