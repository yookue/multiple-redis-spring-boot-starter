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

package com.yookue.springstarter.multipleredis.facade;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * Facade interface for customizing {@link org.springframework.data.redis.core.RedisTemplate}
 *
 * @author David Hsing
 * @see org.springframework.data.redis.core.RedisTemplate
 */
@FunctionalInterface
public interface RedisTemplateCustomizer {
    void customize(@Nonnull RedisTemplate<?, ?> template, @Nullable String... beanNames);
}
