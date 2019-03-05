package com.grapheign.examples.feign;

import com.grapheign.core.annotations.GrapheignField;
import com.grapheign.core.annotations.GrapheignMutation;
import com.grapheign.examples.types.CreateHumanInput;
import com.grapheign.examples.types.Human;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "startwarHumanClient", url = "${starwar.url}")
@GrapheignMutation
public interface StarWarFeignClient {
    @GrapheignField(name = "createHuman")
    @RequestMapping(value = "/createHuman", method = POST)
    Human createHuman(@RequestBody CreateHumanInput input);
}