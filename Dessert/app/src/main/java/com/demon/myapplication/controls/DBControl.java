package com.demon.myapplication.controls;

import android.content.Context;
import android.os.Environment;


import com.demon.myapplication.Beans.MaterialBean;
import com.demon.myapplication.daos.BeanDao;
import com.demon.myapplication.utils.StringUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBControl {

    public static void createOrUpdate(Context context, MaterialBean bean) {
        BeanDao dao = BeanDao.getDaoOperate(context, MaterialBean.class);
        dao.update(bean);
    }

    public static void delete(Context context, MaterialBean bean) {
        BeanDao dao = BeanDao.getDaoOperate(context, MaterialBean.class);
        dao.delete(bean);
    }

    public static void deleteAll(Context context, List<MaterialBean> list) {
        BeanDao dao = BeanDao.getDaoOperate(context, MaterialBean.class);
        dao.deleteList(list);
    }


    public static List<MaterialBean> quaryByType(Context context, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);

        BeanDao dao = BeanDao.getDaoOperate(context, MaterialBean.class);
        return dao.queryWhereForList(map);
    }

    public static List<MaterialBean> quaryAll(Context context) {
        BeanDao dao = BeanDao.getDaoOperate(context, MaterialBean.class);
        return dao.queryAll();
    }

    public static void saveDB2File(Context context) {
        String path = Environment.getExternalStorageDirectory().getPath();
        path += "/YuanYi/save";

        List<MaterialBean> materialList = quaryAll(context);
        for (MaterialBean bean : materialList) {
            bean.id = 0;
        }

        FileWriter writer = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, "peiFangDB.txt");
            file.createNewFile();

            writer = new FileWriter(file);
            writer.write(new Gson().toJson(materialList));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
