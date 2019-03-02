package com.grapheign.examples.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Human implements Character {
    private String _type;
    private String id;
    private String name;
    private List<Character> friends = new ArrayList<>();
    private List<Episode> appearsIn;
    private String homePlanet;

    public Human(String id, String name, List<Episode> appearsIn, String homePlanet) {
        this.id = id;
        this.name = name;
        this.appearsIn = appearsIn;
        this.homePlanet = homePlanet;
        this._type = "human";
    }

    public void addFriends(Character ... friends) {
        this.friends.addAll(Arrays.asList(friends));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Character> getFriends() {
        return friends;
    }

    @Override
    public List<Episode> getAppearsIn() {
        return appearsIn;
    }

    public String getHomePlanet() {
        return homePlanet;
    }
}
