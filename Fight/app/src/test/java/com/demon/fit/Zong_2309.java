package com.demon.fit;

import com.google.gson.Gson;

import java.util.List;

/**
 * 2023-4-19 后为主力数据
 */
public class Zong_2309 {

    private static StringBuffer buffer_101 = new StringBuffer();
    private static StringBuffer buffer_60 = new StringBuffer();
    private static StringBuffer buffer_5 = new StringBuffer();

    public static void result() {
    }

    /**
     *
     * 日K
     */
    static {
        buffer_101.append("");
    }

    /**
     * 时K
     */
    static {
        buffer_60.append("棕榈油2309,p2309,2023-06-26 22:00,7092,7198,7216,7092,235994,16949664540,1.74,1.07,76.0,0.0\n" +
                "棕榈油2309,p2309,2023-06-26 23:00,7198,7166,7198,7160,119729,8596656680,0.53,-0.44,-32.0,0.0\n");
        buffer_60.append("棕榈油2309,p2309,2023-06-27 10:00,7172,7176,7186,7114,144149,10307699920,1.0,0.14,10.0,0.0\n" +
                "棕榈油2309,p2309,2023-06-27 11:15,7176,7206,7212,7162,99202,7131922300,0.7,0.42,30.0,0.0\n" +
                "棕榈油2309,p2309,2023-06-27 14:15,7206,7240,7250,7194,131516,9492795200,0.78,0.47,34.0,0.0\n" +
                "棕榈油2309,p2309,2023-06-27 15:00,7240,7156,7244,7154,126199,9077644780,1.24,-1.16,-84.0,0.0\n");
        buffer_60.append("");
        buffer_60.append("");
        buffer_60.append("");
        buffer_60.append("");
        buffer_60.append("");
    }

