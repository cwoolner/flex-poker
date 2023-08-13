package com.flexpoker.test.util

import com.redis.testcontainers.RedisContainer
import org.testcontainers.utility.DockerImageName

fun redisContainer(): RedisContainer {
    return RedisContainer(DockerImageName.parse("redis:7.0.11-alpine3.18"))
}
