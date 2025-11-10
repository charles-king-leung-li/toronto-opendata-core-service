package com.toronto.opendata.core.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple model representing a 2D coordinate point with a type.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultiPointModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private double x;
    private double y;
    private String type;
}
