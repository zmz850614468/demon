package com.lilanz.webrtcdemo.remove;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lilanz.webrtcdemo.R;
import com.lilanz.webrtcdemo.utils.WebRtcUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.EglBase;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class RemovePreviewActivity extends Activity implements View.OnClickListener, SignClient.CallBack {

    private SurfaceViewRenderer localSurface;
    private SurfaceViewRenderer removeSurface;

    private Button bt;

    private PeerConnectionFactory peerConnectionFactory;
    private EglBase eglBase;
    private VideoSource localVideoSource;
    private VideoTrack localVideoTrack;
    private CameraVideoCapturer videoCapturer;

    private SignClient signClient;
    private int roomId = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_preview);

        initUI();
        signClient = new SignClient(this);
        openLocalCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_enter_room:
                createAndJoinRoom(roomId);
                break;
        }
    }

    // 打开本地视频预览
    private void openLocalCamera() {
        WebRtcUtil.iniWebRtc(this);
        eglBase = EglBase.create();
        peerConnectionFactory = WebRtcUtil.getPeerConnectionFaction(eglBase.getEglBaseContext());

        localSurface.init(eglBase.getEglBaseContext(), null);
        localSurface.setMirror(true);

        videoCapturer = WebRtcUtil.getVideoCapture(this, true);
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("helper", eglBase.getEglBaseContext());

        localVideoSource = peerConnectionFactory.createVideoSource(false);
        localVideoTrack = peerConnectionFactory.createVideoTrack("video", localVideoSource);
        videoCapturer.initialize(surfaceTextureHelper, this, localVideoSource.getCapturerObserver());
        videoCapturer.startCapture(960, 1280, 30);

        localVideoTrack.setEnabled(true);
        localVideoTrack.addSink(localSurface);
    }

    private void createAndJoinRoom(int roomId) {
        try {
            JSONObject message = new JSONObject();
            message.put("room", roomId);
            signClient.sendMessage("createAndJoinRoom", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void closelocalSurface() {
        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void switchCamera() {
        if (videoCapturer != null) {
            videoCapturer.switchCamera(null);
        }
    }

    private String TAG = "RemovePreviewActivity";

    @Override
    public void onRoomCreate(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("id");
            int room = jsonObject.getInt("room");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onRoomCreate: " + jsonObject);
    }

    @Override
    public void onRemoveJoin(JSONObject jsonObject) {
        Log.d(TAG, "onRemoveJoin: " + jsonObject);
    }

    @Override
    public void onSelfJoin(JSONObject jsonObject) {
        Log.d(TAG, "onSelfJoin: " + jsonObject);
    }

    @Override
    public void onExit(JSONObject jsonObject) {
        Log.d(TAG, "onExit: " + jsonObject);
    }

    @Override
    public void onOffer(JSONObject jsonObject) {
        Log.d(TAG, "onOffer: " + jsonObject);
    }

    @Override
    public void onAnswer(JSONObject jsonObject) {
        Log.d(TAG, "onAnswer: " + jsonObject);
    }

    @Override
    public void onCandidate(JSONObject jsonObject) {
        Log.d(TAG, "onCandidate: " + jsonObject);
    }

    private void initUI() {
        localSurface = findViewById(R.id.local_surface);
        removeSurface = findViewById(R.id.remove_surface);

        bt = findViewById(R.id.bt_enter_room);
        bt.setOnClickListener(this);
    }
}
