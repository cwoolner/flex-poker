package com.flexpoker.archtest;

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Controller

class ControllerArchTests {

    private val cut = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages("com.flexpoker")

    @Test
    fun testAnnotations() {
        val classesHaveControllerAnnotation = classes().that()
            .haveNameMatching(".*Controller")
            .should().beAnnotatedWith(Controller::class.java)
        classesHaveControllerAnnotation.check(cut)
    }

    @Test
    fun testDependencies() {
        val dependencyRule = noClasses().that().resideInAPackage("com.flexpoker..")
            .should().dependOnClassesThat().resideInAPackage("com.flexpoker.web.controller")
        dependencyRule.check(cut)
    }

}
