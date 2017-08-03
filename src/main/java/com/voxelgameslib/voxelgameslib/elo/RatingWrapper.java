package com.voxelgameslib.voxelgameslib.elo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import jskills.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@Table(name = "rating")
public class RatingWrapper {

    @Id
    @GeneratedValue
    private int id;

    private double conservativeStandardDeviationMultiplier;
    private double mean;
    private double standardDeviation;
    private double conservativeRating;

    public RatingWrapper(double conservativeStandardDeviationMultiplier, double mean, double standardDeviation, double conservativeRating) {
        this.conservativeStandardDeviationMultiplier = conservativeStandardDeviationMultiplier;
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.conservativeRating = conservativeRating;
    }

    protected RatingWrapper() {
        //JPA
    }

    public RatingWrapper(Rating rating) {
        this(rating.getConservativeStandardDeviationMultiplier(), rating.getMean(), rating.getStandardDeviation(), rating.getConservativeRating());
    }

    public Rating toRating() {
        return new Rating(getMean(), getStandardDeviation(), getConservativeStandardDeviationMultiplier());
    }
}
