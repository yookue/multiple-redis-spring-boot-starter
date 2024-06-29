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


import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.JedisConfigurationUtils;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.LettuceConfigurationUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.SimpleRedisConnectionConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAnyProperties;
import com.yookue.commonplexus.springutil.util.RedisConfigWraps;
import com.yookue.springstarter.multipleredis.facade.RedisTemplateCustomizer;
import com.yookue.springstarter.multipleredis.facade.StringRedisTemplateCustomizer;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;


/**
 * Tertiary configuration for classic redis
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.multiple-redis", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnAnyProperties(value = {
    @ConditionalOnProperty(prefix = TertiaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "url"),
    @ConditionalOnProperty(prefix = TertiaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "host")
})
@ConditionalOnClass(value = RedisOperations.class)
@AutoConfigureAfter(value = SecondaryRedisAutoConfiguration.class)
@AutoConfigureBefore(value = {RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@Import(value = {TertiaryRedisAutoConfiguration.Entry.class, TertiaryRedisAutoConfiguration.Lettuce.class, TertiaryRedisAutoConfiguration.Jedis.class, TertiaryRedisAutoConfiguration.Stage.class})
public class TertiaryRedisAutoConfiguration {
    public static final String PROPERTIES_PREFIX = "spring.multiple-redis.tertiary";    // $NON-NLS-1$
    public static final String REDIS_PROPERTIES = "tertiaryRedisProperties";    // $NON-NLS-1$
    public static final String STANDALONE_CONFIGURATION = "tertiaryRedisStandaloneConfiguration";    // $NON-NLS-1$
    public static final String SENTINEL_CONFIGURATION = "tertiaryRedisSentinelConfiguration";    // $NON-NLS-1$
    public static final String CLUSTER_CONFIGURATION = "tertiaryRedisClusterConfiguration";    // $NON-NLS-1$
    public static final String LETTUCE_CLIENT_CUSTOMIZER = "tertiaryLettuceClientCustomizer";    // $NON-NLS-1$
    public static final String LETTUCE_CLIENT_RESOURCES = "tertiaryLettuceClientResources";    // $NON-NLS-1$
    public static final String JEDIS_CLIENT_CUSTOMIZER = "tertiaryJedisClientCustomizer";    // $NON-NLS-1$
    public static final String CONNECTION_FACTORY = "tertiaryRedisConnectionFactory";    // $NON-NLS-1$
    public static final String OBJECT_REDIS_TEMPLATE = "tertiaryObjectRedisTemplate";    // $NON-NLS-1$
    public static final String JSON_REDIS_TEMPLATE = "tertiaryJsonRedisTemplate";    // $NON-NLS-1$
    public static final String STRING_REDIS_TEMPLATE = "tertiaryStringRedisTemplate";    // $NON-NLS-1$


    /**
     * Tertiary configuration for classic redis
     *
     * @author David Hsing
     */
    @Order(value = 0)
    static class Entry {
        @Bean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = REDIS_PROPERTIES)
        @ConfigurationProperties(prefix = PROPERTIES_PREFIX)
        public RedisProperties redisProperties() {
            return new RedisProperties();
        }

        @Bean(name = STANDALONE_CONFIGURATION)
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "standalone", matchIfMissing = true)
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = STANDALONE_CONFIGURATION)
        public RedisStandaloneConfiguration redisStandaloneConfiguration(@Qualifier(value = REDIS_PROPERTIES) @Nonnull RedisProperties properties) {
            return new SimpleRedisConnectionConfiguration(properties).getRedisStandaloneConfiguration();
        }

        @Bean(name = SENTINEL_CONFIGURATION)
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "sentinel")
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = SENTINEL_CONFIGURATION)
        public RedisSentinelConfiguration redisSentinelConfiguration(@Qualifier(value = REDIS_PROPERTIES) @Nonnull RedisProperties properties) {
            return new SimpleRedisConnectionConfiguration(properties).getRedisSentinelConfiguration();
        }

        @Bean(name = CLUSTER_CONFIGURATION)
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "cluster")
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = CLUSTER_CONFIGURATION)
        public RedisClusterConfiguration redisClusterConfiguration(@Qualifier(value = REDIS_PROPERTIES) @Nonnull RedisProperties properties) {
            return new SimpleRedisConnectionConfiguration(properties).getRedisClusterConfiguration();
        }
    }


    /**
     * Tertiary configuration for classic redis of lettuce
     *
     * @author David Hsing
     */
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "client-type", havingValue = "lettuce", matchIfMissing = true)
    @ConditionalOnClass(value = io.lettuce.core.RedisClient.class)
    @Order(value = 1)
    static class Lettuce {
        @Bean(name = LETTUCE_CLIENT_RESOURCES, destroyMethod = "shutdown")
        @ConditionalOnMissingBean(name = LETTUCE_CLIENT_RESOURCES, value = ClientResources.class)
        public DefaultClientResources lettuceClientResources() {
            return DefaultClientResources.create();
        }

        @Bean(name = CONNECTION_FACTORY)
        @ConditionalOnBean(name = {REDIS_PROPERTIES, LETTUCE_CLIENT_RESOURCES})
        @ConditionalOnMissingBean(name = CONNECTION_FACTORY, value = RedisConnectionFactory.class)
        public LettuceConnectionFactory redisConnectionFactory(@Qualifier(value = REDIS_PROPERTIES) @Nonnull RedisProperties properties,
            @Autowired(required = false) @Qualifier(value = STANDALONE_CONFIGURATION) @Nullable RedisStandaloneConfiguration standalone,
            @Autowired(required = false) @Qualifier(value = SENTINEL_CONFIGURATION) @Nullable RedisSentinelConfiguration sentinel,
            @Autowired(required = false) @Qualifier(value = CLUSTER_CONFIGURATION) @Nullable RedisClusterConfiguration cluster,
            @Autowired(required = false) @Qualifier(value = LETTUCE_CLIENT_CUSTOMIZER) @Nullable LettuceClientConfigurationBuilderCustomizer customizer,
            @Qualifier(value = LETTUCE_CLIENT_RESOURCES) @Nonnull ClientResources resources) {
            return LettuceConfigurationUtils.redisConnectionFactory(properties, standalone, sentinel, cluster, customizer, resources);
        }
    }


    /**
     * Tertiary configuration for classic redis of jedis
     *
     * @author David Hsing
     */
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "client-type", havingValue = "jedis", matchIfMissing = true)
    @ConditionalOnClass(value = {org.apache.commons.pool2.ObjectPool.class, redis.clients.jedis.Jedis.class})
    @Order(value = 2)
    static class Jedis {
        @Bean(name = CONNECTION_FACTORY)
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = CONNECTION_FACTORY, value = RedisConnectionFactory.class)
        public JedisConnectionFactory redisConnectionFactory(@Qualifier(value = REDIS_PROPERTIES) @Nonnull RedisProperties properties,
            @Autowired(required = false) @Qualifier(value = STANDALONE_CONFIGURATION) @Nullable RedisStandaloneConfiguration standalone,
            @Autowired(required = false) @Qualifier(value = SENTINEL_CONFIGURATION) @Nullable RedisSentinelConfiguration sentinel,
            @Autowired(required = false) @Qualifier(value = CLUSTER_CONFIGURATION) @Nullable RedisClusterConfiguration cluster,
            @Autowired(required = false) @Qualifier(value = JEDIS_CLIENT_CUSTOMIZER) @Nullable JedisClientConfigurationBuilderCustomizer customizer) {
            return JedisConfigurationUtils.redisConnectionFactory(properties, standalone, sentinel, cluster, customizer);
        }
    }


    /**
     * Tertiary configuration for classic redis of stage
     *
     * @author David Hsing
     */
    @Order(value = 3)
    static class Stage {
        @Bean(name = OBJECT_REDIS_TEMPLATE)
        @ConditionalOnBean(name = CONNECTION_FACTORY)
        @ConditionalOnMissingBean(name = OBJECT_REDIS_TEMPLATE)
        public RedisTemplate<Object, Object> objectRedisTemplate(@Qualifier(value = CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull ObjectProvider<RedisTemplateCustomizer> customizers) {
            RedisTemplate<Object, Object> template = new RedisAutoConfiguration().redisTemplate(factory);
            customizers.orderedStream().filter(Objects::nonNull).forEach(customizer -> customizer.customize(template, OBJECT_REDIS_TEMPLATE));
            template.afterPropertiesSet();
            return template;
        }

        @Bean(name = JSON_REDIS_TEMPLATE)
        @ConditionalOnClass(value = ObjectMapper.class)
        @ConditionalOnBean(name = CONNECTION_FACTORY)
        @ConditionalOnMissingBean(name = JSON_REDIS_TEMPLATE)
        @SuppressWarnings("ConstantConditions")
        public RedisTemplate<String, Object> jsonRedisTemplate(@Qualifier(value = CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull ObjectProvider<RedisTemplateCustomizer> customizers, @Nonnull ObjectProvider<JacksonProperties> properties) {
            RedisTemplate<String, Object> template = RedisConfigWraps.jacksonJsonSerializerTemplate(factory, properties.getIfAvailable());
            customizers.orderedStream().filter(Objects::nonNull).forEach(customizer -> customizer.customize(template, JSON_REDIS_TEMPLATE));
            template.afterPropertiesSet();
            return template;
        }

        @Bean(name = STRING_REDIS_TEMPLATE)
        @ConditionalOnBean(name = CONNECTION_FACTORY)
        @ConditionalOnMissingBean(name = STRING_REDIS_TEMPLATE)
        public StringRedisTemplate stringRedisTemplate(@Qualifier(value = CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull ObjectProvider<StringRedisTemplateCustomizer> customizers) {
            StringRedisTemplate template = new RedisAutoConfiguration().stringRedisTemplate(factory);
            customizers.orderedStream().filter(Objects::nonNull).forEach(customizer -> customizer.customize(template, STRING_REDIS_TEMPLATE));
            template.afterPropertiesSet();
            return template;
        }
    }
}