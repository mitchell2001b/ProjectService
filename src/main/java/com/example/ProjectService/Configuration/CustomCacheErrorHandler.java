package com.example.ProjectService.Configuration;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        handleCacheError(exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        handleCacheError(exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        handleCacheError(exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        handleCacheError(exception);
    }

    private void handleCacheError(Exception exception) {
        // Log the exception or perform any other necessary actions
        if (isRedisConnectionIssue(exception)) {
            // Log the exception and handle it gracefully

        } else {
            throw new RuntimeException("Error in cache operation", exception);
        }
    }

    private boolean isRedisConnectionIssue(Exception ex) {
        // Check if the exception message or type indicates a connection issue with Redis
        return ex.getMessage() != null && ex.getMessage().contains("Connection refused");
    }
}