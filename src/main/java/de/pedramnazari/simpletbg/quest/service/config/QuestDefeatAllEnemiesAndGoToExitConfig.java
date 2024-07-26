package de.pedramnazari.simpletbg.quest.service.config;

import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.service.AllEnemiesDefeatedQuestObjective;
import de.pedramnazari.simpletbg.quest.service.QuestService;
import de.pedramnazari.simpletbg.quest.service.event.AllEnemiesDefeatedQuestEvent;

public class QuestDefeatAllEnemiesAndGoToExitConfig extends AbstractQuestConfig{

    public static final String QUEST_ID = "quest2";

    public QuestDefeatAllEnemiesAndGoToExitConfig() {
        super(QUEST_ID);

        this.quest = new Quest("Defeat all enemies and reach exit.", "You have to defeat all enemies and go to the exit to win the game");
        final AllEnemiesDefeatedQuestObjective questObjective1 = new AllEnemiesDefeatedQuestObjective("Defeat all enemies");
        quest.setHeroMustReachExit(true);

        quest.addObjective(questObjective1);

        this.questService = new QuestService(quest);
        questService.registerListener(AllEnemiesDefeatedQuestEvent.class, questObjective1);
    }
}
