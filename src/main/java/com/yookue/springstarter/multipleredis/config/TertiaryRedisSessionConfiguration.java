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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAllProperties;


/**
 * Tertiary configuration for redis session
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAllProperties(value = {
    @ConditionalOnProperty(prefix = "spring.multiple-redis", name = "enabled", havingValue = "true", matchIfMissing = true),
    @ConditionalOnProperty(prefix = "spring.session", name = "store-type", havingValue = "redis", matchIfMissing = true),
    @ConditionalOnProperty(prefix = TertiaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "session.enabled", havingValue = "true", matchIfMissing = true)
})
@ConditionalOnClass(value = {RedisOperations.class, RedisSessionRepository.class})
@ConditionalOnBean(name = TertiaryRedisAutoConfiguration.OBJECT_REDIS_TEMPLATE)
@AutoConfigureAfter(value = {SecondaryRedisSessionConfiguration.class, TertiaryRedisAutoConfiguration.class})
public class TertiaryRedisSessionConfiguration {
    public static final String SESSION_REPOSITORY = "tertiaryRedisIndexedSessionRepository";    // $NON-NLS-1$

    @Bean(name = SESSION_REPOSITORY)
    @ConditionalOnMissingBean(name = SESSION_REPOSITORY, type = "org.springframework.session.SessionRepository")
    public RedisIndexedSessionRepository sessionRepository(@Qualifier(value = TertiaryRedisAutoConfiguration.OBJECT_REDIS_TEMPLATE) @Nonnull RedisTemplate<Object, Object> template) {
        return new RedisIndexedSessionRepository(template);
    }
}