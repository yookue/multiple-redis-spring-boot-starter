# Multiple Redis Spring Boot Starter

Spring Boot application integrates multiple `redis` quickly.

## Quickstart

- Import dependencies

```xml
    <dependency>
        <groupId>com.yookue.springstarter</groupId>
        <artifactId>multiple-redis-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> By default, this starter will auto take effect, you can turn it off by `spring.multiple-redis.enabled = false`

- Configure Spring Boot `application.yml` with prefix `spring.multiple-redis`

```yml
spring:
    multiple-redis:
        primary:
            host: '192.168.0.1'
            timeout: 10s
        secondary:
            host: '192.168.0.2'
            timeout: 10s
        tertiary:
            host: '192.168.0.3'
            timeout: 10s
```

> This starter supports 3 `RedisConnectionFactory` at most. (Three strikes and you're out)

- **Optional feature**: If you want to use redis repositories, locate your repositories under the following packages (take `primary` as an example)

    - Repositories: `**.repository.primary.redis`

- **Optional feature**: If you want to use redis session, replace the original `@EnableRedisHttpSession` annotation with `@EnableMultipleRedisHttpSession` annotation.

> Don't forget to specify the 'redisConnection' attribute.

- Configure your beans with the following beans by `@Autowired`/`@Resource` annotation, combined with `@Qualifier` annotation (take `primary` as an example)

| Bean Type                             | Qualifier                                                        |
|---------------------------------------|------------------------------------------------------------------|
| RedisConnectionFactory                | PrimaryRedisAutoConfiguration.CONNECTION_FACTORY                 |
| RedisTemplate<Object, Object>         | PrimaryRedisAutoConfiguration.OBJECT_REDIS_TEMPLATE              |
| RedisTemplate<String, Object>         | PrimaryRedisAutoConfiguration.JSON_REDIS_TEMPLATE                |
| StringRedisTemplate                   | PrimaryRedisAutoConfiguration.STRING_REDIS_TEMPLATE              |
| ReactiveRedisTemplate<Object, Object> | PrimaryRedisReactiveConfiguration.REACTIVE_REDIS_TEMPLATE        |
| ReactiveStringRedisTemplate           | PrimaryRedisReactiveConfiguration.REACTIVE_STRING_REDIS_TEMPLATE |
| CacheManager                          | PrimaryRedisCacheConfiguration.CACHE_MANAGER                     |
| CacheResolver                         | PrimaryRedisCacheConfiguration.CACHE_RESOLVER                    |
| RedisIndexedSessionRepository         | PrimaryRedisSessionConfiguration.SESSION_REPOSITORY              |

- This starter supports the most popular redis connection factories in the world, including
  - lettuce
  - jedis

## Document

- Github: https://github.com/yookue/multiple-redis-spring-boot-starter
- Redis homepage: https://redis.io

## Requirement

- jdk 1.8+

## License

This project is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
See the `NOTICE.txt` file for required notices and attributions.

## Donation

You like this package? Then [donate to Yookue](https://yookue.com/public/donate) to support the development.

## Website

- Yookue: https://yookue.com
