package com.demon.fit;

import com.google.gson.Gson;

import java.util.List;

/**
 * 2023-4-19 后为主力数据
 */
public class Cai_2309 {

    private static StringBuffer buffer_101 = new StringBuffer();
    private static StringBuffer buffer_60 = new StringBuffer();
    private static StringBuffer buffer_5 = new StringBuffer();

    public static void result() {
        List<Bean> list = BeanUtil.parse(buffer_101.toString());
        System.out.println("数据大小：" + list.size());
        for (Bean bean : list) {
            System.out.println(new Gson().toJson(bean));
        }
    }

    /**
     *
     * 日K
     */
    static {
        buffer_101.append("菜油309,OI309,2023-03-01,9908,9897,9938,9847,21394,2117150240,0.92,-0.06,-6.0,0.0\n" +
                "菜油309,OI309,2023-03-02,9897,9958,9979,9867,49210,4876710912,1.13,0.63,62.0,0.0\n" +
                "菜油309,OI309,2023-03-03,9945,10041,10060,9940,31184,3116528960,1.21,1.32,131.0,0.0\n" +
                "菜油309,OI309,2023-03-06,10026,9860,10036,9857,31524,3134116080,1.79,-1.34,-134.0,0.0\n" +
                "菜油309,OI309,2023-03-07,9857,9806,9860,9793,25383,2494133584,0.67,-1.37,-136.0,0.0\n" +
                "菜油309,OI309,2023-03-08,9776,9698,9808,9696,27481,2677748640,1.14,-1.3,-128.0,0.0\n" +
                "菜油309,OI309,2023-03-09,9697,9599,9716,9568,37274,3595450048,1.52,-1.49,-145.0,0.0\n" +
                "菜油309,OI309,2023-03-10,9602,9305,9641,9279,54936,5178816768,3.75,-3.54,-341.0,0.0\n" +
                "菜油309,OI309,2023-03-13,9298,9259,9320,9205,38334,3551261760,1.22,-1.78,-168.0,0.0\n" +
                "菜油309,OI309,2023-03-14,9130,9193,9237,9047,45123,4137327872,2.05,-0.77,-71.0,0.0\n" +
                "菜油309,OI309,2023-03-15,9206,9090,9214,9082,37240,3406342800,1.44,-0.86,-79.0,0.0\n" +
                "菜油309,OI309,2023-03-16,9000,8841,9049,8832,67362,6016100352,2.37,-3.35,-306.0,0.0\n" +
                "菜油309,OI309,2023-03-17,8855,8875,8952,8674,88061,7774905600,3.11,-0.63,-56.0,0.0\n" +
                "菜油309,OI309,2023-03-20,8851,8813,8914,8775,62418,5520247808,1.57,-0.18,-16.0,0.0\n" +
                "菜油309,OI309,2023-03-21,8827,8712,8861,8688,80003,7011462912,1.96,-1.49,-132.0,0.0\n" +
                "菜油309,OI309,2023-03-22,8734,8629,8763,8583,89589,7759303424,2.05,-1.54,-135.0,0.0\n" +
                "菜油309,OI309,2023-03-23,8649,8370,8651,8350,121060,10271941120,3.48,-3.36,-291.0,0.0\n" +
                "菜油309,OI309,2023-03-24,8357,8338,8357,8127,218927,18094316544,2.71,-1.73,-147.0,0.0\n" +
                "菜油309,OI309,2023-03-27,8280,8361,8469,8261,197708,16558044928,2.52,1.16,96.0,0.0\n" +
                "菜油309,OI309,2023-03-28,8376,8437,8489,8360,189731,15982939392,1.54,0.74,62.0,0.0\n" +
                "菜油309,OI309,2023-03-29,8430,8366,8486,8318,181955,15302415616,1.99,-0.69,-58.0,0.0\n" +
                "菜油309,OI309,2023-03-30,8392,8449,8471,8322,155213,13013058048,1.77,0.46,39.0,0.0\n" +
                "菜油309,OI309,2023-03-31,8450,8555,8594,8450,194812,16642789120,1.72,2.04,171.0,0.0\n" +
                "菜油309,OI309,2023-04-03,8573,8628,8768,8558,173861,15052885504,2.46,0.99,85.0,0.0\n" +
                "菜油309,OI309,2023-04-04,8619,8672,8700,8614,136447,11825861376,0.99,0.16,14.0,0.0\n" +
                "菜油309,OI309,2023-04-06,8620,8619,8694,8535,146322,12639294464,1.83,-0.55,-48.0,0.0\n" +
                "菜油309,OI309,2023-04-07,8584,8643,8687,8500,221336,19021615872,2.16,0.06,5.0,0.0\n" +
                "菜油309,OI309,2023-04-10,8662,8548,8681,8527,186650,16031368448,1.79,-0.54,-46.0,0.0\n" +
                "菜油309,OI309,2023-04-11,8534,8624,8673,8534,241153,20784977152,1.62,0.41,35.0,0.0\n" +
                "菜油309,OI309,2023-04-12,8604,8508,8679,8459,338360,28991926272,2.55,-1.29,-111.0,0.0\n" +
                "菜油309,OI309,2023-04-13,8523,8455,8534,8352,357240,30122476800,2.12,-1.32,-113.0,0.0\n" +
                "菜油309,OI309,2023-04-14,8434,8481,8589,8434,377391,32089556736,1.84,0.58,49.0,0.0\n" +
                "菜油309,OI309,2023-04-17,8486,8505,8551,8415,380964,32317176064,1.6,0.02,2.0,0.0\n" +
                "菜油309,OI309,2023-04-18,8513,8665,8677,8501,516756,44384172800,2.07,2.15,182.0,0.0\n" +
                "菜油309,OI309,2023-04-19,8686,8648,8785,8640,657068,57296329728,1.69,0.69,59.0,0.0\n" +
                "菜油309,OI309,2023-04-20,8642,8517,8642,8465,675735,57869945344,2.03,-2.33,-203.0,0.0\n" +
                "菜油309,OI309,2023-04-21,8503,8357,8530,8335,590663,49733824512,2.28,-2.42,-207.0,0.0\n" +
                "菜油309,OI309,2023-04-24,8369,8226,8407,8208,610119,50664281856,2.36,-2.3,-194.0,0.0\n" +
                "菜油309,OI309,2023-04-25,8246,8238,8335,8204,539238,44573413120,1.58,-0.79,-66.0,0.0\n" +
                "菜油309,OI309,2023-04-26,8230,8149,8250,8024,858348,69740773376,2.73,-1.42,-117.0,0.0\n" +
                "菜油309,OI309,2023-04-27,8105,8063,8186,8035,619929,50263843328,1.86,-0.76,-62.0,0.0\n" +
                "菜油309,OI309,2023-04-28,8041,8047,8136,8015,616965,49838432768,1.49,-0.75,-61.0,0.0\n" +
                "菜油309,OI309,2023-05-04,8034,8127,8150,7832,591244,47329082112,3.94,0.61,49.0,0.0\n" +
                "菜油309,OI309,2023-05-05,8144,8161,8180,8030,623018,50620212480,1.87,1.95,156.0,0.0\n" +
                "菜油309,OI309,2023-05-08,8177,8322,8376,8162,758611,62790232576,2.63,2.42,197.0,0.0\n" +
                "菜油309,OI309,2023-05-09,8343,8239,8385,8226,601959,49902401024,1.92,-0.46,-38.0,0.0\n" +
                "菜油309,OI309,2023-05-10,8263,8248,8326,8181,572922,47323357184,1.75,-0.51,-42.0,0.0\n" +
                "菜油309,OI309,2023-05-11,8231,8075,8280,8060,737860,60312676352,2.66,-2.24,-185.0,0.0\n" +
                "菜油309,OI309,2023-05-12,8048,8003,8076,7964,569402,45677428480,1.37,-2.09,-171.0,0.0\n" +
                "菜油309,OI309,2023-05-15,8020,8110,8170,7908,797736,64249657344,3.27,1.1,88.0,0.0\n" +
                "菜油309,OI309,2023-05-16,8145,8076,8216,8045,654737,53249760256,2.12,0.27,22.0,0.0\n" +
                "菜油309,OI309,2023-05-17,8053,7976,8115,7935,699855,56156365312,2.21,-1.93,-157.0,0.0\n" +
                "菜油309,OI309,2023-05-18,7990,7926,8018,7893,642194,51022313216,1.56,-1.22,-98.0,0.0\n" +
                "菜油309,OI309,2023-05-19,7900,8015,8098,7874,810474,64756872704,2.82,0.88,70.0,0.0\n" +
                "菜油309,OI309,2023-05-22,8062,7675,8079,7666,786879,61762132736,5.17,-3.94,-315.0,0.0\n" +
                "菜油309,OI309,2023-05-23,7712,7642,7752,7624,646520,49659201280,1.63,-2.64,-207.0,0.0\n" +
                "菜油309,OI309,2023-05-24,7659,7676,7730,7595,633961,48599450368,1.76,-0.07,-5.0,0.0\n" +
                "菜油309,OI309,2023-05-25,7675,7758,7777,7592,640358,49128265728,2.41,1.2,92.0,0.0\n" +
                "菜油309,OI309,2023-05-26,7770,7817,7844,7706,578831,45056205056,1.8,1.89,145.0,0.0\n" +
                "菜油309,OI309,2023-05-29,7825,7789,7903,7763,601732,47061459712,1.8,0.06,5.0,0.0\n" +
                "菜油309,OI309,2023-05-30,7799,7759,7837,7694,610524,47486556672,1.83,-0.79,-62.0,0.0\n" +
                "菜油309,OI309,2023-05-31,7722,7469,7738,7447,697991,52656441088,3.74,-3.97,-309.0,0.0\n" +
                "菜油309,OI309,2023-06-01,7420,7612,7614,7375,763094,57125216768,3.17,0.9,68.0,0.0\n" +
                "菜油309,OI309,2023-06-02,7630,7715,7746,7550,628101,47955511296,2.62,3.06,229.0,0.0\n" +
                "菜油309,OI309,2023-06-05,7741,7723,7855,7663,715052,55452282624,2.51,1.15,88.0,0.0\n" +
                "菜油309,OI309,2023-06-06,7712,7732,7789,7574,841224,64589178624,2.77,-0.3,-23.0,0.0\n" +
                "菜油309,OI309,2023-06-07,7717,7686,7816,7662,604680,46784091648,2.01,0.1,8.0,0.0\n" +
                "菜油309,OI309,2023-06-08,7694,7660,7785,7625,699322,53896746496,2.07,-1.0,-77.0,0.0\n" +
                "菜油309,OI309,2023-06-09,7670,7943,7948,7645,750061,58527259904,3.93,3.06,236.0,0.0\n" +
                "菜油309,OI309,2023-06-12,7938,7858,8022,7826,647142,51279532032,2.51,0.7,55.0,0.0\n" +
                "菜油309,OI309,2023-06-13,7850,7954,8017,7808,661725,52395385600,2.64,0.38,30.0,0.0\n" +
                "菜油309,OI309,2023-06-14,8006,8098,8166,7976,740731,59836250112,2.4,2.27,180.0,0.0\n" +
                "菜油309,OI309,2023-06-15,8077,8169,8196,8077,629277,51147634688,1.47,1.13,91.0,0.0\n" +
                "菜油309,OI309,2023-06-16,8195,8473,8486,8168,756760,63030540288,3.91,4.24,345.0,0.0\n" +
                "菜油309,OI309,2023-06-19,8500,8484,8564,8412,721943,61285741312,1.82,1.86,155.0,0.0\n" +
                "菜油309,OI309,2023-06-20,8502,8591,8665,8464,717641,61501833728,2.37,1.2,102.0,0.0\n" +
                "菜油309,OI309,2023-06-21,8555,8500,8678,8281,921116,78534348800,4.63,-0.82,-70.0,0.0\n" +
                "菜油309,OI309,2023-06-26,8549,8630,8665,8445,594989,50818010624,2.58,1.22,104.0,0.0\n" +
                "菜油309,OI309,2023-06-27,8650,8583,8714,8575,728340,63015976800,1.63,0.49,42.0,0.0\n");
    }

