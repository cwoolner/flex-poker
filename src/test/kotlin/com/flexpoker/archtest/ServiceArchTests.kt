package com.flexpoker.archtest

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Service

class ServiceArchTests {

    private val serviceClasses = nonInterfaceTopLevelClasses(".*Service")

    @Test
    fun testAnnotations() {
        val classesHaveServiceAnnotation = classes().that(serviceClasses)
            .should().beAnnotatedWith(Service::class.java)
        classesHaveServiceAnnotation.check(classesUnderTest)
    }

    @Test
    fun testDependencies() {
        val dependencyRule = classes().that().resideInAPackage("..command.service..")
            .should().onlyBeAccessed().byAnyPackage("..command.service..", "..command.handlers..")
        dependencyRule.check(classesUnderTest)
    }

    @Test
    fun testNoFieldInjection() {
        NO_CLASSES_SHOULD_USE_FIELD_INJECTION.check(classesUnderTest.that(serviceClasses))
    }

}