package com.grapheign.examples.restapi;

import com.grapheign.examples.CharacterRepository;
import com.grapheign.examples.types.Droid;
import com.grapheign.examples.types.Episode;
import com.grapheign.examples.types.Human;
import com.grapheign.examples.types.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StarWarController {
    private CharacterRepository characterRepository;

    @Autowired
    public StarWarController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @GetMapping("/hero")
    public Character hero(@RequestParam("episode") Episode episode) {
        return episode != null ? characterRepository.getHeroes().get(episode) : characterRepository.getCharacters().get("1000");
    }

    @GetMapping("/human/{id}")
    public Human human(@PathVariable("id") String id) {
        return (Human) characterRepository.getCharacters().values().stream()
                .filter(character -> character instanceof Human && character.getId().equals(id))
                .findFirst()
                .orElseGet(null);
    }

    @GetMapping("/droid/{id}")
    public Droid droid(@PathVariable("id") String id) {
        return (Droid) characterRepository.getCharacters().values().stream()
                .filter(character -> character instanceof Droid && character.getId().equals(id))
                .findFirst()
                .orElseGet(null);
    }

    @GetMapping("/character/{id}")
    public Character character(@PathVariable("id") String id) {
        return characterRepository.getCharacters().get(id);
    }
}