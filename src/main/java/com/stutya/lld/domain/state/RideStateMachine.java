package com.stutya.lld.domain.state;

import com.stutya.lld.domain.Ride;
import com.stutya.lld.domain.RideStatus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class RideStateMachine {
    private final Map<RideStatus, Set<RideStatus>> allowedTransitions = new EnumMap<>(RideStatus.class);

    public RideStateMachine() {
        allowedTransitions.put(RideStatus.REQUESTED, EnumSet.of(RideStatus.ASSIGNED, RideStatus.CANCELLED));
        allowedTransitions.put(RideStatus.ASSIGNED, EnumSet.of(RideStatus.ACCEPTED, RideStatus.CANCELLED, RideStatus.REQUESTED));
        allowedTransitions.put(RideStatus.ACCEPTED, EnumSet.of(RideStatus.IN_PROGRESS, RideStatus.CANCELLED));
        allowedTransitions.put(RideStatus.IN_PROGRESS, EnumSet.of(RideStatus.COMPLETED, RideStatus.CANCELLED));
        allowedTransitions.put(RideStatus.COMPLETED, EnumSet.noneOf(RideStatus.class));
        allowedTransitions.put(RideStatus.CANCELLED, EnumSet.noneOf(RideStatus.class));
    }

    public void transition(Ride ride, RideStatus nextStatus) {
        RideStatus current = ride.getStatus();
        Set<RideStatus> allowed = allowedTransitions.getOrDefault(current, EnumSet.noneOf(RideStatus.class));
        if (!allowed.contains(nextStatus)) {
            throw new IllegalStateException("Invalid ride state transition from " + current + " to " + nextStatus);
        }
        ride.setStatus(nextStatus);
    }

    // TODO: Replace this simple validation with full state objects per STEP-4 once interview time allows.
}