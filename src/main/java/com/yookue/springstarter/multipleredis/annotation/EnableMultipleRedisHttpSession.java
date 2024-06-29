/*
 * Copyright (c) 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yookue.springstarter.multipleredis.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.FlushMode;
import org.springframework.session.MapSession;
import org.springframework.session.SaveMode;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import com.yookue.commonplexus.javaseutil.enumeration.NumberOrderType;
import com.yookue.springstarter.multipleredis.registrar.MultipleRedisHttpSessionRegistrar;


/**
 * Annotation for {@link org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession}
 *
 * @author David Hsing
 * @see org.springframework.session.config.annotation.web.http.EnableSpringHttpSession
 * @see org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
 * @see com.yookue.springstarter.multipleredis.registrar.MultipleRedisHttpSessionRegistrar
 * @reference "https://docs.spring.io/spring-session/reference/api.html"
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
@Configuration(proxyBeanMethods = false)
@Import(value = MultipleRedisHttpSessionRegistrar.class)
@SuppressWarnings({"unused", "JavadocDeclaration", "JavadocLinkAsPlainText"})
public @interface EnableMultipleRedisHttpSession {
    /**
     * Defines the redis connection order if multiple
     *
     * @return the redis connection order
     */
    NumberOrderType redisConnection() default NumberOrderType.PRIMARY;

    /**
     * Defines a unique namespace for keys
     * The value is used to isolate sessions by changing the prefix from default {@code spring:session:}
     *
     * @return the unique namespace for keys
     */
    String redisNamespace() default RedisIndexedSessionRepository.DEFAULT_NAMESPACE;

    /**
     * Flush mode for the Redis sessions
     * The default is {@code ON_SAVE} which only updates the backing Redis when {@link SessionRepository#save(Session)} is invoked
     * In a web environment this happens just before the HTTP response is committed
     *
     * @return the {@link FlushMode} to use
     */
    FlushMode flushMode() default FlushMode.ON_SAVE;

    /**
     * Save mode for the session
     *
     * @return the save mode
     */
    SaveMode saveMode() default SaveMode.ON_SET_ATTRIBUTE;

    /**
     * The session timeout in seconds
     * By default, it is set to 1800 seconds (30 minutes)
     * This should be a non-negative integer
     *
     * @return the seconds a session can be inactive before expiring
     */
    int maxInactiveIntervalInSeconds() default MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

    /**
     * The cron expression for expired session cleanup job
     * By default, this runs every minute
     *
     * @return the session cleanup cron expression
     */
    String cleanupCron() default "0 * * * * *";    // $NON-NLS-1$
}
