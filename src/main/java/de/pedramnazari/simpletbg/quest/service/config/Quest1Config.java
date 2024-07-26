package de.pedramnazari.simpletbg.quest.service.config;

import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.service.AllEnemiesDefeatedQuestObjective;
import de.pedramnazari.simpletbg.quest.service.ItemPickUpQuestObjective;
import de.pedramnazari.simpletbg.quest.service.QuestService;
import de.pedramnazari.simpletbg.quest.service.event.AllEnemiesDefeatedQuestEvent;
import de.pedramnazari.simpletbg.quest.service.event.ItemPickUpQuestEvent;

public class Quest1Config extends AbstractQuestConfig {

    public static final String QUEST_ID = "quest1";

    public Quest1Config() {
        super(QUEST_ID);

        this.quest = new Quest("Defeat all enemies and collect black sword.", "You have to defeat all enemies and collect the black sword to win the game");
        final AllEnemiesDefeatedQuestObjective questObjective1 = new AllEnemiesDefeatedQuestObjective("Defeat all enemies");
        final ItemPickUpQuestObjective questObjective2 = new ItemPickUpQuestObjective("Collect black sword");
        quest.setHeroMustReachExit(false);

        quest.addObjective(questObjective1);
        quest.addObjective(questObjective2);

        this.questService = new QuestService(quest);
        questService.registerListener(AllEnemiesDefeatedQuestEvent.class, questObjective1);
        questService.registerListener(ItemPickUpQuestEvent.class, questObjective2);
    }
}
