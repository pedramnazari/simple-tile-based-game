package de.pedramnazari.simpletbg.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
        packages = "de.pedramnazari.simpletbg",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class CleanArchitectureTest {


    @ArchIgnore
    @ArchTest
    static final ArchRule models_should_only_depend_on_other_models =
            classes().that().resideInAPackage("..model..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..model..", "java..");

    @ArchTest
    static final ArchRule services_should_only_depend_on_models_and_services =
            classes().that().resideInAPackage("..service..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..model..", "..service..", "java..");

    @ArchTest
    static final ArchRule interface_adapters_should_only_depend_on_models_and_services =
            classes().that().resideInAPackage("..adapters..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..model..", "..service..", "java..");


    @ArchTest
    static final ArchRule package_tilemap_should_not_depend_on_other_packages =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.tilemap..")
                    // TODO: remove access to GameContext (which is in de.pedramnazari.simpletbg.service)
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.tilemap..", "de.pedramnazari.simpletbg.service", "java..");

    @ArchTest
    static final ArchRule package_inventory_should_only_depend_on_tilemap_and_inventory =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.inventory..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.inventory..", "de.pedramnazari.simpletbg.tilemap..", "java..");

    @ArchTest
    static final ArchRule package_character_should_only_depend_on_tilemap_inventory_and_character =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.character..")
                    // TODO: remove access to GameContext (which is in de.pedramnazari.simpletbg.service)
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.character..", "de.pedramnazari.simpletbg.inventory..",
                            "de.pedramnazari.simpletbg.tilemap..", "de.pedramnazari.simpletbg.service", "java..");

    @ArchTest
    static final ArchRule package_enemy_should_only_depend_on_tilemap_inventory_character_and_enemy =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.character.enemy..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.character.enemy..", "de.pedramnazari.simpletbg.character.model",
                            "de.pedramnazari.simpletbg.character.service",
                            "de.pedramnazari.simpletbg.inventory..", "de.pedramnazari.simpletbg.tilemap..", "de.pedramnazari.simpletbg.service", "java..");


    @ArchTest
    static ArchRule rule =
            classes()
                    .that().haveSimpleNameEndingWith("Service")
                    .should().resideInAPackage("..service..");


}