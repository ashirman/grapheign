package com.grapheign.examples.feign;

import com.grapheign.core.annotations.GrapheignField;
import com.grapheign.core.annotations.GrapheignQuery;
import com.grapheign.examples.types.Character;
import com.grapheign.examples.types.Droid;
import com.grapheign.examples.types.Episode;
import com.grapheign.examples.types.Human;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "startwarclient", url = "${starwar.url}")
@GrapheignQuery
public interface StarWarFeignClient {
    @GrapheignField(name = "hero")
    @RequestMapping(value = "/hero", method = GET)
    Character hero(@RequestParam("episode") Episode episode);

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
