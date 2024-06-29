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

package com.yookue.springstarter.multipleredis.config;


import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;
import reactor.core.publisher.Flux;


/**
 * Primary configuration for reactive redis
 *
 * @author David Hsing
 * @see org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.multiple-redis", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(value = {RedisOperations.class, Flux.class})
@ConditionalOnBean(name = PrimaryRedisAutoConfiguration.CONNECTION_FACTORY, value = ReactiveRedisConnectionFactory.class)
@AutoConfigureAfter(value = PrimaryRedisAutoConfiguration.class)
@AutoConfigureBefore(value = {RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
public class PrimaryRedisReactiveConfiguration {
    public static final String DEFAULT_REACTIVE_REDIS_TEMPLATE = "reactiveRedisTemplate";    // $NON-NLS-1$
    public static final String DEFAULT_REACTIVE_STRING_REDIS_TEMPLATE = "reactiveStringRedisTemplate";    // $NON-NLS-1$
    public static final String REACTIVE_REDIS_TEMPLATE = "primaryReactiveRedisTemplate";    // $NON-NLS-1$
    public static final String REACTIVE_STRING_REDIS_TEMPLATE = "primaryReactiveStringRedisTemplate";    // $NON-NLS-1$

    @Primary
    @Bean(name = {REACTIVE_REDIS_TEMPLATE, DEFAULT_REACTIVE_REDIS_TEMPLATE})
    @ConditionalOnMissingBean(name = REACTIVE_REDIS_TEMPLATE)
    public ReactiveRedisTemplate<Object, Object> redisTemplate(@Qualifier(value = PrimaryRedisAutoConfiguration.CONNECTION_FACTORY) @Nonnull ReactiveRedisConnectionFactory factory, @Nonnull ResourceLoader loader) {
        return new RedisReactiveAutoConfiguration().reactiveRedisTemplate(factory, loader);
    }

    @Primary
    @Bean(name = {REACTIVE_STRING_REDIS_TEMPLATE, DEFAULT_REACTIVE_STRING_REDIS_TEMPLATE})
    @ConditionalOnMissingBean(name = REACTIVE_STRING_REDIS_TEMPLATE)
    public ReactiveStringRedisTemplate stringRedisTemplate(@Qualifier(value = PrimaryRedisAutoConfiguration.CONNECTION_FACTORY) @Nonnull ReactiveRedisConnectionFactory factory) {
        return new RedisReactiveAutoConfiguration().reactiveStringRedisTemplate(factory);
    }
}