package de.pedramnazari.simpletbg.quest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestTest {

    @Test
    public void testQuestAndObjective() {
        final Quest quest = new Quest("Test Quest", "Test Description");
        assertEquals("Test Quest", quest.getName());
        assertEquals("Test Description", quest.getDescription());

        final QuestObjective objective1 = new QuestObjective("Defeat all enemies");
        assertEquals("Defeat all enemies", objective1.getDescription());
        assertFalse(objective1.isCompleted());
        assertFalse(objective1.isGoalDefeatAllEnemies());

        objective1.setGoalDefeatAllEnemies(true);

        quest.addObjective(objective1);
        assertEquals(1, quest.getObjectives().size());
        assertEquals(objective1, quest.getObjectives().get(0));

        assertFalse(quest.isCompleted());

        objective1.complete();

        assertTrue(objective1.isCompleted());
        assertTrue(quest.isCompleted());


    }

}
