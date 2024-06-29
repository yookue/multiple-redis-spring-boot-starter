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

package com.yookue.springstarter.multipleredis.registrar;


import javax.annotation.Nonnull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.data.redis.config.annotation.SpringSessionRedisOperations;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;
import com.yookue.commonplexus.javaseutil.enumeration.NumberOrderType;
import com.yookue.commonplexus.springutil.util.BeanFactoryWraps;
import com.yookue.commonplexus.springutil.util.ReflectionUtilsWraps;
import com.yookue.springstarter.multipleredis.annotation.EnableMultipleRedisHttpSession;
import com.yookue.springstarter.multipleredis.config.PrimaryRedisAutoConfiguration;
import com.yookue.springstarter.multipleredis.config.SecondaryRedisAutoConfiguration;
import com.yookue.springstarter.multipleredis.config.TertiaryRedisAutoConfiguration;
import lombok.Getter;
import lombok.Setter;


/**
 * Configuration for {@link com.yookue.springstarter.multipleredis.annotation.EnableMultipleRedisHttpSession}
 *
 * @author David Hsing
 * @see org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration
 * @see com.yookue.springstarter.multipleredis.annotation.EnableMultipleRedisHttpSession
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.multiple-redis", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(value = {RedisOperations.class, SpringSessionRedisOperations.class})
@AutoConfigureAfter(value = {PrimaryRedisAutoConfiguration.class, SecondaryRedisAutoConfiguration.class, TertiaryRedisAutoConfiguration.class})
@AutoConfigureBefore(value = RedisHttpSessionConfiguration.class)
@SuppressWarnings("SpringFacetCodeInspection")
public class MultipleRedisHttpSessionRegistrar extends RedisHttpSessionConfiguration implements BeanFactoryAware {
    @Getter
    private NumberOrderType redisConnection;

    @Setter
    protected BeanFactory beanFactory;

    @Override
    public void setImportMetadata(@Nonnull AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableMultipleRedisHttpSession.class.getName()));
        if (ObjectUtils.isEmpty(attributes)) {
            return;
        }
        redisConnection = attributes.getEnum("redisConnection");    // $NON-NLS-1$
        super.setMaxInactiveIntervalInSeconds(attributes.getNumber("maxInactiveIntervalInSeconds"));    // $NON-NLS-1$
        String redisNamespace = attributes.getString("redisNamespace");    // $NON-NLS-1$
        if (StringUtils.hasText(redisNamespace)) {
            StringValueResolver resolver = ReflectionUtilsWraps.getFieldAs(RedisHttpSessionConfiguration.class, "embeddedValueResolver", true, this, StringValueResolver.class);    // $NON-NLS-1$
            if (resolver != null) {
                super.setRedisNamespace(resolver.resolveStringValue(redisNamespace));
            }
        }
        super.setFlushMode(attributes.getEnum("flushMode"));    // $NON-NLS-1$
        super.setSaveMode(attributes.getEnum("saveMode"));    // $NON-NLS-1$
        String cleanupCron = attributes.getString("cleanupCron");    // $NON-NLS-1$
        if (StringUtils.hasText(cleanupCron)) {
            super.setCleanupCron(cleanupCron);
        }
        if (redisConnection == null) {
            return;
        }
        RedisConnectionFactory factory = null;
        switch (redisConnection) {
            case PRIMARY:
                factory = BeanFactoryWraps.getBean(beanFactory, PrimaryRedisAutoConfiguration.CONNECTION_FACTORY, RedisConnectionFactory.class);
                break;
            case SECONDARY:
                factory = BeanFactoryWraps.getBean(beanFactory, SecondaryRedisAutoConfiguration.CONNECTION_FACTORY, RedisConnectionFactory.class);
                break;
            case TERTIARY:
                factory = BeanFactoryWraps.getBean(beanFactory, TertiaryRedisAutoConfiguration.CONNECTION_FACTORY, RedisConnectionFactory.class);
                break;
            default:
                break;
        }
        if (factory != null) {
            ReflectionUtilsWraps.setField(RedisHttpSessionConfiguration.class, "redisConnectionFactory", true, this, redisConnection);    // $NON-NLS-1$
        }
    }
}
