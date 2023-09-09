package com.flexpoker.archtest;

import com.flexpoker.test.util.ArchUnitTestClass
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Controller

@ArchUnitTestClass
class ControllerArchTests {

    private val controllerClasses = nonInterfaceTopLevelClasses(".*Controller")

    @Test
    fun testAnnotations() {
        val classesHaveControllerAnnotation = classes().that(controllerClasses)
            .should().beAnnotatedWith(Controller::class.java)
        classesHaveControllerAnnotation.check(classesUnderTest)
    }

    @Test
    fun testDependencies() {
        val dependencyRule = noClasses().that().resideInAPackage("com.flexpoker..")
            .should().dependOnClassesThat().resideInAPackage("com.flexpoker.web.controller")
        dependencyRule.check(classesUnderTest)
    }

    @Test
    fun testNoFieldInjection() {
        NO_CLASSES_SHOULD_USE_FIELD_INJECTION.check(classesUnderTest.that(controllerClasses))
    }

}