    /**
     * 时K
     */
    static {
        buffer_60.append("菜油309,OI309,2023-06-26 22:00,8541,8687,8714,8541,206456,17895606080,2.0,0.66,57.0,0.0\n" +
                "菜油309,OI309,2023-06-26 23:00,8686,8654,8697,8654,107539,9327760420,0.49,-0.38,-33.0,0.0\n" +
                "菜油309,OI309,2023-06-27 10:00,8665,8635,8665,8601,124182,10718379930,0.74,-0.22,-19.0,0.0\n" +
                "菜油309,OI309,2023-06-27 11:15,8636,8644,8654,8606,71527,6173134770,0.56,0.1,9.0,0.0\n" +
                "菜油309,OI309,2023-06-27 14:15,8644,8687,8703,8640,102859,8926948970,0.73,0.5,43.0,0.0\n" +
                "菜油309,OI309,2023-06-27 15:00,8686,8583,8687,8575,115777,9974146630,1.29,-1.2,-104.0,0.0\n");
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
            buffer_5.append("菜油309,OI309,2023-06-26 21:05,8541,8693,8714,8541,47173,4097918510,2.0,0.73,63.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:10,8692,8674,8703,8665,24342,2113874390,0.44,-0.22,-19.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:15,8674,8662,8675,8656,13955,1209567200,0.22,-0.14,-12.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:20,8660,8641,8668,8640,14045,1214551600,0.32,-0.24,-21.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:25,8642,8645,8649,8638,8670,749137050,0.13,0.05,4.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:30,8645,8630,8646,8627,11061,955964370,0.22,-0.17,-15.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:35,8627,8651,8653,8626,18324,1583554480,0.31,0.24,21.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:40,8649,8661,8663,8642,15895,1374709300,0.24,0.12,10.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:45,8662,8663,8674,8661,14891,1290454060,0.15,0.02,2.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:50,8663,8669,8673,8661,9079,786786140,0.14,0.07,6.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 21:55,8669,8680,8684,8668,12391,1075702320,0.18,0.13,11.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:00,8680,8687,8693,8678,16630,1443386660,0.17,0.08,7.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:05,8686,8682,8686,8670,15639,1357809470,0.18,-0.06,-5.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:10,8681,8675,8682,8665,14271,1237152990,0.2,-0.08,-7.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:15,8674,8677,8681,8669,6031,522827390,0.14,0.02,2.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:20,8677,8684,8688,8665,8236,716485170,0.27,0.08,7.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:25,8683,8683,8697,8680,11917,1035829400,0.2,-0.01,-1.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:30,8683,8674,8686,8671,7153,620236630,0.17,-0.1,-9.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:35,8674,8678,8680,8663,9106,789581260,0.2,0.05,4.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:40,8677,8674,8685,8674,5293,458956030,0.13,-0.05,-4.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:45,8674,8667,8676,8664,6457,559886470,0.14,-0.08,-7.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:50,8667,8666,8673,8662,7683,666192930,0.13,-0.01,-1.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 22:55,8666,8661,8668,8655,7320,634717200,0.15,-0.06,-5.0,0.0\n" +
                    "菜油309,OI309,2023-06-26 23:00,8661,8654,8665,8654,8433,728085480,0.13,-0.08,-7.0,0.0\n");
        }
        // 2023-06-27
        {
            buffer_5.append("菜油309,OI309,2023-06-27 09:05,8665,8627,8665,8622,25907,2239338860,0.5,-0.31,-27.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:10,8625,8638,8642,8616,15415,1329065860,0.3,0.13,11.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:15,8639,8632,8644,8624,10442,901246130,0.23,-0.07,-6.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:20,8632,8625,8633,8620,8915,768738010,0.15,-0.08,-7.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:25,8625,8611,8628,8605,13897,1196264660,0.27,-0.16,-14.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:30,8611,8617,8621,8601,11452,987972010,0.23,0.07,6.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:35,8617,8623,8630,8613,7939,683517170,0.2,0.07,6.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:40,8624,8643,8644,8623,7938,687430800,0.24,0.23,20.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:45,8643,8644,8646,8635,5210,451186000,0.13,0.01,1.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:50,8642,8645,8649,8640,5810,498876800,0.1,0.01,1.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 09:55,8644,8643,8644,8631,5784,500836560,0.15,-0.02,-2.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:00,8643,8635,8649,8632,5473,473907070,0.2,-0.09,-8.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:05,8636,8612,8636,8606,9940,856223430,0.35,-0.27,-23.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:10,8613,8621,8625,8612,5200,445682830,0.15,0.1,9.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:15,8621,8633,8634,8617,2805,242828850,0.2,0.14,12.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:35,8637,8616,8644,8613,8445,731083650,0.36,-0.2,-17.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:40,8617,8635,8639,8611,7254,623260570,0.32,0.22,19.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:45,8633,8636,8643,8630,4867,421287520,0.15,0.01,1.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:50,8636,8641,8646,8633,4071,352385760,0.15,0.06,5.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 10:55,8643,8647,8650,8635,6511,563592160,0.17,0.07,6.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 11:00,8647,8636,8654,8632,7273,629550880,0.25,-0.13,-11.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 11:05,8635,8638,8638,8627,4835,413523820,0.13,0.02,2.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 11:10,8638,8644,8644,8633,5334,461657700,0.13,0.07,6.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 11:15,8644,8644,8648,8634,4992,432057600,0.16,0.0,0.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 11:20,8644,8658,8660,8640,6136,531070800,0.23,0.16,14.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 11:25,8658,8675,8677,8655,10553,913362150,0.25,0.2,17.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 11:30,8676,8692,8703,8672,18362,1600126200,0.36,0.2,17.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 13:35,8686,8680,8688,8672,12480,1080393600,0.18,-0.14,-12.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 13:40,8679,8669,8683,8666,8382,725629740,0.2,-0.13,-11.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 13:45,8669,8663,8675,8661,7772,672822040,0.16,-0.07,-6.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 13:50,8664,8662,8664,8654,7043,609712510,0.12,-0.01,-1.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 13:55,8662,8675,8676,8660,5848,512124160,0.18,0.15,13.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:00,8674,8673,8675,8664,4562,394977960,0.13,-0.02,-2.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:05,8673,8684,8688,8669,9325,807358500,0.22,0.13,11.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:10,8684,8687,8690,8680,5546,480172680,0.12,0.03,3.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:15,8686,8687,8693,8682,6850,599198630,0.13,0.0,0.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:20,8686,8674,8687,8666,7708,667435720,0.24,-0.15,-13.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:25,8673,8668,8680,8663,5841,505772190,0.2,-0.07,-6.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:30,8667,8673,8674,8660,5144,445418960,0.16,0.06,5.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:35,8673,8665,8675,8662,4792,414939280,0.15,-0.09,-8.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:40,8665,8614,8668,8613,19540,1685412720,0.63,-0.59,-51.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:45,8615,8587,8623,8577,33513,2880882510,0.53,-0.31,-27.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:50,8588,8605,8613,8575,18810,1620926390,0.44,0.21,18.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 14:55,8605,8604,8615,8602,8880,761307290,0.15,-0.01,-1.0,0.0\n" +
                    "菜油309,OI309,2023-06-27 15:00,8604,8583,8604,8583,11549,992051570,0.24,-0.24,-21.0,0.0\n");
        }
        //
        {
            buffer_5.append("");
        }
    }


}
