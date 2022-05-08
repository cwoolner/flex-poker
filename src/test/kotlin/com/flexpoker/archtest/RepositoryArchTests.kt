package com.flexpoker.archtest

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Repository

class RepositoryArchTests {

    private val cut = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages("com.flexpoker")

    @Test
    fun testAnnotations() {
        val classesHaveRepositoryAnnotation = classes().that()
            .areNotInterfaces().and()
            .haveNameMatching(".*Repository")
            .should().beAnnotatedWith(Repository::class.java)
        classesHaveRepositoryAnnotation.check(cut)
    }

    @Test
    fun testRedisRepositoriesDependOnRedisTemplate() {
        val redisReposDependOnRedisTemplate = classes().that()
            .areNotInterfaces().and()
            .haveNameMatching(".*Redis.*Repository")
            .should().dependOnClassesThat().haveSimpleName("RedisTemplate")
        redisReposDependOnRedisTemplate.check(cut)
    }

    @Test
    fun testRedisRepositoriesUseCorrectProfileAnnotation() {
        val redisRepos = cut
            .filter { !it.isInterface }
            .filter { it.isTopLevelClass }
            .filter { it.name.contains(".*Redis.*Repository".toRegex()) }
        assertTrue(redisRepos.isNotEmpty())

        redisRepos.forEach {
            val profileAnnotationValues = it.annotations
                .first { a -> (a.type as JavaClass).simpleName == "Profile" }
                .get("value").get() as Array<String>
            assertTrue(profileAnnotationValues.contains("redis"))
        }
    }

    @Test
    fun testInMemoryRepositoriesUseCorrectProfileAnnotation() {
        val inMemoryRepos = cut
            .filter { !it.isInterface }
            .filter { it.isTopLevelClass }
            .filter { it.name.contains(".*InMemory.*Repository".toRegex()) }
        assertTrue(inMemoryRepos.isNotEmpty())

        inMemoryRepos.forEach {
            val profileAnnotationValues = it.annotations
                .first { a -> (a.type as JavaClass).simpleName == "Profile" }
                .get("value").get() as Array<String>
            assertTrue(profileAnnotationValues.contains("default"))
        }
    }

}
