package com.example.OceanlandStatistics;

import com.example.OceanlandStatistics.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public String saveOlandPrice(double price) {
        return savePrice("olandPrice", price);
    }

    public String saveWaterPrice(double price) {
        return savePrice("waterPrice", price);
    }

    public String saveFoodPrice(double price) {
        return savePrice("foodPrice", price);
    }

    public String saveWoodPrice(double price) {
        return savePrice("woodPrice", price);
    }

    public String saveMetalPrice(double price) {
        return savePrice("metalPrice", price);
    }

    private String savePrice(String propertyName, double price) {
        Price lastPrice = priceRepository.findTopByOrderByIdDesc();
        if (lastPrice != null && getProperty(lastPrice, propertyName) == price) {
            return propertyName + " prices are the same. No update is made.";
        }
        Price newPrice = new Price();
        if (lastPrice != null) {
            copyProperties(lastPrice, newPrice);
        }
        setProperty(newPrice, propertyName, price);
        priceRepository.save(newPrice);
        return propertyName + " price is saved";
    }

    private double getProperty(Price price, String propertyName) {
        try {
            Class<?> clazz = price.getClass();
            Field field = clazz.getDeclaredField(propertyName);
            field.setAccessible(true);
            return (double) field.get(price);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void setProperty(Price price, String propertyName, double value) {
        try {
            Class<?> clazz = price.getClass();
            Field field = clazz.getDeclaredField(propertyName);
            field.setAccessible(true);
            field.setDouble(price, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyProperties(Price source, Price target) {
        try {
            Class<?> clazz = Price.class;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(target, field.get(source));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Price getLastPrices() {
        try {
            return priceRepository.findTopByOrderByIdDesc();
        } catch (Exception e) {
            return null;
        }
    }
}
