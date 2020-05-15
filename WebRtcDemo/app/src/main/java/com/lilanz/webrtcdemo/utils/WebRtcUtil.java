package com.lilanz.webrtcdemo.utils;

import android.content.Context;

import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;

import java.util.List;

public class WebRtcUtil {

    /**
     * 初始化 PeerConnectionFactory 类,在使用前必须初始化
     *
     * @param context
     */
    public static void iniWebRtc(Context context) {
        PeerConnectionFactory.InitializationOptions options = PeerConnectionFactory.InitializationOptions.builder(context.getApplicationContext())
                .createInitializationOptions();
        PeerConnectionFactory.initialize(options);
    }

    /**
     * 获取相机捕捉器
     */
    public static CameraVideoCapturer getVideoCapture(Context context, boolean isFront) {
        CameraEnumerator enumerator = null;
        if (Camera2Enumerator.isSupported(context)) {
            enumerator = new Camera2Enumerator(context);
        } else {
            enumerator = new Camera1Enumerator(false);
        }

        String[] deviceNames = enumerator.getDeviceNames();
        // 优先查找前置摄像头
        if (isFront) {
            for (String name : deviceNames) {
                if (enumerator.isFrontFacing(name)) {
                    CameraVideoCapturer videoCapturer = enumerator.createCapturer(name, null);
                    if (videoCapturer != null) {
                        return videoCapturer;
                    }
                }
            }
        } else {
            // 没有前置摄像头，查找是否有其他摄像头
            for (String deviceName : deviceNames) {
                if (!enumerator.isFrontFacing(deviceName)) {
                    CameraVideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                    if (videoCapturer != null) {
                        return videoCapturer;
                    }
                }
            }
        }

        return null;
    }


    /**
     * 创建PeerConnection的配置类
     *
     * @param iceServers
     * @return
     */
    public static PeerConnection.RTCConfiguration createPeerConnection(List<PeerConnection.IceServer> iceServers) {
        PeerConnection.RTCConfiguration rtcConfiguration = new PeerConnection.RTCConfiguration(iceServers);
        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfiguration.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfiguration.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfiguration.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfiguration.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        rtcConfiguration.keyType = PeerConnection.KeyType.ECDSA;
        // Enable DTLS for normal calls and disable for loopback calls.
        rtcConfiguration.enableDtlsSrtp = true;
        rtcConfiguration.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN;

        return rtcConfiguration;
    }

    /**
     * 获取PeerConnectionFactory类
     *
     * @param eglBaseContext
     * @return
     */
    public static PeerConnectionFactory getPeerConnectionFaction(EglBase.Context eglBaseContext) {
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory encoderFactory = new DefaultVideoEncoderFactory(eglBaseContext, true, true);
        DefaultVideoDecoderFactory decoderFactory = new DefaultVideoDecoderFactory(eglBaseContext);
        return PeerConnectionFactory.builder()
                .setOptions(options)
//                .setAudioDeviceModule()
                .setVideoEncoderFactory(encoderFactory)
                .setVideoDecoderFactory(decoderFactory)
                .createPeerConnectionFactory();
    }

}
