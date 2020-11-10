package com.example.timeup.controls;

import android.content.Context;
import android.os.Environment;

import com.example.timeup.beans.TypeBean;
import com.example.timeup.daos.BeanDao;
import com.example.timeup.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class TypeDBControl {

    public static void createOrUpdate(Context context, TypeBean bean) {
        BeanDao dao = BeanDao.getDaoOperate(context, TypeBean.class);
        dao.update(bean);
    }

    public static void createOrUpdate(Context context, List<TypeBean> list) {
        for (TypeBean bean : list) {
            createOrUpdate(context, bean);
        }
    }

    public static void delete(Context context, TypeBean bean) {
        BeanDao dao = BeanDao.getDaoOperate(context, TypeBean.class);
        dao.delete(bean);
    }

    public static List<TypeBean> quaryAll(Context context) {
        BeanDao dao = BeanDao.getDaoOperate(context, TypeBean.class);
        return dao.queryAll();
    }

    /**
     * 保存数据信息到文件中去
     *
     * @param context
     */
    public static void saveDB2File(Context context) {
        String path = Environment.getExternalStorageDirectory().getPath();
        path += "/YuanYi/save";

        List<TypeBean> TypeList = quaryAll(context);
        for (TypeBean bean : TypeList) {
            bean.id = 0;
        }

        FileWriter writer = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, "clockDB.txt");
            file.createNewFile();

            writer = new FileWriter(file);
            writer.write(new Gson().toJson(TypeList));
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

    /**
     * 更新文件信息到数据库中去
     *
     * @param context
     */
    public static void updateDBFromFile(Context context) {
        String path = Environment.getExternalStorageDirectory().getPath();
        path += "/YuanYi/clockDB.txt";

        File file = new File(path);
        if (file.exists()) {
            long dbLastUpdate = SharePreferencesUtil.getDBLastUpdate(context);
            long fileLastModified = file.lastModified();
            if (dbLastUpdate == fileLastModified) {
                return;
            }

            try {
                char[] chArr = new char[2048];
                StringBuffer buffer = new StringBuffer();
                FileReader reader = new FileReader(file);
                while (true) {
                    int length = reader.read(chArr);
                    if (length > 0) {
                        buffer.append(chArr, 0, length);
                    } else {
                        break;
                    }
                }
                if (buffer.length() > 0) {
                    List<TypeBean> list = new Gson().fromJson(buffer.toString(), new TypeToken<List<TypeBean>>() {
                    }.getType());

                    List<TypeBean> oldList = quaryAll(context);
                    for (TypeBean bean : list) {
                        boolean isExit = false;
                        for (TypeBean oldBean : oldList) {
                            if (bean.name.equals(oldBean.name)) {
                                isExit = true;
                                break;
                            }
                        }
                        if (!isExit) {
                            createOrUpdate(context, bean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            SharePreferencesUtil.saveDBLastUpdate(context, fileLastModified);
        }
    }

}
