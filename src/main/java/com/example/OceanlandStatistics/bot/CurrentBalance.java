package com.example.OceanlandStatistics.bot;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CurrentBalance {

    private int id;
    private String publicKey;
    private String chainId;
    private String email;
    private boolean isValidated;
    private double waterBalance;
    private double foodBalance;
    private double woodBalance;
    private double metalBalance;
    private String referenceCode;
    private Integer newsId;

}
