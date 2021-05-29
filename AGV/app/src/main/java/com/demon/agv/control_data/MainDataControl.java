package com.demon.agv.control_data;

import com.demon.agv.activity.MainActivity;
import com.demon.agv.util.SerialPortUtil;

import java.util.List;

/**
 * 主界面的数据控制类
 */
public class MainDataControl {


    /**
     * 获取控制速度的命令
     *
     * @param baseSpeed     基础速度
     * @param directionList 方向数组
     * @param moveMode      运动模式
     * @return
     */
    public static String getSpeedOrder(int baseSpeed, List<String> directionList, int moveMode) {
        int leftSpeed = 0;
        int rightSpeed = 0;

        if (directionList.contains(MainActivity.DIRECTION_LEFT)) {
            // 原地左转
            if (!directionList.contains(MainActivity.DIRECTION_UP) && !directionList.contains(MainActivity.DIRECTION_DOWN)) {
                leftSpeed = -baseSpeed / 2;
                rightSpeed = leftSpeed / 2;
                // 左前方移动
            } else if (directionList.contains(MainActivity.DIRECTION_UP)) {
                leftSpeed = baseSpeed / 2;
                rightSpeed = baseSpeed;
                // 左后方移动
            } else if (directionList.contains(MainActivity.DIRECTION_DOWN)) {
                leftSpeed = -baseSpeed / 2;
                rightSpeed = -baseSpeed;
            }
        } else if (directionList.contains(MainActivity.DIRECTION_RIGHT)) {
            // 原地右转
            if (!directionList.contains(MainActivity.DIRECTION_UP) && !directionList.contains(MainActivity.DIRECTION_DOWN)) {
                leftSpeed = baseSpeed / 2;
                rightSpeed = -leftSpeed / 2;
                // 右前方移动
            } else if (directionList.contains(MainActivity.DIRECTION_UP)) {
                leftSpeed = baseSpeed;
                rightSpeed = baseSpeed / 2;
                // 右前方移动
            } else if (directionList.contains(MainActivity.DIRECTION_DOWN)) {
                leftSpeed = -baseSpeed;
                rightSpeed = -baseSpeed / 2;
            }
        } else {
            // 前进
            if (directionList.contains(MainActivity.DIRECTION_UP)) {
                leftSpeed = baseSpeed;
                rightSpeed = baseSpeed;
                // 后退
            } else if (directionList.contains(MainActivity.DIRECTION_DOWN)) {
                leftSpeed = -baseSpeed;
                rightSpeed = -baseSpeed;
            }
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("AAAF 11");
        String data = String.format("%04x", leftSpeed);
        buffer.append(data.length() > 4 ? data.substring(data.length() - 4) : data);
        data = String.format("%04x", rightSpeed);
        buffer.append(data.length() > 4 ? data.substring(data.length() - 4) : data);
        buffer.append(String.format("%02x", moveMode));
        buffer.append(SerialPortUtil.verticalCheck(buffer.toString()));
        return buffer.toString().trim();
    }

    /**
     * 获取运动的方向
     *
     * @param directionList 方向数组
     * @return
     */
    public static String getDirection(List<String> directionList) {
        String direction = "stop";
        if (directionList.contains(MainActivity.DIRECTION_LEFT)) {
            // 原地左转
            if (!directionList.contains(MainActivity.DIRECTION_UP) && !directionList.contains(MainActivity.DIRECTION_DOWN)) {
                direction = "originLeft";
                // 左前方移动
            } else if (directionList.contains(MainActivity.DIRECTION_UP)) {
                direction = "left";
                // 左后方移动
            } else if (directionList.contains(MainActivity.DIRECTION_DOWN)) {
                direction = "";
            }
        } else if (directionList.contains(MainActivity.DIRECTION_RIGHT)) {
            // 原地右转
            if (!directionList.contains(MainActivity.DIRECTION_UP) && !directionList.contains(MainActivity.DIRECTION_DOWN)) {
                direction = "originRight";
                // 右前方移动
            } else if (directionList.contains(MainActivity.DIRECTION_UP)) {
                direction = "right";
                // 右后方移动
            } else if (directionList.contains(MainActivity.DIRECTION_DOWN)) {
                direction = "";
            }
        } else {
            // 前进
            if (directionList.contains(MainActivity.DIRECTION_UP)) {
                direction = "front";
                // 后退
            } else if (directionList.contains(MainActivity.DIRECTION_DOWN)) {
                direction = "back";
            }
        }

        return direction;
    }
}