    /**
     * 5K
     */
    static {
        // 2023-06-26
        {
            buffer_5.append("棕榈油2309,p2309,2023-06-26 21:05,7092,7194,7202,7092,47037,3380096960,1.54,1.01,72.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:10,7194,7162,7196,7160,27180,1950064000,0.5,-0.44,-32.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:15,7160,7162,7176,7158,19915,1427329300,0.25,0.0,0.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:20,7162,7156,7170,7152,11983,857939160,0.25,-0.08,-6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:25,7156,7164,7166,7152,10134,725558140,0.2,0.11,8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:30,7162,7158,7170,7156,13595,973804720,0.2,-0.08,-6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:35,7158,7186,7188,7148,26719,1915289440,0.56,0.39,28.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:40,7186,7206,7210,7186,26500,1907922300,0.33,0.28,20.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:45,7206,7198,7216,7192,17942,1292364280,0.33,-0.11,-8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:50,7198,7198,7200,7186,10702,769817760,0.19,0.0,0.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 21:55,7198,7204,7210,7196,11412,822064420,0.19,0.08,6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:00,7204,7198,7212,7194,12875,927414060,0.25,-0.08,-6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:05,7198,7190,7198,7180,16439,1181650960,0.25,-0.11,-8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:10,7190,7178,7190,7172,12081,867338820,0.25,-0.17,-12.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:15,7176,7184,7188,7174,8791,631373600,0.2,0.08,6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:20,7184,7194,7198,7172,12581,904189240,0.36,0.14,10.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:25,7194,7184,7198,7180,8816,633748840,0.25,-0.14,-10.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:30,7186,7176,7186,7172,8395,602540500,0.19,-0.11,-8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:35,7176,7182,7184,7172,8134,583896220,0.17,0.08,6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:40,7182,7178,7188,7174,7123,511448800,0.19,-0.06,-4.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:45,7178,7172,7184,7168,9199,660118680,0.22,-0.08,-6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:50,7172,7180,7180,7168,7717,553551960,0.17,0.11,8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 22:55,7180,7178,7184,7174,7014,503548300,0.14,-0.03,-2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-26 23:00,7178,7166,7178,7160,13439,963250760,0.25,-0.17,-12.0,0.0\n"
            );
        }
        // 2023-06-27
        {
            buffer_5.append("棕榈油2309,p2309,2023-06-27 09:05,7172,7126,7182,7122,36719,2624172120,0.84,-0.56,-40.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:10,7126,7144,7146,7114,19554,1394269000,0.45,0.25,18.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:15,7144,7144,7154,7136,11195,799897720,0.25,0.0,0.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:20,7144,7142,7148,7130,8001,571161180,0.25,-0.03,-2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:25,7144,7138,7152,7134,8727,623238840,0.25,-0.06,-4.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:30,7138,7158,7166,7138,12167,870313100,0.39,0.28,20.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:35,7158,7150,7166,7150,10844,776469380,0.22,-0.11,-8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:40,7152,7164,7166,7150,7717,552459000,0.22,0.2,14.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:45,7166,7166,7170,7158,6892,493743500,0.17,0.03,2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:50,7164,7174,7176,7160,6722,481809900,0.22,0.11,8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 09:55,7172,7178,7178,7164,6693,479898240,0.2,0.06,4.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:00,7178,7176,7186,7172,8918,640267940,0.2,-0.03,-2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:05,7176,7164,7178,7162,6116,438440920,0.22,-0.17,-12.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:10,7164,7172,7176,7164,4073,292060560,0.17,0.11,8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:15,7170,7182,7186,7166,4947,355053720,0.28,0.14,10.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:35,7186,7178,7200,7172,17596,1264629580,0.39,-0.06,-4.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:40,7178,7196,7200,7172,11307,812916500,0.39,0.25,18.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:45,7196,7194,7200,7192,6534,470209880,0.11,-0.03,-2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:50,7194,7192,7196,7190,4953,356243880,0.08,-0.03,-2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 10:55,7192,7194,7198,7184,8062,579724220,0.19,0.03,2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 11:00,7194,7184,7204,7176,11213,806114900,0.39,-0.14,-10.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 11:05,7184,7194,7194,7180,5839,419676580,0.19,0.14,10.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 11:10,7192,7200,7202,7190,5946,427905500,0.17,0.08,6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 11:15,7202,7206,7212,7198,12616,908946060,0.19,0.08,6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 11:20,7206,7200,7206,7200,4489,323343100,0.08,-0.08,-6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 11:25,7200,7206,7206,7198,4645,334566220,0.11,0.08,6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 11:30,7204,7222,7226,7204,17612,1271047580,0.31,0.22,16.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 13:35,7216,7206,7216,7198,16892,1216995160,0.25,-0.22,-16.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 13:40,7206,7206,7206,7198,7868,566664360,0.11,0.0,0.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 13:45,7206,7198,7206,7194,7645,550302280,0.17,-0.11,-8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 13:50,7196,7196,7200,7194,5434,391069920,0.08,-0.03,-2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 13:55,7198,7208,7208,7196,5966,429699480,0.17,0.17,12.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:00,7208,7212,7212,7200,6811,490761220,0.17,0.06,4.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:05,7210,7236,7246,7206,29356,2122595960,0.55,0.33,24.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:10,7238,7242,7246,7234,13782,997775580,0.17,0.08,6.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:15,7242,7240,7250,7236,11016,797974340,0.19,-0.03,-2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:20,7240,7240,7244,7238,7150,517711080,0.08,0.0,0.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:25,7240,7226,7244,7222,10748,777381620,0.3,-0.19,-14.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:30,7226,7224,7230,7218,8454,610720100,0.17,-0.03,-2.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:35,7224,7220,7230,7218,7648,552416540,0.17,-0.06,-4.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:40,7220,7198,7224,7196,14426,1039600500,0.39,-0.3,-22.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:45,7196,7172,7198,7160,34691,2489623140,0.53,-0.36,-26.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:50,7172,7180,7184,7168,12873,923776560,0.22,0.11,8.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 14:55,7182,7180,7186,7176,9985,717003020,0.14,0.0,0.0,0.0\n" +
                    "棕榈油2309,p2309,2023-06-27 15:00,7182,7156,7182,7154,20224,1449412220,0.39,-0.33,-24.0,0.0\n");
        }
        buffer_5.append("");
        buffer_5.append("");
    }


}
