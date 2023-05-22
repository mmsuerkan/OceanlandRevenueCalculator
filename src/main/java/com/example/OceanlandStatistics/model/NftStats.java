package com.example.OceanlandStatistics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NftStats {
    private String displayName;
    private int count;
    private double totalWaterConsumption;
    private double totalFoodConsumption;
    private double totalWoodConsumption;
    private double totalMetalConsumption;
    private double totalWaterReward;
    private double totalFoodReward;
    private double totalWoodReward;
    private double totalMetalReward;

    @Override
    public String toString() {
        return "Nft Name : " + displayName + "\n" +
                " Count=" + count + "\n" +
                " TotalWaterConsumption=" + totalWaterConsumption + "\n" +
                " TotalFoodConsumption=" + totalFoodConsumption + "\n" +
                " TotalWoodConsumption=" + totalWoodConsumption + "\n" +
                " TotalMetalConsumption=" + totalMetalConsumption + "\n" +
                " TotalWaterReward=" + totalWaterReward + "\n" +
                " TotalFoodReward=" + totalFoodReward + "\n" +
                " TotalWoodReward=" + totalWoodReward + "\n" +
                " TotalMetalReward=" + totalMetalReward + "\n" ;
    }
}
