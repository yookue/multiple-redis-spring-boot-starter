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

package org.springframework.boot.autoconfigure.data.redis;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import com.yookue.commonplexus.springutil.support.SingletonObjectProvider;


/**
 * Utilities for {@link org.springframework.data.redis.connection.RedisStandaloneConfiguration} and {@link org.springframework.data.redis.connection.RedisSentinelConfiguration}
 *
 * @author David Hsing
 * @see org.springframework.data.redis.connection.RedisStandaloneConfiguration
 * @see org.springframework.data.redis.connection.RedisSentinelConfiguration
 */
@SuppressWarnings("unused")
public class SimpleRedisConnectionConfiguration extends RedisConnectionConfiguration {
    public SimpleRedisConnectionConfiguration(@Nonnull RedisProperties properties) {
        super(properties, SingletonObjectProvider.empty(), SingletonObjectProvider.empty(), SingletonObjectProvider.empty());
    }

    public SimpleRedisConnectionConfiguration(@Nonnull RedisProperties properties, @Nullable RedisStandaloneConfiguration standalone, @Nullable RedisSentinelConfiguration sentinel, @Nullable RedisClusterConfiguration cluster) {
        super(properties, SingletonObjectProvider.ofNullable(standalone), SingletonObjectProvider.ofNullable(sentinel), SingletonObjectProvider.ofNullable(cluster));
    }

    @Nonnull
    public RedisProperties getRedisProperties() {
        return super.getProperties();
    }

    @Nonnull
    public RedisStandaloneConfiguration getRedisStandaloneConfiguration() {
        return super.getStandaloneConfig();
    }

    @Nullable
    public RedisSentinelConfiguration getRedisSentinelConfiguration() {
        return super.getSentinelConfig();
    }

    @Nullable
    public RedisClusterConfiguration getRedisClusterConfiguration() {
        return super.getClusterConfiguration();
    }
}
