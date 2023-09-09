package com.flexpoker.archtest

import com.flexpoker.test.util.ArchUnitTestClass
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Repository

@ArchUnitTestClass
class RepositoryArchTests {

    private val redisRepositoryClasses = nonInterfaceTopLevelClasses(".*Redis.*Repository")

    private val inMemoryRepositoryClasses = nonInterfaceTopLevelClasses(".*InMemory.*Repository")

    @Test
    fun testAnnotations() {
        val classesHaveRepositoryAnnotation = classes()
            .that(redisRepositoryClasses).or(inMemoryRepositoryClasses)
            .should().beAnnotatedWith(Repository::class.java)
        classesHaveRepositoryAnnotation.check(classesUnderTest)
    }

    @Test
    fun testRedisRepositoriesDependOnRedisTemplate() {
        val redisReposDependOnRedisTemplate = classes().that(redisRepositoryClasses)
            .should().dependOnClassesThat().haveSimpleName("RedisTemplate")
        redisReposDependOnRedisTemplate.check(classesUnderTest)
    }

    @Test
    fun testRedisRepositoriesUseCorrectProfileAnnotation() {
        val redisAnnotationRule = classes().that(redisRepositoryClasses)
            .should(checkAnnotationContainsValue("Profile", "redis"))
        redisAnnotationRule.check(classesUnderTest)
    }

    @Test
    fun testInMemoryRepositoriesUseCorrectProfileAnnotation() {
        val inMemoryAnnotationRule = classes().that(inMemoryRepositoryClasses)
            .should(checkAnnotationContainsValue("Profile", "default"))
        inMemoryAnnotationRule.check(classesUnderTest)
    }

    @Test
    fun testNoFieldInjection() {
        NO_CLASSES_SHOULD_USE_FIELD_INJECTION.check(classesUnderTest.that(inMemoryRepositoryClasses))
        NO_CLASSES_SHOULD_USE_FIELD_INJECTION.check(classesUnderTest.that(redisRepositoryClasses))
    }

}
