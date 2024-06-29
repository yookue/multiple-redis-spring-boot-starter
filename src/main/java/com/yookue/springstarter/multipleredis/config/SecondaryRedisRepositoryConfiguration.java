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


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAllProperties;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAnnotation;


/**
 * Secondary configuration for redis repository
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAllProperties(value = {
    @ConditionalOnProperty(prefix = "spring.multiple-redis", name = "enabled", havingValue = "true", matchIfMissing = true),
    @ConditionalOnProperty(prefix = SecondaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "repository.enabled", havingValue = "true")
})
@ConditionalOnClass(value = RedisOperations.class)
@ConditionalOnBean(name = SecondaryRedisAutoConfiguration.CONNECTION_FACTORY)
@ConditionalOnAnnotation(includeFilter = Repository.class, basePackage = SecondaryRedisRepositoryConfiguration.REPOSITORY_PACKAGE)
@AutoConfigureAfter(value = {PrimaryRedisRepositoryConfiguration.class, SecondaryRedisAutoConfiguration.class})
@AutoConfigureBefore(value = {RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@EnableRedisRepositories(basePackages = SecondaryRedisRepositoryConfiguration.REPOSITORY_PACKAGE, redisTemplateRef = SecondaryRedisAutoConfiguration.OBJECT_REDIS_TEMPLATE)
public class SecondaryRedisRepositoryConfiguration {
    public static final String REPOSITORY_PACKAGE = "**.repository.secondary.redis";    // $NON-NLS-1$
}
