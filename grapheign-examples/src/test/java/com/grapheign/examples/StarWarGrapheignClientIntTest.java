package com.grapheign.examples;

import com.grapheign.examples.types.Character;
import com.grapheign.examples.types.Human;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8080"
})
public class StarWarGrapheignClientIntTest {

    @MockBean
    CharacterRepository repository;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getEpisodeHero() {
        String humanQuery =
                "{\"query\": " +
                "               \"{" +
                "                   human(id:\\\"2003\\\"){" +
                                        "name " +
                                        "appearsIn " +
                                        "homePlanet" +
                                     "}" +
                                "}" +
                            "\"}";

        HashMap<String, Character> repo = new HashMap<String, Character>() {{
            put("2003", new Human("2003", "simple Human Name", emptyList(), "Moon"));
        }};
        when(repository.getCharacters()).thenReturn(repo);

        assertThat(getData(humanQuery, "human")).contains(
                entry("name", "simple Human Name"),
                entry("appearsIn", emptyList()),
                entry("homePlanet", "Moon")
        );
    }

    private HashMap<String, Object> getData(String query, String type) {
        HttpEntity<String> requestEntity = new HttpEntity<>(query, new HttpHeaders() {{
            put("content-type", singletonList("application/json"));
        }});

        return restTemplate.exchange("/graphql", HttpMethod.POST, requestEntity, parameterizedTypeReference)
                .getBody()
                .get("data")
                .get(type);
    }

    // OMFG! sometymes java's strong typing is too strong :(
    private static final ParameterizedTypeReference<HashMap<String, HashMap<String, HashMap<String, Object>>>> parameterizedTypeReference = new ParameterizedTypeReference<HashMap<String, HashMap<String, HashMap<String, Object>>>>() {
    };
}
