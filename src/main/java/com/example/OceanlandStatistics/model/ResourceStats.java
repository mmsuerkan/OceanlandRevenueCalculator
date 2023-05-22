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

    @Override
    public String toString() {
        return  "NetWater: " + netWaterProduced + "\n " +
                "NetFood: " + netFoodProduced   + "\n " +
                "NetWood: " + netWoodProduced   + "\n " +
                "NetMetal: " + netMetalProduced + "\n"  +
                "Daily Net Water: " + netWaterProduced * 24 + "\n " +
                "Daily Net Food: " + netFoodProduced * 24  + "\n " +
                "Daily Net Wood: " + netWoodProduced * 24  + "\n " +
                "Daily Net Metal: " + netMetalProduced * 24 + "\n"  ;
    }
}
