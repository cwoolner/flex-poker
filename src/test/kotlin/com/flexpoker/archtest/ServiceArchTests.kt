package com.flexpoker.archtest

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Service

class ServiceArchTests {

    @Test
    fun testAnnotations() {
        val classesHaveServiceAnnotation = classes().that()
            .areNotInterfaces().and()
            .haveNameMatching(".*Service")
            .should().beAnnotatedWith(Service::class.java)
        classesHaveServiceAnnotation.check(classesUnderTest)
    }

    @Test
    fun testDependencies() {
        val dependencyRule = classes().that().resideInAPackage("..command.service..")
            .should().onlyBeAccessed().byAnyPackage("..command.service..", "..command.handlers..")
        dependencyRule.check(classesUnderTest)
    }

}