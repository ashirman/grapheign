package com.grapheign.examples.types;

import java.util.List;

public interface Character {
    String getId();
    String getName();
    List<Character> getFriends();
    List<Episode> getAppearsIn();
}
