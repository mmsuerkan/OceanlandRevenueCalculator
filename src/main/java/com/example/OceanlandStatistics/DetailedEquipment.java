package com.example.OceanlandStatistics;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// DetailedEquipment sınıfı ve ilgili alanlar
@Getter
@Setter
public class DetailedEquipment {
    private int id;
    private String displayName;
    private String type;
    private int tier;
    private int level;
    private String resourceType;
    private int chargeTime;
    private int durabilityCount;
    private String properties;
    private boolean isCraftable;
    private boolean isEquipable;
    private String status;
    private Object nftClaimId;
    private List<NftResourceConsumption> nftResourceConsumptionList;
    private List<NftResourceCraftCost> nftResourceCraftCostList;
    private List<NftResourceReward> nftResourceRewardList;

}

