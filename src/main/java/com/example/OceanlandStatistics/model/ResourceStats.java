package com.example.OceanlandStatistics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceStats {
    private double totalWaterProduced;
    private double totalFoodProduced;
    private double totalWoodProduced;
    private double totalMetalProduced;

    private double totalWaterConsumed;
    private double totalFoodConsumed;
    private double totalWoodConsumed;
    private double totalMetalConsumed;

    private double netWaterProduced;
    private double netFoodProduced;
    private double netWoodProduced;
    private double netMetalProduced;

    // Getters and Setters

    public double getNetWaterProduced() {
        return totalWaterProduced - totalWaterConsumed;
    }

    public double getNetFoodProduced() {
        return totalFoodProduced - totalFoodConsumed;
    }

    public double getNetWoodProduced() {
        return totalWoodProduced - totalWoodConsumed;
    }

    public double getNetMetalProduced() {
        return totalMetalProduced - totalMetalConsumed;
    }

    public double setNetWaterProduced(double netWaterProduced) {
        return this.netWaterProduced = netWaterProduced;
    }

    public double setNetFoodProduced(double netFoodProduced) {
        return this.netFoodProduced = netFoodProduced;
    }

    public double setNetWoodProduced(double netWoodProduced) {
        return this.netWoodProduced = netWoodProduced;
    }

    public double setNetMetalProduced(double netMetalProduced) {
        return this.netMetalProduced = netMetalProduced;
    }

}
