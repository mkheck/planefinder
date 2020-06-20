package com.thehecklers.planefinder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@RestController
public class PlaneController {
    private final WebClient client;
    private final PlaneFinderService pfService;

    public PlaneController(WebClient client, PlaneFinderService pfService) {
        this.client = client;
        this.pfService = pfService;
    }

    @GetMapping
    public Iterable<Aircraft> getCurrentAircraft() throws IOException {
        return pfService.getAircraft();
    }
}
