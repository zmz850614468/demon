package com.lilanz.wificonnect.controls;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.lilanz.wificonnect.activitys.App;
import com.lilanz.wificonnect.beans.WakeUpBean;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;
import com.lilanz.wificonnect.utils.XunFeiUtil;

import java.io.InputStream;

/**
 * 讯飞语音控制类
 */
public class XunFeiVoiceControl {

    private Context context;

    private static XunFeiVoiceControl instance;

    public static XunFeiVoiceControl getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new XunFeiVoiceControl(context);
                }
            }
        }
        return instance;
    }

    private XunFeiVoiceControl(Context context) {
        this.context = context;
        // 初始化唤醒对象
        voiceWakeuper = VoiceWakeuper.createWakeuper(context, null);
        initOneShot();
    }

    private RecognizerDialog mIatDialog;

    /**
     * 讯飞语音转文字
     */
    public void voiceRecognizer(Context context, OnVoiceResult voiceResult) {

        if (mIatDialog == null) {
            // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
            // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
            mIatDialog = new RecognizerDialog(context, null);

            //以下为dialog设置听写参数
            //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
            mIatDialog.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
            mIatDialog.setParameter(SpeechConstant.SUBJECT, null);
            //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
            mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
            //此处engineType为“cloud”SpeechConstant.TYPE_LOCAL
//            mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
            mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置语音输入语言，zh_cn为简体中文
            mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            //设置结果返回语言
            mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
            // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
            //取值范围{1000～10000}
            mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");
            //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
            //自动停止录音，范围{0~10000}
            mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
            //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
            mIatDialog.setParameter(SpeechConstant.ASR_PTT, "1");

            //开始识别并设置监听器
            if (voiceResult != null) {
                mIatDialog.setListener(new RecognizerDialogListener() {
                    private StringBuffer buffer = new StringBuffer();

                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean b) {
                        // b :true :表示最后一个信息
                        buffer.append(XunFeiUtil.getVoiceResult(recognizerResult.getResultString()));
                        if (b) {
                            Log.e("TAG", "onResult: " + buffer.toString());
                            voiceResult.onResult(buffer.toString());
                            buffer.delete(0, buffer.length());
                        }
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        voiceResult.onError(speechError.getErrorDescription());
                    }
                });
            }
        }
        //显示听写对话框
        mIatDialog.show();
    }

    public interface OnVoiceResult {
        void onResult(String result);

        void onError(String error);
    }


    // ===================================   讯飞语音唤醒    ===========================================
    private VoiceWakeuper voiceWakeuper;
    // 本地语法构建路径
    private String grmPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/xunfei/grm";

    /**
     * 开启讯飞语音唤醒
     */
    public void startWakeUp() {
        //非空判断，防止因空指针使程序崩溃
        voiceWakeuper = VoiceWakeuper.getWakeuper();
        if (voiceWakeuper != null) {

            // 清空参数
            voiceWakeuper.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            int voiceSensitivity = SharePreferencesUtil.getVoiceSensitivity(context);
            voiceWakeuper.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + voiceSensitivity);
            // 设置唤醒模式
            voiceWakeuper.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            voiceWakeuper.setParameter(SpeechConstant.KEEP_ALIVE, "1");
            // 设置闭环优化网络模式
            voiceWakeuper.setParameter(SpeechConstant.IVW_NET_MODE, "0");
            // 设置唤醒资源路径
            voiceWakeuper.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            voiceWakeuper.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
            voiceWakeuper.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
            //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );
            // 启动唤醒
            /*	mIvw.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");*/

            voiceWakeuper.startListening(mWakeuperListener);
        } else {
            showToast("唤醒未初始化");
        }
    }

    /**
     * 关闭讯飞语音唤醒
     */
    public void stopWakeUp() {
        if (voiceWakeuper != null) {
            voiceWakeuper.stopListening();
            voiceWakeuper = null;
        }
    }

    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }

    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        private boolean status = false;

        @Override
        public void onResult(WakeuperResult result) {
            // 唤醒模式，的回调
            try {
                WakeUpBean wakeUpBean = new Gson().fromJson(result.getResultString(), WakeUpBean.class);
                if (wakeUpBean.id == 1) {
                    status = false;
                } else {
                    status = true;
                }

                showLog(wakeUpBean.toString());
            } catch (Exception e) {
                showLog("结果解析出错");
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SpeechError error) {
            showToast(error.getPlainDescription(true));
            oneShot();
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            // 命令识别结果
            if (SpeechEvent.EVENT_IVW_RESULT == eventType) {
                RecognizerResult reslut = ((RecognizerResult) obj.get(SpeechEvent.KEY_EVENT_IVW_RESULT));
                String result = XunFeiUtil.getVoiceResult(reslut.getResultString());
                showLog(result);
                if (onOneShotResult != null) {
                    onOneShotResult.onResult(status, result);
                }
            }
        }

        @Override
        public void onVolumeChanged(int volume) {

        }
    };

    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/5fc9e265.jet");
        return resPath;
    }

    // ===================================   讯飞语音唤醒 + 命令   ===========================================

    private SpeechRecognizer speechRecognizer;

    // 云端语法文件
    private String mCloudGrammar = null;
    // 云端语法id
    private String mCloudGrammarID;
    // 本地语法id
    private String mLocalGrammarID;
    // 本地语法文件
    private String mLocalGrammar = null;

    private void initOneShot() {
        speechRecognizer = SpeechRecognizer.createRecognizer(context, null);
        // 初始化语法文件
        mCloudGrammar = readFile(context, "wake_grammar_sample.abnf", "utf-8");
        mLocalGrammar = readFile(context, "wake.bnf", "utf-8");
        initGrammar();
    }

    public void oneShot() {
        // 非空判断，防止因空指针使程序崩溃
        voiceWakeuper = VoiceWakeuper.getWakeuper();
        if (voiceWakeuper != null) {

            final String resPath = ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/5fc9e265.jet");
            // 清空参数
            voiceWakeuper.setParameter(SpeechConstant.PARAMS, null);
            // 设置识别引擎
            voiceWakeuper.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置唤醒资源路径
            voiceWakeuper.setParameter(ResourceUtil.IVW_RES_PATH, resPath);
            /**
             * 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
             * 示例demo默认设置第一个唤醒词，建议开发者根据定制资源中唤醒词个数进行设置
             */
            int voiceSensitivity = SharePreferencesUtil.getVoiceSensitivity(context);
            voiceWakeuper.setParameter(SpeechConstant.IVW_THRESHOLD,
                    "0:" + voiceSensitivity +
                            ";1:" + voiceSensitivity +
                            ";2:" + voiceSensitivity);
            // 设置唤醒+识别模式
            voiceWakeuper.setParameter(SpeechConstant.IVW_SST, "oneshot");
            // 设置返回结果格式
            voiceWakeuper.setParameter(SpeechConstant.RESULT_TYPE, "json");
//
//				mIvw.setParameter(SpeechConstant.IVW_SHOT_WORD, "0");

            // 设置唤醒录音保存路径，保存最近一分钟的音频
            voiceWakeuper.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
            voiceWakeuper.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

//            if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
//                if (!TextUtils.isEmpty(mCloudGrammarID)) {
//                    // 设置云端识别使用的语法id
//                    mIvw.setParameter(SpeechConstant.CLOUD_GRAMMAR,
//                            mCloudGrammarID);
//                    mIvw.startListening(mWakeuperListener);
//                } else {
//                    showTip("请先构建语法");
//                }
//            } else {
            if (!TextUtils.isEmpty(mLocalGrammarID)) {
                // 设置本地识别资源
                voiceWakeuper.setParameter(ResourceUtil.ASR_RES_PATH,
                        getOnehotResourcePath());
                // 设置语法构建路径
                voiceWakeuper.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
                // 设置本地识别使用语法id
                voiceWakeuper.setParameter(SpeechConstant.LOCAL_GRAMMAR,
                        mLocalGrammarID);
                voiceWakeuper.startListening(mWakeuperListener);
            } else {
                showToast("请先构建语法");
            }
//            }

        } else {
            showToast("唤醒未初始化");
        }
    }

    // 获取识别资源路径
    private String getOnehotResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        // 识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context,
                ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
        return tempBuffer.toString();
    }

    private void initGrammar() {
        speechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        speechRecognizer.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        // 设置引擎类型
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置语法构建路径
        speechRecognizer.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
        // 设置资源路径
        speechRecognizer.setParameter(ResourceUtil.ASR_RES_PATH, getOnehotResourcePath());
        int ret = speechRecognizer.buildGrammar("bnf", mLocalGrammar, grammarListener);
        if (ret != ErrorCode.SUCCESS) {
            showToast("语法构建失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }

    GrammarListener grammarListener = new GrammarListener() {
        @Override
        public void onBuildFinish(String grammarId, SpeechError error) {
            if (error == null) {
//                if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
//                    mCloudGrammarID = grammarId;
//                } else {
                mLocalGrammarID = grammarId;
//                }
                showToast("语法构建成功：" + grammarId);
            } else {
                showToast("语法构建失败,错误码：" + error.getErrorCode() + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };

    /**
     * 读取asset目录下文件。
     *
     * @return content
     */
    public static String readFile(Context mContext, String file, String code) {
        int len = 0;
        byte[] buf = null;
        String result = "";
        try {
            InputStream in = mContext.getAssets().open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            result = new String(buf, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private OnOneShotResult onOneShotResult;

    public void setOnOneShotResult(OnOneShotResult onOneShotResult) {
        this.onOneShotResult = onOneShotResult;
    }

    public interface OnOneShotResult {
        void onResult(boolean status, String result);
    }


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("xunFei", msg);
        }
    }
}
