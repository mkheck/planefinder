package com.thehecklers.planefinder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class PlaneFinderService {
    private final PlaneRepository repo;
    private URL acURL;
    private final ObjectMapper om;

    @SneakyThrows
    public PlaneFinderService(PlaneRepository repo) {
        this.repo = repo;

        acURL = new URL("http://192.168.1.193/ajax/aircraft");
        om = new ObjectMapper();
    }

    public Iterable<Aircraft> getAircraft() throws IOException {
        JsonNode aircraftNodes = om.readTree(acURL)
                .get("aircraft");

        aircraftNodes.iterator()
                .forEachRemaining(this::saveAircraft);

        return repo.findAll();
    }

    private void saveAircraft(JsonNode node) {
        Aircraft ac = null;
        try {
            ac = om.treeToValue(node, Aircraft.class);
            repo.save(ac);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
