package android_keyboard_listen;

import com.lilanz.tooldemo.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liang on 2017/1/3.
 */
public class KeyBoard implements Runnable {
    static {
//        System.loadLibrary("mykeyboard");
        System.loadLibrary("local_serial_port");
    }

    public native void Start_KeyBoard(String dev);

    public native String Get_Input();

    public native void Start_KeyBoard3(String dev);

    public native String Get_Input3();

    public native void Start_KeyBoard4(String dev);

    public native String Get_Input4();

    public String device;


    //    private static KeyBoard mInstance;
    private static Map<String, KeyBoard> keyBoardList = new HashMap<>();
    private static List<String> inputDeviceList = new ArrayList<>();

    public static synchronized KeyBoard getInstance(String dev, KeyBoardCallBack mcb) {
        if (!inputDeviceList.contains(dev)) {
            inputDeviceList.add(dev);
//            Collections.sort(inputDeviceList);
        }

        KeyBoard mInstance = null;
        if (!keyBoardList.containsKey(dev)) {
            mInstance = new KeyBoard(dev, mcb);
//            mInstance.device = dev;
            new Thread(mInstance).start();
            keyBoardList.put(dev, mInstance);
        }
        mInstance = keyBoardList.get(dev);
        mInstance.setmKeyBoardCallBack(mcb);

        return mInstance;

//        if (mInstance == null) {
//            mInstance = new KeyBoard(dev, mcb);
//            new Thread(mInstance).start();
//        }
//        mInstance.setmKeyBoardCallBack(mcb);
//        return mInstance;
    }

    /**
     *
     */
    public void clearListener() {
        keyBoardList.clear();
    }

    public KeyBoard(String dev, KeyBoardCallBack mcb) {
        device = dev;
        File device = new File(dev);
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec("su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
                        //+ "setenforce 0\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw new SecurityException();
            }
        }

        mKeyBoardCallBack = mcb;
        for (int i = 0; i < inputDeviceList.size(); i++) {
            if (dev != null && dev.equals(inputDeviceList.get(i))) {
                switch (i) {
                    case 0:
                        Start_KeyBoard(dev);
                        break;
                    case 1:
                        Start_KeyBoard3(dev);
                        break;
                    case 2:
                        Start_KeyBoard4(dev);
                        break;
                    default:
                        Start_KeyBoard(dev);
                        break;
                }
            }
        }
//        if (dev.equals("/dev/input/event2")) {
//            Start_KeyBoard(dev);
//        } else if (dev.equals("/dev/input/event3")) {
//            Start_KeyBoard3(dev);
//        } else if (dev.equals("/dev/input/event4")) {
//            Start_KeyBoard4(dev);
//        }
    }

    public void setmKeyBoardCallBack(KeyBoardCallBack callBack) {
        this.mKeyBoardCallBack = callBack;
    }

    /**
     * 关闭对应线程
     *
     * @param device
     */
//    public static void close(String device) {
//        if (keyBoardList.containsKey(device)) {
//            keyBoardList.remove(device).close();
//        }
//        if (inputDeviceList.contains(device)) {
//            inputDeviceList.remove(device);
//        }
//    }

    private boolean isContinue = true;

    @Override
    public void run() {
        while (isContinue) {
            String str = "";

            for (int i = 0; i < inputDeviceList.size(); i++) {
                if (device != null && device.equals(inputDeviceList.get(i))) {
                    switch (i) {
                        case 0:
                            str = Get_Input();
                            break;
                        case 1:
                            str = Get_Input3();
                            break;
                        case 2:
                            str = Get_Input4();
                            break;
                        default:
                            str = Get_Input();
                            break;
                    }
                }
            }
//            if ("/dev/input/event2".equals(device)) {
//                str = Get_Input();
//            } else if ("/dev/input/event3".equals(device)) {
//                str = Get_Input3();
//            } else if ("/dev/input/event4".equals(device)) {
//                str = Get_Input4();
//            }
            if (str.equalsIgnoreCase("null") || StringUtil.isEmpty(str)) {

            } else if (mKeyBoardCallBack != null) {
                mKeyBoardCallBack.StringBack(device, str);
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        isContinue = false;
        mKeyBoardCallBack = null;
    }

    public interface KeyBoardCallBack {
        public void StringBack(String dev, String code);
    }

    public KeyBoardCallBack mKeyBoardCallBack = null;
}
