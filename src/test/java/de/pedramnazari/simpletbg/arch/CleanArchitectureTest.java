package de.pedramnazari.simpletbg.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

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

    @ArchIgnore
    @ArchTest
    static final ArchRule package_inventory_should_only_depend_on_tilemap_and_inventory =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.inventory..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.inventory..", "de.pedramnazari.simpletbg.tilemap..", "java..");

    @ArchTest
    static final ArchRule package_character_should_only_depend_on_tilemap_inventory_and_character =
            classes().that().resideInAnyPackage("de.pedramnazari.simpletbg.character.model", "de.pedramnazari.simpletbg.character.service")
                    // TODO: remove access to GameContext (which is in de.pedramnazari.simpletbg.service)
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.character.model",
                            "de.pedramnazari.simpletbg.character.service", "de.pedramnazari.simpletbg.inventory..",
                            "de.pedramnazari.simpletbg.tilemap..", "de.pedramnazari.simpletbg.service", "java..");

    @ArchTest
    static final ArchRule package_enemy_should_only_depend_on_tilemap_inventory_character_and_enemy =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.character.enemy..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.character.enemy..", "de.pedramnazari.simpletbg.character.model",
                            "de.pedramnazari.simpletbg.character.service",
                            // TODO: remove access to GameContext (which is in de.pedramnazari.simpletbg.service)
                            "de.pedramnazari.simpletbg.inventory..", "de.pedramnazari.simpletbg.tilemap..", "de.pedramnazari.simpletbg.service", "java..");

    @ArchTest
    static final ArchRule package_hero_should_only_depend_on_tilemap_inventory_character_and_hero =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.character.hero..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.character.hero..", "de.pedramnazari.simpletbg.character.model",
                            "de.pedramnazari.simpletbg.character.service",
                            // TODO: remove access to GameContext (which is in de.pedramnazari.simpletbg.service)
                            "de.pedramnazari.simpletbg.inventory..", "de.pedramnazari.simpletbg.tilemap..", "de.pedramnazari.simpletbg.service", "java..");

    @ArchTest
    static final ArchRule package_quest_should_only_depend_on_tilemap_and_quest =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.quest..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.quest..", "de.pedramnazari.simpletbg.character.enemy..",
                            "de.pedramnazari.simpletbg.character.model", "de.pedramnazari.simpletbg.character.service",
                            "de.pedramnazari.simpletbg.inventory..", "de.pedramnazari.simpletbg.tilemap..", "java..");

    @ArchTest
    static final ArchRule package_quest_service_event_should_not_depend_on_quest_config =
            noClasses().that().resideInAPackage("de.pedramnazari.simpletbg.quest.service.event..")
                    .should().dependOnClassesThat().resideInAPackage("de.pedramnazari.simpletbg.quest.service.config..");


    @ArchTest
    static final ArchRule package_game_should_only_depend_on_tilemap_inventory_character_quest_and_game =
            classes().that().resideInAPackage("de.pedramnazari.simpletbg.game..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("de.pedramnazari.simpletbg.game..", "de.pedramnazari.simpletbg.quest..",
                            // TODO: remove access to GameContext (which is in de.pedramnazari.simpletbg.service)
                            "de.pedramnazari.simpletbg.character..", "de.pedramnazari.simpletbg.inventory..", "de.pedramnazari.simpletbg.tilemap..",
                            "de.pedramnazari.simpletbg.service", "java..");


    // TODO: activate this test once GameContext is removed from the dependencies
    @ArchIgnore
    @ArchTest
    static final ArchRule no_cycles_in_package_dependencies =
            slices().matching("de.pedramnazari.simpletbg.(*)..").should().beFreeOfCycles();


}
