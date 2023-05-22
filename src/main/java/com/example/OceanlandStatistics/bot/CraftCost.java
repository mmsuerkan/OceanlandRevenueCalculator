package com.example.OceanlandStatistics.bot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CraftCost {

    private double totalNeededOland;

    private double totalNeededUsdt;

    public void setTotalNeededOland(double totalNeededOland) {
        this.totalNeededOland = totalNeededOland;
    }

    public void setTotalNeededUsdt(double totalNeededUsdt) {
        this.totalNeededUsdt = totalNeededUsdt;
    }
}
