package de.pedramnazari.simpletbg.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
        packages = "de.pedramnazari.simpletbg",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ArchitectureTest {

    @ArchTest
    static final ArchRule models_should_only_depend_on_other_models =
            classes().that().resideInAPackage("..model..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..model..", "java..");
}
