package com.example.OceanlandStatistics.bot;

import com.example.OceanlandStatistics.PriceService;
import com.example.OceanlandStatistics.config.AuthProperties;
import com.example.OceanlandStatistics.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@RestController
@Getter
@Setter
class ApiController {
    private RestTemplate restTemplate;
    private AuthProperties authProperties;
    private double olandPrice;
    private double waterPrice;
    private double foodPrice;
    private double woodPrice;
    private double metalPrice;

    private final PriceService priceService;

    @Autowired
    public ApiController(PriceService priceService) {
        this.priceService = priceService;
    }


    //her saat başı fiyatları güncelle

    public void updatePricesFromDB() {
        Price lastPrice = priceService.getLastPrices();

        if (lastPrice != null) {
            this.olandPrice = lastPrice.getOlandPrice();
            this.waterPrice = lastPrice.getWaterPrice();
            this.foodPrice = lastPrice.getFoodPrice();
            this.woodPrice = lastPrice.getWoodPrice();
            this.metalPrice = lastPrice.getMetalPrice();
        }
    }

    @GetMapping("/fetch-token")
    public String fetchToken() {
        restTemplate = new RestTemplate();

        String apiUrl = "https://api.oceanland.io/api/auth/signin/email";
        String requestBody = "{ \"chainid\": \"" + "0x38" + "\", \"email\": \"" + "mmsuerkan@gmail.com" + "\", \"password\": \"" + "Mu102019*" + "\" }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        String accessToken = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            accessToken = jsonNode.get("accessToken").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }
    @GetMapping("/all-nfts-list")
    public List<DetailedEquipment> fetchDetailedEquipment() {
        String accessToken = fetchToken().substring("Access Token: ".length());
        String apiUrl = "https://api.oceanland.io/api/nfts";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<DetailedEquipment[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, request, DetailedEquipment[].class);
        return Arrays.asList(response.getBody());
    }
    @GetMapping("/fetch-equipped-nfts")
    public List<Equipment> fetchEquipment() {
        String accessToken = fetchToken();
        String apiUrl = "https://api.oceanland.io/api/equip";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Equipment[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, request, Equipment[].class);
        Equipment[] equipmentArray = response.getBody();
        return Arrays.asList(equipmentArray);
    }
    @GetMapping("/fetch-mapped-equipped-nfts")
    public List<DetailedEquipment> fetchEquippedNfts() {
        List<DetailedEquipment> allEquipmentDetails = fetchDetailedEquipment(); // Tüm ekipmanların detaylarını çekin
        List<Equipment> equippedEquipmentList = fetchEquipment(); // Kullanıcının ekipmanlarını çekin

        List<DetailedEquipment> userEquipmentDetails = new ArrayList<>();

        for (Equipment equipment : equippedEquipmentList) {
            for (DetailedEquipment detailedEquipment : allEquipmentDetails) {
                if (equipment.getNftId() == detailedEquipment.getId()) {
                    userEquipmentDetails.add(detailedEquipment);
                    break;
                }
            }
        }

        return userEquipmentDetails;
    }
    @GetMapping("/initial-full-craft-cost")
    public CraftCost initialCraftCost(){
        List<DetailedEquipment> nfts = fetchDetailedEquipment();

        Map<String, List<DetailedEquipment>> collect = nfts.stream().filter(nft -> nft.getTier() == 1 && nft.getType().equals("TOOL")).collect(Collectors.groupingBy(DetailedEquipment::getResourceType));
        double totalWood = 0;
        double totalMetal = 0;
        for (Map.Entry<String, List<DetailedEquipment>> entry : collect.entrySet()) {
            List<DetailedEquipment> value = entry.getValue();
            for (DetailedEquipment detailedEquipment : value) {
                totalWood += detailedEquipment.getNftResourceCraftCostList().get(0).getWood();
                totalMetal += detailedEquipment.getNftResourceCraftCostList().get(0).getMetal();
            }
        }
        updatePricesFromDB();
        double totalNeededOland = ((totalWood * woodPrice) + (totalMetal * metalPrice)) * 15;
        double totalNeededUsdt = totalNeededOland * olandPrice;
        CraftCost craftCost = new CraftCost();
        craftCost.setTotalNeededOland(totalNeededOland);
        craftCost.setTotalNeededUsdt(totalNeededUsdt);

        System.out.println("Total Needed Oland: " + totalNeededOland);
        System.out.println("Total Needed Usdt: " + totalNeededUsdt);

        return craftCost;
    }

    @GetMapping("/calculate-nft-stats")
    public List<NftStats> calculateNftStats() {
        List<DetailedEquipment> userEquipmentDetails = fetchEquippedNfts();

        Map<String, NftStats> nftStatsMap = new HashMap<>();

        for (DetailedEquipment equipment : userEquipmentDetails) {
            NftStats stats = nftStatsMap.get(equipment.getDisplayName());

            if (stats == null) {
                stats = new NftStats();
                stats.setDisplayName(equipment.getDisplayName());
                stats.setCount(1);
                stats.setTotalWaterConsumption(equipment.getNftResourceConsumptionList().get(0).getWater());
                stats.setTotalFoodConsumption(equipment.getNftResourceConsumptionList().get(0).getFood());
                stats.setTotalWoodConsumption(equipment.getNftResourceConsumptionList().get(0).getWood());
                stats.setTotalMetalConsumption(equipment.getNftResourceConsumptionList().get(0).getMetal());
                stats.setTotalWaterReward(equipment.getNftResourceRewardList().get(0).getWater());
                stats.setTotalFoodReward(equipment.getNftResourceRewardList().get(0).getFood());
                stats.setTotalWoodReward(equipment.getNftResourceRewardList().get(0).getWood());
                stats.setTotalMetalReward(equipment.getNftResourceRewardList().get(0).getMetal());
                nftStatsMap.put(equipment.getDisplayName(), stats);
            } else {
                stats.setCount(stats.getCount() + 1);
                stats.setTotalWaterConsumption(stats.getTotalWaterConsumption() + equipment.getNftResourceConsumptionList().get(0).getWater());
                stats.setTotalFoodConsumption(stats.getTotalFoodConsumption() + equipment.getNftResourceConsumptionList().get(0).getFood());
                stats.setTotalWoodConsumption(stats.getTotalWoodConsumption() + equipment.getNftResourceConsumptionList().get(0).getWood());
                stats.setTotalMetalConsumption(stats.getTotalMetalConsumption() + equipment.getNftResourceConsumptionList().get(0).getMetal());
                stats.setTotalWaterReward(stats.getTotalWaterReward() + equipment.getNftResourceRewardList().get(0).getWater());
                stats.setTotalFoodReward(stats.getTotalFoodReward() + equipment.getNftResourceRewardList().get(0).getFood());
                stats.setTotalWoodReward(stats.getTotalWoodReward() + equipment.getNftResourceRewardList().get(0).getWood());
                stats.setTotalMetalReward(stats.getTotalMetalReward() + equipment.getNftResourceRewardList().get(0).getMetal());
            }
        }

        return new ArrayList<>(nftStatsMap.values());
    }

    @GetMapping("/calculate-hourly-resource-stats")
    public ResourceStats calculateHourlyResourceStats() {
        List<DetailedEquipment> userEquipmentDetails = fetchEquippedNfts();

        ResourceStats resourceStats = new ResourceStats();

        for (DetailedEquipment detailedEquipment : userEquipmentDetails) {
            for (NftResourceReward nftResourceReward : detailedEquipment.getNftResourceRewardList()) {
                resourceStats.setTotalWaterProduced(resourceStats.getTotalWaterProduced() + nftResourceReward.getWater());
                resourceStats.setTotalFoodProduced(resourceStats.getTotalFoodProduced() + nftResourceReward.getFood());
                resourceStats.setTotalWoodProduced(resourceStats.getTotalWoodProduced() + nftResourceReward.getWood());
                resourceStats.setTotalMetalProduced(resourceStats.getTotalMetalProduced() + nftResourceReward.getMetal());
            }

            for (NftResourceConsumption nftResourceConsumption : detailedEquipment.getNftResourceConsumptionList()) {
                resourceStats.setTotalWaterConsumed(resourceStats.getTotalWaterConsumed() + nftResourceConsumption.getWater());
                resourceStats.setTotalFoodConsumed(resourceStats.getTotalFoodConsumed() + nftResourceConsumption.getFood());
                resourceStats.setTotalWoodConsumed(resourceStats.getTotalWoodConsumed() + nftResourceConsumption.getWood());
                resourceStats.setTotalMetalConsumed(resourceStats.getTotalMetalConsumed() + nftResourceConsumption.getMetal());
            }
        }

        resourceStats.setNetWaterProduced(resourceStats.getTotalWaterProduced() - resourceStats.getTotalWaterConsumed());
        resourceStats.setNetFoodProduced(resourceStats.getTotalFoodProduced() - resourceStats.getTotalFoodConsumed());
        resourceStats.setNetWoodProduced(resourceStats.getTotalWoodProduced() - resourceStats.getTotalWoodConsumed());
        resourceStats.setNetMetalProduced(resourceStats.getTotalMetalProduced() - resourceStats.getTotalMetalConsumed());

        return resourceStats;
    }
    @GetMapping("/calculate-daily-revenue")
    public RevenueStats calculateDailyRevenue() {

        updatePricesFromDB();

        // Net 1 saatlik üretim değerlerini al
        ResourceStats resourceStats = calculateHourlyResourceStats();

        // Günlük üretim değerlerini hesapla
        double dailyWater = resourceStats.getNetWaterProduced() * 24;
        double dailyFood = resourceStats.getNetFoodProduced() * 24;
        double dailyWood = resourceStats.getNetWoodProduced() * 24;
        double dailyMetal = resourceStats.getNetMetalProduced() * 24;

        // Günlük OLAND kazancı
        double dailyOlandRevenue = (dailyWater * waterPrice) + (dailyFood * foodPrice) + (dailyWood * woodPrice) + (dailyMetal * metalPrice);

        // Günlük USDT kazancı
        double dailyUsdtRevenue = dailyOlandRevenue * olandPrice;

        // Aylık OLAND kazancı
        double monthlyOlandRevenue = dailyOlandRevenue * 30;

        // Aylık USDT kazancı
        double monthlyUsdtRevenue = monthlyOlandRevenue * olandPrice;

        // Sonuçları bir nesne olarak döndür
        return new RevenueStats(dailyOlandRevenue, dailyUsdtRevenue, monthlyOlandRevenue, monthlyUsdtRevenue);
    }

    @PostMapping("/start-nfts")
    public ResponseEntity<String> startAllEquippedNFT() {
         startAllEquippedNFTs();
        return new ResponseEntity<>("Started all equipped NFTs.", HttpStatus.OK);
    }

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    @Scheduled(cron = "0 0 * * * *")
    public void startAllEquippedNFTs() {
        // Fetch equipped NFTs
        List<Equipment> equippedNFTs = fetchEquipment();

        // Filter NFTs with type TOOL
        List<Equipment> tools = equippedNFTs.stream()
                .filter(nft -> "TOOL".equals(nft.getNftType()))
                .collect(Collectors.toList());

        // Sort tools by next available time
        tools.sort(Comparator.comparingLong(Equipment::getNextAvailableTime));

        // Start all TOOL NFTs
        for (Equipment tool : tools) {
            startNFT(tool.getId());
        }
    }

    public void startNFT(long id) {
        String accessToken = fetchToken();
        String apiUrl = "https://api.oceanland.io/api/mine/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        // Optional: check the response
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Successfully started NFT with id: " + id);
        } else {
            System.out.println("Failed to start NFT with id: " + id);
        }
    }

    public Balance currentBalance() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = fetchToken();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);


        ResponseEntity<CurrentBalance> response = restTemplate.exchange("https://api.oceanland.io/api/users", HttpMethod.GET, request, CurrentBalance.class);
        updatePricesFromDB();
        double metalBalance = response.getBody().getMetalBalance();
        double foodBalance = response.getBody().getFoodBalance();
        double waterBalance = response.getBody().getWaterBalance();
        double woodBalance = response.getBody().getWoodBalance();

        double totalOland = metalBalance * metalPrice + foodBalance * foodPrice + waterBalance * waterPrice + woodBalance * woodPrice;
        double totalUsdt = totalOland * olandPrice;


        return new Balance(totalUsdt,totalOland);
    }


}
