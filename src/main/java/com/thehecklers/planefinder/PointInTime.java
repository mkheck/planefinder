package com.thehecklers.planefinder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointInTime {
    private User user;
    private Iterable<Aircraft> aircraft;
}
