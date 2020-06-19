package com.thehecklers.planefinder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PlaneController {
    private final WebClient client;

    public PlaneController(WebClient client) {
        this.client = client;
    }

    @GetMapping
    List<Aircraft> getNodes() throws IOException {
        List<Aircraft> aircraftList = new ArrayList<>();

        JsonNode aircraftNodes = new ObjectMapper().readTree(new URL("http://192.168.1.193/ajax/aircraft"))
                .get("aircraft");

        aircraftNodes.iterator()
                .forEachRemaining(node -> addAircraft(aircraftList, node));

        return aircraftList;
    }

    private void addAircraft(List<Aircraft> aircraftList, JsonNode node) {
        Aircraft ac = null;
        try {
            ac = new ObjectMapper().treeToValue(node, Aircraft.class);
            aircraftList.add(ac);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/raw")
    String getAC() {
        return client.get()
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
