package com.demon.tool.controls;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

/**
 * 原生文字转语音控制类
 */
public class TTSControl implements TextToSpeech.OnInitListener {

    private static TTSControl instance;

    public static TTSControl getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new TTSControl(context);
                }
            }
        }
        return instance;
    }

    private Context context;
    private TextToSpeech engine;

    private TTSControl(Context context) {
        this.context = context;
        engine = new TextToSpeech(context, this::onInit);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            //默认设定语言为中文，原生的android貌似不支持中文。
            int result = engine.setLanguage(Locale.CHINESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                showToast("不支持中文语音");
            } else {
                engine.setLanguage(Locale.US);
            }
        }
    }

    /**
     * 播放文字声音
     *
     * @param voice
     */
    public void playVoice(String voice) {
        engine.speak(voice, TextToSpeech.QUEUE_FLUSH, null, "play_voice");
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
