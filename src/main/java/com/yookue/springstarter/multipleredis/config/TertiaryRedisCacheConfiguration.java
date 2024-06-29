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
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAllProperties;
import com.yookue.commonplexus.springutil.util.RedisConfigWraps;
import com.yookue.springstarter.cacheexpiry.resolver.impl.RedisExpiryCacheResolver;


/**
 * Tertiary configuration for redis cache
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAllProperties(value = {
    @ConditionalOnProperty(prefix = "spring.multiple-redis", name = "enabled", havingValue = "true", matchIfMissing = true),
    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis", matchIfMissing = true),
    @ConditionalOnProperty(prefix = TertiaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "cache-enabled", havingValue = "true", matchIfMissing = true)
})
@ConditionalOnBean(value = {CacheAspectSupport.class, CacheProperties.class})
@ConditionalOnClass(value = CacheManager.class)
@AutoConfigureAfter(value = {CacheAutoConfiguration.class, SecondaryRedisCacheConfiguration.class, TertiaryRedisAutoConfiguration.class})
@AutoConfigureBefore(name = "com.yookue.springstarter.cacheexpiry.config.RedisExpiryCacheConfiguration")
public class TertiaryRedisCacheConfiguration {
    public static final String CACHE_MANAGER = "tertiaryRedisCacheManager";    // $NON-NLS-1$
    public static final String CACHE_RESOLVER = "tertiaryRedisCacheResolver";    // $NON-NLS-1$

    @Bean(name = CACHE_MANAGER)
    @ConditionalOnBean(name = TertiaryRedisAutoConfiguration.CONNECTION_FACTORY)
    @ConditionalOnMissingBean(name = CACHE_MANAGER)
    public CacheManager cacheManager(@Qualifier(value = TertiaryRedisAutoConfiguration.CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull CacheProperties cacheProperties, @Nullable JacksonProperties jacksonProperties) {
        return RedisConfigWraps.cacheManager(factory, cacheProperties, jacksonProperties);
    }

    @Bean(name = CACHE_RESOLVER)
    @ConditionalOnClass(value = RedisExpiryCacheResolver.class)
    @ConditionalOnBean(name = CACHE_MANAGER)
    @ConditionalOnMissingBean(name = CACHE_RESOLVER)
    public CacheResolver cacheResolver(@Qualifier(value = CACHE_MANAGER) @Nonnull CacheManager cacheManager) {
        return new RedisExpiryCacheResolver(cacheManager);
    }
}
