package com.thehecklers.planefinder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaneFinderService {
    private final PlaneRepository repo;
    private URL acURL;
    private final ObjectMapper om;

    @SneakyThrows
    public PlaneFinderService(PlaneRepository repo) {
        this.repo = repo;

        acURL = new URL("http://192.168.1.139/ajax/aircraft");
        om = new ObjectMapper();
    }

    //    public Iterable<Aircraft> getAircraft() throws IOException {
    public Flux<Aircraft> getAircraft() {
        List<Aircraft> positions = new ArrayList<>();

        JsonNode aircraftNodes = null;
        try {
            aircraftNodes = om.readTree(acURL)
                    .get("aircraft");

            aircraftNodes.iterator().forEachRemaining(node -> {
                try {
                    positions.add(om.treeToValue(node, Aircraft.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("\n>>> IO Exception: " + e.getLocalizedMessage() +
                    ", generating and providing sample data.\n");
            return saveSamplePositions();
        }

        if (positions.size() > 0) {
            positions.forEach(System.out::println);

            return repo.deleteAll()
                    .thenMany(repo.saveAll(positions));
            //.thenMany(repo.findAll());
        } else {
            System.out.println("\n>>> No positions to report, generating and providing sample data.\n");

            return repo.deleteAll()
                    .thenMany(saveSamplePositions());
            //.thenMany(repo.findAll());
        }
    }

    private Flux<Aircraft> saveSamplePositions() {
        // Spring Airlines flight 001 en route, flying STL to SFO, at 30000' currently over Kansas City
        var ac1 = new Aircraft("SAL001", "N12345", "SAL001", "LJ",
                30000, 280, 440,
                39.2979849, -94.71921);

        // Spring Airlines flight 002 en route, flying SFO to STL, at 40000' currently over Denver
        var ac2 = new Aircraft("SAL002", "N54321", "SAL002", "LJ",
                40000, 65, 440,
                39.8560963, -104.6759263);

        // Spring Airlines flight 002 en route, flying SFO to STL, at 40000' currently just past DEN
        var ac3 = new Aircraft("SAL002", "N54321", "SAL002", "LJ",
                40000, 65, 440,
                39.8412964, -105.0048267);

        //return repo.saveAll(Flux.just(ac1, ac2, ac3));
        return Flux.just(ac1, ac2, ac3).flatMap(repo::save);
    }
}

