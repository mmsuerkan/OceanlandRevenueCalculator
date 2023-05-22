package com.example.OceanlandStatistics.bot;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Balance {

    private double totalUsdt;
    private double totalOland;

    public Balance(double totalUsdt, double totalOland) {
        this.totalOland = totalOland;
        this.totalUsdt = totalUsdt;
    }
}
