package com.demo.fightdata.util;

import com.demo.fightdata.bean.TypeBean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

//    public static void readTxt(String filePath) throws Exception {
////        String filePath = "D:/aa.txt";
//        FileInputStream fin = new FileInputStream(filePath);
//        InputStreamReader reader = new InputStreamReader(fin);
//        BufferedReader buffReader = new BufferedReader(reader);
//        String strTmp = "";
//        while ((strTmp = buffReader.readLine()) != null) {
//            System.out.println(strTmp);
//        }
//        buffReader.close();
//    }

    /**
     * 解析txt格式,历史数据
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static List<TypeBean> parseTxt(String filePath) throws Exception {
        List<TypeBean> list = new ArrayList<>();
        TypeBean bean;
        FileInputStream fin = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        while ((strTmp = buffReader.readLine()) != null) {
            bean = new TypeBean();
            if (bean.setData(strTmp)) {
                list.add(bean);
            }
        }
        buffReader.close();

        BeanUtil.calAvg(list);
        return list;
    }

}
