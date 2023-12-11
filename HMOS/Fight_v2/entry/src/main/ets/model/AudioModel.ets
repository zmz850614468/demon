import audio from '@ohos.multimedia.audio';

export default class AudioModel {
  // audioManager: audio.AudioManager

  constructor() {
    // this.audioManager = audio.getAudioManager()
    let audioRendererInfo = {
      content: audio.ContentType.CONTENT_TYPE_SONIFICATION,
      usage: audio.StreamUsage.STREAM_USAGE_MEDIA,
      rendererFlags: 0
    };

  }
}










