package de.pedramnazari.simpletbg.quest.service.config;

public class DefaultQuestConfigFactory implements IQuestConfigFactory{
    @Override
    public IQuestConfig createQuestConfig(String questId) {
        IQuestConfig questConfig;

        switch (questId) {
            case Quest1Config.QUEST_ID:
                questConfig = new Quest1Config();
                break;
            default:
                throw new IllegalArgumentException("Invalid quest id: " + questId);
        }

        return questConfig;
    }
}
