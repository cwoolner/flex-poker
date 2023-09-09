package com.flexpoker.test.util

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@Target(AnnotationTarget.CLASS)
@SpringBootTest
@ActiveProfiles(profiles = ["redis"])
@Testcontainers
@DirtiesContext
@Tag("repository-redis")
annotation class RedisTestClass

@Target(AnnotationTarget.CLASS)
@SpringBootTest
@ActiveProfiles(profiles = ["default"])
@DirtiesContext
@Tag("repository-inmemory")
annotation class InMemoryTestClass

@Target(AnnotationTarget.CLASS)
@Tag("archunit")
annotation class ArchUnitTestClass

@Target(AnnotationTarget.CLASS)
@Tag("unit")
annotation class UnitTestClass
