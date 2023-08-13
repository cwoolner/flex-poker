package com.flexpoker.test.util

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@Target(AnnotationTarget.CLASS)
@SpringBootTest
@ActiveProfiles(profiles = ["redis"])
@Testcontainers
@DirtiesContext
annotation class RedisTestClass

@Target(AnnotationTarget.CLASS)
@SpringBootTest
@ActiveProfiles(profiles = ["default"])
@DirtiesContext
annotation class InMemoryTestClass
