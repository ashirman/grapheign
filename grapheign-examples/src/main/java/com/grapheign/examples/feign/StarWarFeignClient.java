package com.grapheign.examples.feign;

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
public interface StarWarFeignClient {
    @RequestMapping(value = "/hero", method = GET)
    Character hero(@RequestParam("episode") Episode episode);

    @RequestMapping(value = "/human/{id}", method = GET)
    Human human(@PathVariable("id") String id);

    @RequestMapping(value = "/droid/{id}", method = GET)
    Droid droid(@PathVariable("id") String id);

    @RequestMapping(value = "/character/{id}", method = GET)
    Character character(@PathVariable("id") String id);
}
