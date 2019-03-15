# GraFeign
library that allows you to use Feign client as a Data Fetcher for graphql.

imagine you have Feign Clients as following

```java
@FeignClient(name = "startwarHumanClient", url = "${starwar.url}")
public interface StarWarFeignClient {
    @RequestMapping(value = "/createHuman", method = POST)
    Human createHuman(@RequestBody CreateHumanInput input);
}

@FeignClient(name = "startwarclient", url = "${starwar.url}")
public interface StarWarHumanClient {
    @RequestMapping(value = "/hero", method = GET)
    Character createHuman(@RequestParam("episode") Episode episode);

    @RequestMapping(value = "/human/{id}", method = GET)
    Human human(@PathVariable("id") String id);

    @RequestMapping(value = "/droid/{id}", method = GET)
    Droid droid(@PathVariable("id") String id);

    @RequestMapping(value = "/character/{id}", method = GET)
    Character character(@PathVariable("id") String id);
}

```
and wish to expose existing APIs as Graphql 

```graphql
type Query {
    # If episode omitted, returns the hero of the whole saga. If provided, returns the hero of that particular episode
    hero(episode: Episode): Character
    # Find human by id
    human(id: String!): Human
    # Find droid by id
    droid(id: String!): Droid
    # Find character by id
    character(id: String!): Character
}

type Mutation {
    # Creates a new human character
    createHuman(input: CreateHumanInput!): Human
}

# the rest part of schema definintion is here
```

easy! just add Grafeign annotations above feign interfaces and methods and these are exposured as Graphql DataFetchers:

```java
@FeignClient(name = "startwarHumanClient", url = "${starwar.url}")
@GrapheignMutation
public interface StarWarFeignClient {
    @GrapheignField(name = "createHuman")
    @RequestMapping(value = "/createHuman", method = POST)
    Human createHuman(@RequestBody CreateHumanInput input);
}

@FeignClient(name = "startwarclient", url = "${starwar.url}")
@GrapheignQuery
public interface StarWarHumanClient {
    @GrapheignField(name = "hero")
    @RequestMapping(value = "/hero", method = GET)
    Character createHuman(@RequestParam("episode") Episode episode);

    @GrapheignField(name = "human")
    @RequestMapping(value = "/human/{id}", method = GET)
    Human human(@PathVariable("id") String id);

    @GrapheignField(name = "droid")
    @RequestMapping(value = "/droid/{id}", method = GET)
    Droid droid(@PathVariable("id") String id);

    @GrapheignField(name = "character")
    @RequestMapping(value = "/character/{id}", method = GET)
    Character character(@PathVariable("id") String id);
}
``` 

then you can wire grafeign data fetches in one line

`` graphqlTypeFetcherSupplier.get().forEach((type, fieldDataFetchers) -> wiring.type(typeRuntimeWiring(type, fieldDataFetchers)));``