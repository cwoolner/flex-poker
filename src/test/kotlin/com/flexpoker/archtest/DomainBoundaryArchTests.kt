package com.flexpoker.archtest

import com.flexpoker.test.util.ArchUnitTestClass
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

@ArchUnitTestClass
class DomainBoundaryArchTests {

    @Test
    fun testDomainsDoNotRelyOnEachOther() {
        val chatRule = noClasses().that().resideInAPackage("..chat..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..login..", "..signup..", "..game..", "..table..")
        chatRule.check(classesUnderTest)

        val loginRule = noClasses().that().resideInAPackage("..login..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..chat..", "..signup..", "..game..", "..table..")
        loginRule.check(classesUnderTest)

        val signUpRule = noClasses().that().resideInAPackage("..signup..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..chat..", "..login..", "..game..", "..table..")
        signUpRule.check(classesUnderTest)

        val gameCommandRule = noClasses().that().resideInAPackage("..game.command..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..chat..", "..login..", "..signup..", "..table..")
        gameCommandRule.check(classesUnderTest)

        val tableCommandRule = noClasses().that().resideInAPackage("..table.command..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..chat..", "..login..", "..signup..", "..game..")
        tableCommandRule.check(classesUnderTest)
    }

    @Test
    fun testCommandSideIsInsulated() {
        val tableCommandRule = noClasses().that().resideInAPackage("..table.command..")
            .should().dependOnClassesThat().resideInAPackage("..table.query..")
        tableCommandRule.check(classesUnderTest)

        val gameCommandRule = noClasses().that().resideInAPackage("..game.command..")
            .should().dependOnClassesThat().resideInAPackage("..game.query..")
        gameCommandRule.check(classesUnderTest)
    }

}