import common from '@ohos.app.ability.common';
import media from '@ohos.multimedia.media';

export class AVPlayerModel {
  private
  private avPlayer
  private playerTimes = 1

  constructor() {
    this.initData()
  }

  async initData() {
    if (!this.avPlayer) {
      // 创建avPlayer实例对象
      this.avPlayer = await media.createAVPlayer();
      // 创建状态机变化回调函数
      this.setAVPlayerCallback();
    }
  }

  /**
   * 注册avplayer回调函数
   */
  private setAVPlayerCallback() {
    // seek操作结果回调函数
    this.avPlayer.on('seekDone', (seekDoneTime) => {
      console.error(`AVPlayer seek succeeded, seek time is ${seekDoneTime}`);
    })
    // error回调监听函数,当avPlayer在操作过程中出现错误时调用reset接口触发重置流程
    this.avPlayer.on('error', (err) => {
      console.error(`Invoke avPlayer failed, code is ${err.code}, message is ${err.message}`);
      this.avPlayer.reset(); // 调用reset重置资源，触发idle状态
    })
    // 状态机变化回调函数
    this.avPlayer.on('stateChange', async (state, reason) => {
      switch (state) {
        case 'idle': // 成功调用reset接口后触发该状态机上报
          console.error('AVPlayer state idle called.');
          if (this.playerTimes > 0) {
            this.avPlayer.fdSrc = this.fileDescriptor;
          }
          break;
        case 'initialized': // avplayer 设置播放源后触发该状态上报
          console.error('AVPlayerstate initialized called.');
          this.avPlayer.prepare().then(() => {
            console.error('AVPlayer prepare succeeded.');
          }, (err) => {
            console.error(`Invoke prepare failed, code is ${err.code}, message is ${err.message}`);
          });
          break;
        case 'prepared': // prepare调用成功后上报该状态机
          console.error('AVPlayer state prepared called.');
        // if (this.playerTimes > 1) {
        //   this.avPlayer.loop = true
        // }
          this.avPlayer.play(); // 调用播放接口开始播放
          break;
        case 'playing': // play成功调用后触发该状态机上报
          console.error('AVPlayer state playing called.');
          break;
        case 'paused': // pause成功调用后触发该状态机上报
          console.error('AVPlayer state paused called.');
        // this.avPlayer.play(); // 再次播放接口开始播放
          break;
        case 'completed': // 播放结束后触发该状态机上报
          this.playerTimes--
          this.avPlayer.stop(); //调用播放结束接口
          console.error('AVPlayer state completed called.');
          break;
        case 'stopped': // stop接口成功调用后触发该状态机上报
          console.error('AVPlayer state stopped called.');
          this.avPlayer.reset(); // 调用reset接口初始化avplayer状态
          break;
        case 'released':
          console.error('AVPlayer state released called.');
          break;
        default:
          console.error('AVPlayer state unknown called.');
          break;
      }
    })
  }

  fileDescriptor = null
  /**
   * 播放 rawfile 声音
   * @param context
   * @param rawName
   */
  async play(context, rawName: string, times?: number) {
    if (times) {
      this.playerTimes = times
    }
    this.fileDescriptor = await context.resourceManager.getRawFd(rawName);
    this.avPlayer.fdSrc = this.fileDescriptor;
  }

  /**
   * 获取当前 音乐播放 的状态
   * @returns
   */
  getPlayerState() {
    return this.avPlayer.state
  }
}