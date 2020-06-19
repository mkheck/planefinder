package com.thehecklers.planefinder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userLat, userLon;
    private Instant userTime;
}
