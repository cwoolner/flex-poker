package com.flexpoker.archtest

import com.flexpoker.archtest.Utils.checkAnnotationContainsValue
import com.flexpoker.archtest.Utils.classesUnderTest
import com.flexpoker.archtest.Utils.nonInterfaceTopLevelClasses
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Repository

class RepositoryArchTests {

    private val redisRepositoryClasses = nonInterfaceTopLevelClasses(".*Redis.*Repository")

    private val inMemoryRepositoryClasses = nonInterfaceTopLevelClasses(".*InMemory.*Repository")

    @Test
    fun testAnnotations() {
        val classesHaveRepositoryAnnotation = classes().that()
            .areNotInterfaces().and()
            .haveNameMatching(".*Repository")
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

}
