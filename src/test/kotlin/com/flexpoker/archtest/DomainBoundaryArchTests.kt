package com.flexpoker.archtest

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class DomainBoundaryArchTests {

    private val cut = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages("com.flexpoker")

    @Test
    fun testDomainsDoNotRelyOnEachOther() {
        val chatRule = noClasses().that().resideInAPackage("..chat..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..login..", "..signup..", "..game..", "..table..")
        chatRule.check(cut)

        val loginRule = noClasses().that().resideInAPackage("..login..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..chat..", "..signup..", "..game..", "..table..")
        loginRule.check(cut)

        val signUpRule = noClasses().that().resideInAPackage("..signup..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..chat..", "..login..", "..game..", "..table..")
        signUpRule.check(cut)

        val gameCommandRule = noClasses().that().resideInAPackage("..game.command..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..chat..", "..login..", "..signup..", "..table..")
        gameCommandRule.check(cut)

        val tableCommandRule = noClasses().that().resideInAPackage("..table.command..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..chat..", "..login..", "..signup..", "..game..")
        tableCommandRule.check(cut)
    }

    @Test
    fun testCommandSideIsInsulated() {
        val tableCommandRule = noClasses().that().resideInAPackage("..table.command..")
            .should().dependOnClassesThat().resideInAPackage("..table.query..")
        tableCommandRule.check(cut)

        val gameCommandRule = noClasses().that().resideInAPackage("..game.command..")
            .should().dependOnClassesThat().resideInAPackage("..game.query..")
        gameCommandRule.check(cut)
    }

}