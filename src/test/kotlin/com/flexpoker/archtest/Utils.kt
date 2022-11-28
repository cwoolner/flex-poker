package com.flexpoker.archtest

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent

object Utils {

    val classesUnderTest = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages("com.flexpoker")

    fun nonInterfaceTopLevelClasses(regex: String): DescribedPredicate<JavaClass> {
        return object : DescribedPredicate<JavaClass>(regex) {
            override fun test(t: JavaClass?): Boolean {
                return if (t == null) false
                else (!t.isInterface
                        && t.isTopLevelClass
                        && t.name.contains(regex.toRegex()))
            }
        }
    }

    fun checkAnnotationContainsValue(annotationName: String, annotationValue: String): ArchCondition<JavaClass> {
        return object : ArchCondition<JavaClass>("$annotationName missing $annotationValue") {
            override fun check(item: JavaClass?, events: ConditionEvents?) {
                if (item != null && events != null) {
                    val profileAnnotationValues = item.annotations
                        .first { a -> (a.type as JavaClass).simpleName == annotationName }
                        .get("value").get() as Array<String>
                    if (!profileAnnotationValues.contains(annotationValue)) {
                        events.add(SimpleConditionEvent.violated(item, "${item.simpleName} violates rule"))
                    }
                }
            }
        }
    }

}
