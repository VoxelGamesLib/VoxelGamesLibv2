package com.voxelgameslib.voxelgameslib.elo;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import jskills.Rating;

@Entity
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

    public RatingWrapper(@Nonnull Rating rating) {
        this(rating.getConservativeStandardDeviationMultiplier(), rating.getMean(), rating.getStandardDeviation(), rating.getConservativeRating());
    }

    @java.beans.ConstructorProperties({"id", "conservativeStandardDeviationMultiplier", "mean", "standardDeviation", "conservativeRating"})
    public RatingWrapper(int id, double conservativeStandardDeviationMultiplier, double mean, double standardDeviation, double conservativeRating) {
        this.id = id;
        this.conservativeStandardDeviationMultiplier = conservativeStandardDeviationMultiplier;
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.conservativeRating = conservativeRating;
    }

    @Nonnull
    public Rating toRating() {
        return new Rating(getMean(), getStandardDeviation(), getConservativeStandardDeviationMultiplier());
    }

    public int getId() {
        return this.id;
    }

    public double getConservativeStandardDeviationMultiplier() {
        return this.conservativeStandardDeviationMultiplier;
    }

    public double getMean() {
        return this.mean;
    }

    public double getStandardDeviation() {
        return this.standardDeviation;
    }

    public double getConservativeRating() {
        return this.conservativeRating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConservativeStandardDeviationMultiplier(double conservativeStandardDeviationMultiplier) {
        this.conservativeStandardDeviationMultiplier = conservativeStandardDeviationMultiplier;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public void setConservativeRating(double conservativeRating) {
        this.conservativeRating = conservativeRating;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof RatingWrapper)) return false;
        final RatingWrapper other = (RatingWrapper) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        if (Double.compare(this.getConservativeStandardDeviationMultiplier(), other.getConservativeStandardDeviationMultiplier()) != 0)
            return false;
        if (Double.compare(this.getMean(), other.getMean()) != 0) return false;
        if (Double.compare(this.getStandardDeviation(), other.getStandardDeviation()) != 0) return false;
        if (Double.compare(this.getConservativeRating(), other.getConservativeRating()) != 0) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final long $conservativeStandardDeviationMultiplier = Double.doubleToLongBits(this.getConservativeStandardDeviationMultiplier());
        result = result * PRIME + (int) ($conservativeStandardDeviationMultiplier >>> 32 ^ $conservativeStandardDeviationMultiplier);
        final long $mean = Double.doubleToLongBits(this.getMean());
        result = result * PRIME + (int) ($mean >>> 32 ^ $mean);
        final long $standardDeviation = Double.doubleToLongBits(this.getStandardDeviation());
        result = result * PRIME + (int) ($standardDeviation >>> 32 ^ $standardDeviation);
        final long $conservativeRating = Double.doubleToLongBits(this.getConservativeRating());
        result = result * PRIME + (int) ($conservativeRating >>> 32 ^ $conservativeRating);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RatingWrapper;
    }

    public String toString() {
        return "RatingWrapper(id=" + this.getId() + ", conservativeStandardDeviationMultiplier=" + this.getConservativeStandardDeviationMultiplier() + ", mean=" + this.getMean() + ", standardDeviation=" + this.getStandardDeviation() + ", conservativeRating=" + this.getConservativeRating() + ")";
    }
}
