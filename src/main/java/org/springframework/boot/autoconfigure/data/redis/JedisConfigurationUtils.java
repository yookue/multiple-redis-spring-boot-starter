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
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import com.yookue.commonplexus.springutil.support.SingletonObjectProvider;


/**
 * Utilities for {@link org.springframework.data.redis.connection.jedis.JedisConnectionFactory}
 *
 * @author David Hsing
 * @see org.springframework.data.redis.connection.jedis.JedisConnectionFactory
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class JedisConfigurationUtils {
    public static JedisConnectionFactory redisConnectionFactory(@Nonnull RedisProperties properties, @Nullable RedisStandaloneConfiguration standalone, @Nullable RedisSentinelConfiguration sentinel, @Nullable RedisClusterConfiguration cluster, @Nullable JedisClientConfigurationBuilderCustomizer customizer) {
        JedisConnectionConfiguration configuration = new JedisConnectionConfiguration(properties, SingletonObjectProvider.ofNullable(standalone), SingletonObjectProvider.ofNullable(sentinel), SingletonObjectProvider.ofNullable(cluster));
        return configuration.redisConnectionFactory(SingletonObjectProvider.ofNullable(customizer));
    }
}
