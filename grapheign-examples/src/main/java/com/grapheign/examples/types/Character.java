package com.grapheign.examples.types;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

/*
known jackson limitation. need to explicitly define possible subtypes here to be able to construct abstract character from json string.
*/
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_type")
@JsonSubTypes({
        @Type(value = Droid.class, name = "droid"),
        @Type(value = Human.class, name = "human")})
public interface Character {
    String getId();

    String getName();

    List<Character> getFriends();

    List<Episode> getAppearsIn();
}
