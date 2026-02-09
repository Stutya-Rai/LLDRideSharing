package com.stutya.lld.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockService {
    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public boolean acquire(String key, long timeoutMs) {
        ReentrantLock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        try {
            // TODO: Swap with distributed lock (Redis/Memcached) for multi-node deployment.
            return lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void release(String key) {
        ReentrantLock lock = lockMap.get(key);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
