package com.thehecklers.planefinder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PlaneFinderService {
    private final PlaneRepository repo;
    private final FlightGenerator generator;
    private HttpURLConnection acConn;
    private final ObjectMapper om;

    @SneakyThrows
    public PlaneFinderService(PlaneRepository repo, FlightGenerator generator) {
        this.repo = repo;
        this.generator = generator;

        om = new ObjectMapper();
    }

    //    public Iterable<Aircraft> getAircraft() throws IOException {
    public Flux<Aircraft> getAircraft() {
        List<Aircraft> positions = new ArrayList<>();

        try {
            acConn = (HttpURLConnection) new URL("http://192.168.1.139/ajax/aircraft").openConnection();
            acConn.setConnectTimeout(1000);

            JsonNode aircraftNodes = om.readTree(acConn.getInputStream())
                    .get("aircraft");

            aircraftNodes.iterator().forEachRemaining(node -> {
                try {
                    positions.add(om.treeToValue(node, Aircraft.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("\n>>> IO Exception: " + e.getLocalizedMessage() +
                    ", generating and providing sample data.\n");
            return repo.deleteAll()
                    .thenMany(saveSamplePositions());
        }

        if (positions.size() > 0) {
            positions.forEach(System.out::println);

            return repo.deleteAll()
                    .thenMany(repo.saveAll(positions));
        } else {
            System.out.println("\n>>> No positions to report, generating and providing sample data.\n");

            return repo.deleteAll()
                    .thenMany(saveSamplePositions());
        }
    }

    private Flux<Aircraft> saveSamplePositions() {
        final Random rnd = new Random();

        return Flux.range(1, rnd.nextInt(10))
                .map(i -> generator.generate())
                .flatMap(repo::save);
    }
}

