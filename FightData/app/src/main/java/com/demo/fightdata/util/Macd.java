package com.demo.fightdata.util;

import java.util.*;

public class Macd {
    private static List<Double> prices;
    private final static double EMA_12_AlPHA = 2d / (1d + 12d);
    private final static double EMA_26_AlPHA = 2d / (1d + 26d);

    public static void main(String []args){
        prices = new ArrayList<Double>();

        for(int i=0; i<100; i++) {
            prices.add(new Double(i));
        }

        for(int i = 25; i < prices.size(); i++) {
//            final double macd = getEma12(i) - getEma26(i);
//            System.out.println(macd);
        }
    }

    public static double getEma12(List<Double> prices, int day) {
        if(day < 11)
            System.err.println("Day must be >= 11");
        double ema12 = 0d;
        for(int i=day-10; i<=day; i++) {
            ema12 = EMA_12_AlPHA * prices.get(i) + (1d - EMA_12_AlPHA) * ema12;
        }
        return ema12;
    }

    public static double getEma26(List<Double> prices, int day) {
        if(day < 25)
            System.err.println("Day must be >= 25");
        double ema26 = 0d;
        for(int i=day-24; i<=day; i++) {
            ema26 = EMA_26_AlPHA * prices.get(i) + (1d - EMA_26_AlPHA) * ema26;
        }
        return ema26;
    }
}