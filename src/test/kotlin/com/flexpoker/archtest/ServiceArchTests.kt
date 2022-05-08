package com.flexpoker.archtest

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Service

class ServiceArchTests {

    private val cut = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages("com.flexpoker")

    @Test
    fun testAnnotations() {
        val classesHaveServiceAnnotation = classes().that()
            .areNotInterfaces().and()
            .haveNameMatching(".*Service")
            .should().beAnnotatedWith(Service::class.java)
        classesHaveServiceAnnotation.check(cut)
    }

    @Test
    fun testDependencies() {
        val dependencyRule = classes().that().resideInAPackage("..command.service..")
            .should().onlyBeAccessed().byAnyPackage("..command.service..", "..command.handlers..")
        dependencyRule.check(cut)
    }

}