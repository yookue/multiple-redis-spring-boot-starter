/*
 * Copyright (c) 2020 Yookue Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.cache;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import com.yookue.commonplexus.springutil.support.SingletonObjectProvider;


/**
 * Utilities for {@link org.springframework.data.redis.cache.RedisCacheManager}
 *
 * @author David Hsing
 * @see org.springframework.data.redis.cache.RedisCacheManager
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class RedisCacheManagerUtils {
    public static RedisCacheManager cacheManager(@Nonnull CacheProperties properties, @Nonnull CacheManagerCustomizers customizers, @Nullable RedisCacheConfiguration configuration,
            @Nullable RedisCacheManagerBuilderCustomizer customizer, @Nonnull RedisConnectionFactory factory, @Nonnull ResourceLoader loader) {
        org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration cacheConfiguration = new org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration();
        return cacheConfiguration.cacheManager(properties, customizers, SingletonObjectProvider.ofNullable(configuration), SingletonObjectProvider.ofNullable(customizer), factory, loader);
    }
}
