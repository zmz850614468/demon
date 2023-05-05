//package top.yujiangtao;
//
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.Java2DFrameConverter;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.Properties;
//
///**
// * Created by IntelliJ IDEA.
// *
// * @author yujan
// * Date 2019/4/23/0023
// * Time 21:59
// **/
//public class ConvertToImage {
//
//    /**
//     * 加载配置文件
//     */
//    private static final Properties CONFIG;
//
//    static {
//        CONFIG = new Properties();
//        try {
//            CONFIG.load(ConvertToImage.class.getClassLoader().getResourceAsStream("convertConfig"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 视频路径
//     */
//    private static final String VIDEO_PATH = CONFIG.getProperty("video.path");
//
//    /**
//     * 图片存放路径
//     */
//    private static final String IMAGE_SAVE_PATH = CONFIG.getProperty("image.save.path");
//
//    public static void videoToFramers(String videoName) {
//        // 标记
//        long flag = 0;
//        // 采集视频
//        try (FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(VIDEO_PATH + videoName)) {
//            // 开始采集
//            fFmpegFrameGrabber.start();
//
//            int lengthInVideoFrames = fFmpegFrameGrabber.getLengthInVideoFrames();
//            BufferedImage bufferedImage;
//            while (flag <= lengthInVideoFrames) {
//                String imagePath = IMAGE_SAVE_PATH + "img_" + flag + ".png";
//                File writeImage = new File(imagePath);
//
//                // 取当前帧图像
//                Frame frameImg = fFmpegFrameGrabber.grabImage();
//                if (frameImg != null) {
//                    bufferedImage = frameToBufferedImage(frameImg);
//                    // 当前帧图像保存为图片
//                    ImageIO.write(bufferedImage, "png", writeImage);
//                }
//                flag++;
//            }
//            // 采集结束
//            fFmpegFrameGrabber.stop();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static BufferedImage frameToBufferedImage(Frame frameImg) {
//        Java2DFrameConverter converter = new Java2DFrameConverter();
//        return converter.getBufferedImage(frameImg);
//    }
//}
