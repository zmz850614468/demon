package com.demon.myapplication.controls;

import android.content.Context;
import android.os.Environment;


import com.demon.myapplication.Beans.GroupBean;
import com.demon.myapplication.Beans.MaterialBean;
import com.demon.myapplication.daos.BeanDao;
import com.demon.myapplication.utils.SharePreferencesUtil;
import com.demon.myapplication.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBControl {

    public static List getDistinct(Context context, Class clazz, String columnName) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        return dao.getDistinctList(columnName);
    }

    public static void createOrUpdate(Context context, Class clazz, Object obj) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        dao.update(obj);
    }

    public static List quaryByColumn(Context context, Class clazz, Map<String, Object> map) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        return dao.queryWhereForList(map);
    }

    public static List quaryAll(Context context, Class clazz) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        return dao.queryAll();
    }


    public static void delete(Context context, Class clazz, Object obj) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        dao.delete(obj);
    }

    // =================================


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
        new Thread() {
            @Override
            public void run() {
                super.run();

                String path = Environment.getExternalStorageDirectory().getPath();
                path += "/YuanYi/save";

                List<MaterialBean> materialList = quaryAll(context, MaterialBean.class);
                List<GroupBean> groupBeanList = quaryAll(context, GroupBean.class);

                for (MaterialBean bean : materialList) {
                    bean.id = 0;
                    for (GroupBean groupBean : groupBeanList) {
                        if (bean.type.equals(groupBean.peiFangName)) {
                            bean.group = groupBean.groupName;
                            break;
                        }
                    }
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
        }.start();
    }

    /**
     * 更新文件信息到数据库中去
     *
     * @param context
     */
    public static void updateDBFromFile(Context context) {
        String path = Environment.getExternalStorageDirectory().getPath();
        path += "/YuanYi/peiFangDB.txt";

        File file = new File(path);
        if (file.exists() && file.canRead()) {
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
                    List<MaterialBean> list = new Gson().fromJson(buffer.toString(), new TypeToken<List<MaterialBean>>() {
                    }.getType());

                    // 保存分组信息
                    List<GroupBean> groupBeanList = DBControl.quaryAll(context, GroupBean.class);
                    List<GroupBean> newList = new ArrayList<>();
                    for (MaterialBean bean : list) {
                        boolean isExit = false;
                        for (GroupBean groupBean : groupBeanList) {
                            if (bean.type.endsWith(groupBean.peiFangName)) {
                                isExit = true;
                                break;
                            }
                        }
                        if (!isExit) {
                            boolean needSave = true;
                            for (GroupBean groupBean : newList) {
                                if (groupBean.peiFangName.equals(bean.type)) {
                                    needSave = false;
                                    break;
                                }
                            }
                            if (needSave) {
                                GroupBean newBean = new GroupBean();
                                newBean.groupName = bean.group;
                                if (StringUtil.isEmpty(newBean.groupName)) {
                                    newBean.groupName = "未分组";
                                }
                                newBean.peiFangName = bean.type;
                                newList.add(newBean);
                            }
                        }
                    }
                    for (GroupBean groupBean : newList) {
                        DBControl.createOrUpdate(context, GroupBean.class, groupBean);
                    }

                    // 保存配方详情
                    for (MaterialBean bean : list) {
                        for (GroupBean groupBean : newList) {
                            if (bean.type.equals(groupBean.peiFangName)) {
                                createOrUpdate(context, MaterialBean.class, bean);
                                break;
                            }
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
