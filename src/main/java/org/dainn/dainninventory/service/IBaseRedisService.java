package org.dainn.dainninventory.service;

import com.fasterxml.jackson.core.type.TypeReference;

public interface IBaseRedisService {
    void set(String key, String value);
    void setTimeToValue(String key, long timeoutInDays);
    Object get(String key);
    void flushDb();
    void setCache(String key, Object value);
    <T> T getCache(String key, TypeReference<T> type);

}
