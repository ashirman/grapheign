package com.grapheign.examples;

import com.grapheign.examples.types.Character;
import com.grapheign.examples.types.CreateHumanInput;
import com.grapheign.examples.types.Droid;
import com.grapheign.examples.types.Episode;
import com.grapheign.examples.types.Human;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CharacterRepository {

    private SortedMap<String, Character> characters;
    private Map<Episode, Character> heroes;

    public CharacterRepository() {
        Human lukeSkywalker = new Human("1000", "Luke Skywalker", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Tatooine");
        Human darthVader = new Human("1001", "Darth Vader", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Tatooine");
        Human hanSolo = new Human("1002", "Han Solo", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), null);
        Human leiaOrgana = new Human("1003", "Leia Organa", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Alderaan");
        Human wilhuffTarkin = new Human("1004", "Wilhuff Tarkin", Collections.singletonList(Episode.NEWHOPE), null);

        Droid c3po = new Droid("2000", "C-3PO", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Protocol");
        Droid aretoo = new Droid("2001", "R2-D2", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Astromech");

/*
        lukeSkywalker.addFriends(hanSolo, leiaOrgana, c3po, aretoo);
        darthVader.addFriends(wilhuffTarkin);
        hanSolo.addFriends(lukeSkywalker, leiaOrgana, aretoo);
        leiaOrgana.addFriends(lukeSkywalker, hanSolo, c3po, aretoo);
        wilhuffTarkin.addFriends(darthVader);

        c3po.addFriends(lukeSkywalker, hanSolo, leiaOrgana, aretoo);
        aretoo.addFriends(lukeSkywalker, hanSolo, leiaOrgana);
*/

        this.characters = Collections.synchronizedSortedMap(Stream.of(lukeSkywalker, darthVader, hanSolo, leiaOrgana, wilhuffTarkin, c3po, aretoo)
                .collect(Collectors.toMap(Character::getId, Function.identity(), (c1, c2) -> c1, TreeMap::new)));

        Map<Episode, Character> heroes = new HashMap<>();
        heroes.put(Episode.NEWHOPE, lukeSkywalker);
        heroes.put(Episode.EMPIRE, aretoo);
        heroes.put(Episode.JEDI, darthVader);

        this.heroes = Collections.unmodifiableMap(heroes);
    }

    public Map<String, Character> getCharacters() {
        return characters;
    }

    public Human addHuman(CreateHumanInput humanInput) {
        synchronized (characters) {
            Long id = Long.valueOf(characters.lastKey()) + 1;
            Human human = new Human(id.toString(), humanInput.getName(), Collections.emptyList(), humanInput.getHomePlanet());
            characters.put(id.toString(), human);
            return human;
        }
    }

    public Map<Episode, Character> getHeroes() {
        return Collections.unmodifiableMap(heroes);
    }
}
