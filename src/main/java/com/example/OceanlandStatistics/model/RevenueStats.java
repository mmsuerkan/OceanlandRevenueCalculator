package com.example.OceanlandStatistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
@AllArgsConstructor
public class RevenueStats {
    private double dailyOlandRevenue;
    private double dailyUsdtRevenue;
    private double monthlyOlandRevenue;
    private double monthlyUsdtRevenue;

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return
                        "daily " + df.format(dailyOlandRevenue) +  " OLAND" + "\n" +
                        "daily " + df.format(dailyUsdtRevenue) +   " USDT"  + "\n" +
                        "monthly " + df.format(monthlyOlandRevenue) +  " OLAND" + "\n" +
                        "monthly " + df.format(monthlyUsdtRevenue)  +  " USDT"+ "\n";
    }
}

