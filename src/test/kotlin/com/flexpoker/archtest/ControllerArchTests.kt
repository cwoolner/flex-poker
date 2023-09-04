package com.flexpoker.archtest;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Controller

class ControllerArchTests {

    @Test
    fun testAnnotations() {
        val classesHaveControllerAnnotation = classes().that()
            .haveNameMatching(".*Controller")
            .should().beAnnotatedWith(Controller::class.java)
        classesHaveControllerAnnotation.check(classesUnderTest)
    }

    @Test
    fun testDependencies() {
        val dependencyRule = noClasses().that().resideInAPackage("com.flexpoker..")
            .should().dependOnClassesThat().resideInAPackage("com.flexpoker.web.controller")
        dependencyRule.check(classesUnderTest)
    }

}
