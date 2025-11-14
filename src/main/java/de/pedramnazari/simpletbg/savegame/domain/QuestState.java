package de.pedramnazari.simpletbg.savegame.domain;

import java.util.List;

public record QuestState(String name, String description, boolean heroMustReachExit, List<QuestObjectiveState> objectives) {
}
