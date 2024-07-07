package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.utils.PageWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class BaseRedisService implements IBaseRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToValue(String key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void flushDb() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection().serverCommands()
                .flushDb();
    }

    @Override
    public void setCache(String key, Object value) {
        try {
            if (value instanceof Page<?> page) {
                PageWrapper<Object> pageWrapper = (PageWrapper<Object>) new PageWrapper<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
                set(key, objectMapper.writeValueAsString(pageWrapper));
                return;
            }
            set(key, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            log.info("Error: {}", e.getMessage());
        }
    }

    @Override
    public <T> T getCache(String key, TypeReference<T> type) {
        Object cache = get(key);
        if (cache != null) {
            try {
                if (type.getType() instanceof ParameterizedType parameterizedType) {
                    Class<?> rawType = (Class<?>) parameterizedType.getRawType();
                    if (Page.class.isAssignableFrom(rawType)) {
                        PageWrapper<?> pageWrapper = objectMapper.readValue(cache.toString(), new TypeReference<PageWrapper<?>>() {
                        });
                        return (T) new PageImpl<>(pageWrapper.getList(), PageRequest.of(pageWrapper.getPageNumber(), pageWrapper.getPageSize()), pageWrapper.getTotalElements());
                    }
                }
                return objectMapper.readValue(cache.toString(), type);
            } catch (JsonProcessingException e) {
                log.info("Error: {}", e.getMessage());
            }
        }
        return null;
    }
}
