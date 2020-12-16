package com.lilanz.wificonnect.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.adapters.SongAdapter;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.beans.SongBean;
import com.lilanz.wificonnect.beans.StopBean;
import com.lilanz.wificonnect.beans.VoiceBean;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.controls.MediaControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.dialogs.TimerDialog;
import com.lilanz.wificonnect.listeners.MsgCallbackListener;
import com.lilanz.wificonnect.threads.WifiService;
import com.lilanz.wificonnect.utils.StringUtil;
import com.lilanz.wificonnect.views.VerticalSeekBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 客户端：音乐列表界面
 */
public class MusicActivity extends Activity {

    @BindView(R.id.iv_player)
    ImageView ivPlayer;

    @BindView(R.id.rv_song)
    protected RecyclerView recycler;
    private SongAdapter songAdapter;

    private List<SongBean> beanList;
    private WifiService wifiService;

    private boolean canFreshMusic = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);

        initVoiceUI();
        updatePlayStatus();
        initService();
        initAdapter();
        updatePlayMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.iv_refresh)
    public void onRefreshClicked() {    // 更新音乐列表
        if (!canFreshMusic) {
            showToast("10秒钟内，只能刷新一次");
            return;
        }

        canFreshMusic = false;
        showToast("更新音乐列表");
        if (!beanList.isEmpty()) {  // 清空播放列表
            DBControl.deleteAll(this, SongBean.class, beanList);
            beanList.clear();
            songAdapter.notifyDataSetChanged();
        }
        if (wifiService != null) {
            wifiService.sendMsg(new MsgBean(MsgBean.UPDATE_MUSIC, "更新音乐").toString());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canFreshMusic = true;
            }
        }, 10000);
    }

    @OnClick(R.id.iv_player)
    public void onPlayClicked(View v) {   // 暂停或播放音乐
        if (v.getId() == R.id.iv_player) {
            AppDataControl.isPlaying = !AppDataControl.isPlaying;
            if (wifiService != null) {
                // 如果没有在播放的歌曲，默认选择第一首歌曲
                if (StringUtil.isEmpty(AppDataControl.playingPath) && !beanList.isEmpty()) {
                    AppDataControl.playingPath = beanList.get(0).path;
                    songAdapter.notifyDataSetChanged();
                }
                wifiService.sendMsg(new MsgBean(MsgBean.MUSIC_PAUSE_OR_START, AppDataControl.playingPath).toString());
            }
            updatePlayStatus();
        }
    }

    private void initAdapter() {
        beanList = DBControl.quaryAll(this, SongBean.class);

        songAdapter = new SongAdapter(this, beanList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(songAdapter);
        recycler.scrollToPosition(songAdapter.getPlayingIndex());

        songAdapter.setListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SongBean bean) {
                if (wifiService != null) {  // 点击播放歌曲
                    AppDataControl.isPlaying = true;
                    AppDataControl.playingPath = bean.path;
                    updatePlayStatus();
                    wifiService.sendMsg(new MsgBean(MsgBean.PLAY_MUSIC, bean.path).toString());
                }
            }
        });
    }

    private void initService() {
        Intent intent = new Intent(this, WifiService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wifiService = ((WifiService.WifiBind) service).getService();
            wifiService.addMsgCallBackListener(callbackListener);
            // 获取音乐音量
            wifiService.sendMsg(new MsgBean(MsgBean.MUSIC_GET_VOICE_INFO, "获取音乐音量").toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            wifiService = null;
        }
    };

    /**
     * 消息回调监听对象
     */
    private MsgCallbackListener callbackListener = new MsgCallbackListener() {
        @Override
        public void onMusicUpdate(SongBean songBean) {
            super.onMusicUpdate(songBean);
            // 更新歌曲列表
            beanList.add(songBean);
            songBean.id = beanList.size();
            if (songBean.isLastOne) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        songAdapter.notifyDataSetChanged();
                    }
                });
                DBControl.createOrUpdateList(MusicActivity.this, SongBean.class, beanList);
            }
        }

        @Override
        public void onPlayNextMusic() {
            super.onPlayNextMusic();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updatePlayStatus();
                    songAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private void updatePlayStatus() {
        if (AppDataControl.isPlaying) {
            Glide.with(this).load(R.mipmap.play_music_gif).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivPlayer);
        } else {
            ivPlayer.setImageResource(R.mipmap.play_music_gif);
        }
    }

    /**
     * 音乐播放模式
     */
    private void updatePlayMode() {
        switch (AppDataControl.playMode) {
            case MediaControl.PLAY_LIST:
                rgPlayMode.check(R.id.rb_play_list);
                ivPlayMode.setImageResource(R.mipmap.play_list);
                break;
            case MediaControl.PLAY_ONLY_ONE:
                rgPlayMode.check(R.id.rb_play_one);
                ivPlayMode.setImageResource(R.mipmap.play_only_one);
                break;
            case MediaControl.PLAY_RANDOM:
                rgPlayMode.check(R.id.rb_play_random);
                ivPlayMode.setImageResource(R.mipmap.play_random);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (wifiService != null) {
            wifiService.removeCallbackListener(callbackListener);
            unbindService(connection);
            wifiService = null;
        }
        timerDialog.cancel();
        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    // =============================      音乐播放设置界面           ============================

    @BindView(R.id.layout_music_setting)
    ViewGroup layoutMusicSetting;
    @BindView(R.id.sb_voice)
    VerticalSeekBar voiceSeekBar;
    @BindView(R.id.rg_play_mode)
    RadioGroup rgPlayMode;
    @BindView(R.id.iv_play_mode)
    ImageView ivPlayMode;

    @OnClick({R.id.layout_music_setting, R.id.iv_voice, R.id.iv_play_mode, R.id.iv_timer})
    public void onMusicSettingClicked(View v) {
        switch (v.getId()) {
            case R.id.layout_music_setting:
                layoutMusicSetting.setVisibility(View.GONE);
                voiceSeekBar.setVisibility(View.GONE);
                rgPlayMode.setVisibility(View.GONE);
                break;
            case R.id.iv_voice:
                voiceSeekBar.setMax(AppDataControl.maxMusicVoice);
                voiceSeekBar.setProgress(AppDataControl.curMusicVoice);
                layoutMusicSetting.setVisibility(View.VISIBLE);
                voiceSeekBar.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_play_mode:
                updatePlayMode();
                layoutMusicSetting.setVisibility(View.VISIBLE);
                rgPlayMode.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_timer:
                timerDialog.updateUI();
                timerDialog.show();
                break;
        }
    }

    private void initVoiceUI() {
        initDialog();
        layoutMusicSetting.setVisibility(View.GONE);
        rgPlayMode.setVisibility(View.GONE);
        voiceSeekBar.setVisibility(View.GONE);

        voiceSeekBar.setOnDataUpdateListener(new VerticalSeekBar.OnDataUpdateListener() {
            @Override
            public void onDataUpdate(SeekBar seekBar) {
                if (wifiService != null) {
                    AppDataControl.curMusicVoice = seekBar.getProgress();
                    VoiceBean voiceBean = new VoiceBean(seekBar.getMax(), seekBar.getProgress());
                    MsgBean msgBean = new MsgBean(MsgBean.MUSIC_VOICE_INFO, voiceBean.toString());
                    wifiService.sendMsg(msgBean.toString());
                }
            }
        });

        rgPlayMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_play_list:
                        AppDataControl.playMode = MediaControl.PLAY_LIST;
                        ivPlayMode.setImageResource(R.mipmap.play_list);
                        break;
                    case R.id.rb_play_one:
                        AppDataControl.playMode = MediaControl.PLAY_ONLY_ONE;
                        ivPlayMode.setImageResource(R.mipmap.play_only_one);
                        break;
                    case R.id.rb_play_random:
                        AppDataControl.playMode = MediaControl.PLAY_RANDOM;
                        ivPlayMode.setImageResource(R.mipmap.play_random);
                        break;
                }

                if (wifiService != null) {
                    wifiService.sendMsg(new MsgBean(MsgBean.MUSIC_PLAY_MODE, AppDataControl.playMode + "").toString());
                }
            }
        });
    }

    private TimerDialog timerDialog;

    private void initDialog() {
        timerDialog = new TimerDialog(this, R.style.DialogStyleOne);
        timerDialog.show();
        timerDialog.hide();
        timerDialog.setListener(new TimerDialog.OnClickListener() {
            @Override
            public void onConfirm(int stopMode, int count) {
                if (wifiService != null) {
                    StopBean stopBean = new StopBean(stopMode, count);
                    MsgBean msgBean = new MsgBean(MsgBean.MUSIC_STOP_MODE, stopBean.toString());
                    wifiService.sendMsg(msgBean.toString());
                }
            }
        });
    }

}
