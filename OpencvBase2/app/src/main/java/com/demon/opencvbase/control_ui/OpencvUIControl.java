package com.demon.opencvbase.control_ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.demon.opencvbase.R;
import com.demon.opencvbase.activity.OpencvActivity;
import com.demon.opencvbase.adapter.OperateAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * opencvActivity 的界面控制类
 */
public class OpencvUIControl {

    public static final String GRAY = "灰度处理";
    public static final String LUT = "LUP处理";               // 查表处理像素
    public static final String CONVERT_TO = "饱和度和亮度";
    public static final String ERODE = "侵蚀";                // 亮度颜色范围变小
    public static final String DILATE = "扩张";               // 亮度颜色范围变大
    public static final String THRESHOLD = "二值化操作";       // 图像二值处理
    public static final String GAUSSIAN_BLUR = "高斯模糊";
    public static final String IN_RANGE = "inRange操作";      // 图片转HSV，通过HSV值二值化图片
    public static final String BLUR = "归一化模糊";
    public static final String MEDIA_BLUR = "中值模糊";

    public static final String AND = "bitwise_and";           // 图片位图操作：原图片和二值化图片操作
    public static final String OR = "bitwise_or";
    public static final String XOR = "bitwise_xor";
    public static final String NOT = "bitwise_not";

    public static final String CANNY = "canny边缘检测";         // 一下四个操作，都先要处理边缘检测
    public static final String LINE = "直线检测";
    public static final String CIRCLE = "圆形检测";
    public static final String MATCH = "模板匹配";
    public static final String CONTOURS = "轮廓查找和绘制";

    @BindView(R.id.layout_control)
    ViewGroup layoutControl;
    @BindView(R.id.bt_ok)
    Button btOk;

    @BindView(R.id.iv_last_original)
    ImageView ivOriginal;
    @BindView(R.id.iv_last_preview)
    ImageView ivLastPreview;
    @BindView(R.id.iv_preview)
    ImageView ivPreview;

    @BindView(R.id.rv_operate)
    protected RecyclerView recycler;
    private OperateAdapter operateAdapter;
    private List<String> operateList;

    private Activity activity;
    private Bitmap originalBitmap;
    private Bitmap templateBitmap;
    private Bitmap lastPreBitmap;
    private Bitmap preBitmap;

//    private int resId = R.drawable.line_13;
    private int resId = R.drawable.test_2;

    public OpencvUIControl(Activity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initUI();
    }

    public void resetBitmap() {
        templateBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.temp);
        originalBitmap = BitmapFactory.decodeResource(activity.getResources(), resId);
        preBitmap = originalBitmap;
        lastPreBitmap = originalBitmap;
        ivOriginal.setImageBitmap(originalBitmap);
        ivLastPreview.setImageBitmap(lastPreBitmap);
        ivPreview.setImageBitmap(preBitmap);
        operateAdapter.setSelectIndex(-1);
        operateAdapter.notifyDataSetChanged();
        hideControls();
    }

    private void initAdapter() {
        operateList = new ArrayList<>();
        operateList.add(GRAY);
        operateList.add(CONVERT_TO);
        operateList.add(ERODE);
        operateList.add(DILATE);
        operateList.add(THRESHOLD);
        operateList.add(IN_RANGE);
        operateList.add(BLUR);
        operateList.add(MEDIA_BLUR);
        operateList.add(GAUSSIAN_BLUR);
        operateList.add(LUT);
        operateList.add(AND);
        operateList.add(OR);
        operateList.add(XOR);
        operateList.add(NOT);
        operateList.add(CANNY);
        operateList.add(LINE);
        operateList.add(CIRCLE);
        operateList.add(MATCH);
        operateList.add(CONTOURS);

        operateAdapter = new OperateAdapter(activity, operateList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(operateAdapter);

        operateAdapter.setListener(new OperateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {
                operateClick(bean);
            }
        });
    }

    /**
     * 处理操作的点击处理
     *
     * @param operate
     */
    private void operateClick(String operate) {
        hideControls();
        if (activity instanceof OpencvActivity) {
            switch (operate) {
                case GRAY:
                    ((OpencvActivity) activity).grayOperate(lastPreBitmap);
                    return;
                case ERODE:
                    ((OpencvActivity) activity).erodeOperate(lastPreBitmap);
                    return;
                case DILATE:
                    ((OpencvActivity) activity).dilateOperate(lastPreBitmap);
                    return;
                case AND:
                    ((OpencvActivity) activity).bitwiseOperate(originalBitmap, lastPreBitmap, 1);
                    return;
                case OR:
                    ((OpencvActivity) activity).bitwiseOperate(originalBitmap, lastPreBitmap, 2);
                    return;
                case XOR:
                    ((OpencvActivity) activity).bitwiseOperate(originalBitmap, lastPreBitmap, 3);
                    return;
                case NOT:
                    ((OpencvActivity) activity).bitwiseOperate(originalBitmap, lastPreBitmap, 4);
                    return;
                case CONTOURS:
                    ((OpencvActivity) activity).contoursOperate(lastPreBitmap);
                    break;
            }
        }

        switch (operate) {
            case LUT:
                layoutLut.setVisibility(View.VISIBLE);
                sbLut.setProgress(1);
                break;
            case CONVERT_TO:
                layoutConvertTo.setVisibility(View.VISIBLE);
                sbBaoHD.setProgress(10);
                sbLiangD.setProgress(100);
                break;
            case THRESHOLD:
                layoutThreshold.setVisibility(View.VISIBLE);
                sbYuZ.setProgress(100);
                sbYuZType.setProgress(0);
                break;
            case BLUR:
                layoutBlur.setVisibility(View.VISIBLE);
                sbBlur.setProgress(0);
                break;
            case MEDIA_BLUR:
                layoutMediaBlur.setVisibility(View.VISIBLE);
                sbMediaBlur.setProgress(0);
                break;
            case GAUSSIAN_BLUR:
                layoutGaussBlur.setVisibility(View.VISIBLE);
                sbGaussBlur.setProgress(0);
                break;
            case IN_RANGE:
                layoutInRange.setVisibility(View.VISIBLE);
                break;
            case CANNY:
                layoutCanny.setVisibility(View.VISIBLE);
                break;
            case LINE:
                layoutHoughLine.setVisibility(View.VISIBLE);
                break;
            case CIRCLE:
                layoutHoughCircle.setVisibility(View.VISIBLE);
                break;
            case MATCH:
                layoutMatchTemplate.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 隐藏所有控制板
     */
    private void hideControls() {
        layoutLut.setVisibility(View.GONE);
        layoutConvertTo.setVisibility(View.GONE);
        layoutThreshold.setVisibility(View.GONE);
        layoutBlur.setVisibility(View.GONE);
        layoutMediaBlur.setVisibility(View.GONE);
        layoutGaussBlur.setVisibility(View.GONE);
        layoutInRange.setVisibility(View.GONE);
        layoutCanny.setVisibility(View.GONE);
        layoutHoughLine.setVisibility(View.GONE);
        layoutHoughCircle.setVisibility(View.GONE);
        layoutMatchTemplate.setVisibility(View.GONE);
    }

    /**
     * 设置预览图片
     *
     * @param bitmap
     */
    public void setPreview(Bitmap bitmap) {
        preBitmap = bitmap;
        ivPreview.setImageBitmap(bitmap);
    }

    public void updateBitmap() {
        this.lastPreBitmap = preBitmap;
        ivLastPreview.setImageBitmap(lastPreBitmap);
        ivPreview.setImageBitmap(lastPreBitmap);
    }

    private void initUI() {
        initLut();
        initConvetTo();
        initThreshold();
        initBlur();
        initMediaBlur();
        initGaussBlur();
        initInRange();
        initCanny();
        initHoughLine();
        initHoughCircle();
        initMatchTemplate();
        hideControls();
        initAdapter();
        resetBitmap();
    }

    @BindView(R.id.layout_lut)
    ViewGroup layoutLut;
    @BindView(R.id.tv_lut)
    TextView tvLut;
    @BindView(R.id.sb_lut)
    SeekBar sbLut;

    private void initLut() {
        sbLut.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLut.setText("LUT-" + progress + ":");
                if (activity instanceof OpencvActivity) {
                    ((OpencvActivity) activity).lutOperate(lastPreBitmap, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @BindView(R.id.layout_convert_to)
    ViewGroup layoutConvertTo;
    @BindView(R.id.tv_baohd)
    TextView tvBaoHD;
    @BindView(R.id.sb_baohd)
    SeekBar sbBaoHD;
    @BindView(R.id.tv_liangd)
    TextView tvLiangD;
    @BindView(R.id.sb_liangd)
    SeekBar sbLiangD;

    /**
     * 饱和度 和 亮度 界面初始化
     */
    private void initConvetTo() {
        sbBaoHD.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateConvertTo();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sbLiangD.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateConvertTo();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateConvertTo() {
        double alpha = sbBaoHD.getProgress() / 10.0f;
        int beta = sbLiangD.getProgress() - 100;
        tvBaoHD.setText(String.format("饱和度(%.1f):", alpha));
        tvLiangD.setText("亮度(" + beta + "):");
        if (activity instanceof OpencvActivity) {
            ((OpencvActivity) activity).convertToOperate(lastPreBitmap, alpha, beta);
        }
    }

    @BindView(R.id.layout_yuz)
    ViewGroup layoutThreshold;
    @BindView(R.id.tv_yuz)
    TextView tvYuZ;
    @BindView(R.id.sb_yuz)
    SeekBar sbYuZ;
    @BindView(R.id.tv_yuz_type)
    TextView tvYuZType;
    @BindView(R.id.sb_yuz_type)
    SeekBar sbYuZType;

    /**
     * 阈值界面初始化
     */
    private void initThreshold() {
        sbYuZ.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateThreshold();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbYuZType.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateThreshold();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateThreshold() {
        int threshold = sbYuZ.getProgress();
        int type = sbYuZType.getProgress();
        tvYuZ.setText(String.format("阈值(%d):", threshold));
        tvYuZType.setText(String.format("阈值类型(%d):", type));
        if (activity instanceof OpencvActivity) {
            ((OpencvActivity) activity).thresholdOperate(lastPreBitmap, threshold, type);
        }
    }

    @BindView(R.id.layout_blur)
    ViewGroup layoutBlur;
    @BindView(R.id.tv_blur)
    TextView tvBlur;
    @BindView(R.id.sb_blur)
    SeekBar sbBlur;

    /**
     * 归一化模糊处理
     */
    private void initBlur() {
        sbBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int kSize = progress * 2 + 1;
                tvBlur.setText(String.format("核大小(%d):", kSize));
                if (activity instanceof OpencvActivity) {
                    ((OpencvActivity) activity).blurOperate(lastPreBitmap, kSize);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @BindView(R.id.layout_media_blur)
    ViewGroup layoutMediaBlur;
    @BindView(R.id.tv_media_blur)
    TextView tvMediaBlur;
    @BindView(R.id.sb_media_blur)
    SeekBar sbMediaBlur;

    /**
     * 中值模糊处理
     */
    private void initMediaBlur() {
        sbMediaBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int kSize = progress * 2 + 1;
                tvMediaBlur.setText(String.format("核大小(%d):", kSize));
                if (activity instanceof OpencvActivity) {
                    ((OpencvActivity) activity).mediaBlurOperate(lastPreBitmap, kSize);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @BindView(R.id.layout_gauss_blur)
    ViewGroup layoutGaussBlur;
    @BindView(R.id.tv_gauss_blur)
    TextView tvGaussBlur;
    @BindView(R.id.sb_gauss_blur)
    SeekBar sbGaussBlur;

    /**
     * 高斯模糊处理
     */
    private void initGaussBlur() {
        sbGaussBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int kSize = progress * 2 + 1;
                tvGaussBlur.setText(String.format("核大小(%d):", kSize));
                if (activity instanceof OpencvActivity) {
                    ((OpencvActivity) activity).gaussBlurOperate(lastPreBitmap, kSize);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @BindView(R.id.layout_canny)
    ViewGroup layoutCanny;
    @BindView(R.id.tv_canny)
    TextView tvCanny;
    @BindView(R.id.sb_canny)
    SeekBar sbCanny;

    /**
     * 边缘查找
     */
    private void initCanny() {
        sbCanny.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvCanny.setText(String.format("最小阈值(%d):", progress));
                if (activity instanceof OpencvActivity) {
                    ((OpencvActivity) activity).cannyOperate(lastPreBitmap, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @BindView(R.id.cb_line)
    CheckBox cbLine;
    @BindView(R.id.layout_hough_line)
    ViewGroup layoutHoughLine;
    @BindView(R.id.tv_hough_line)
    TextView tvHoughLine;
    @BindView(R.id.sb_hough_line)
    SeekBar sbHoughLine;
    @BindView(R.id.tv_min_line)
    TextView tvMinLine;
    @BindView(R.id.sb_min_line)
    SeekBar sbMinLine;

    /**
     * 直线检测
     */
    private void initHoughLine() {
        sbHoughLine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvHoughLine.setText(String.format("阈值(%d):", progress));
                updateLine();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbMinLine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMinLine.setText(String.format("最小值(%d):", progress));
                updateLine();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        cbLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateLine();
            }
        });
    }

    private void updateLine() {
        if (activity instanceof OpencvActivity) {
            int type = cbLine.isChecked() ? 2 : 1;
            int threshold = sbHoughLine.getProgress();
            int min = sbMinLine.getProgress();

            ((OpencvActivity) activity).lineOperate(lastPreBitmap, threshold, min, type);
        }
    }

    @BindView(R.id.layout_hough_circle)
    ViewGroup layoutHoughCircle;
    @BindView(R.id.tv_max_circle)
    TextView tvMaxCircle;
    @BindView(R.id.sb_max_circle)
    SeekBar sbMaxCircle;
    @BindView(R.id.tv_min_circle)
    TextView tvMinCircle;
    @BindView(R.id.sb_min_circle)
    SeekBar sbMinCircle;

    /**
     * 圆形检测
     */
    private void initHoughCircle() {
        sbMaxCircle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMaxCircle.setText(String.format("最大值(%d):", progress));
                updateCircle(1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbMinCircle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMinCircle.setText(String.format("最小值(%d):", progress));
                updateCircle(-1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateCircle(int type) {
        if (activity instanceof OpencvActivity) {
            int min = sbMinCircle.getProgress();
            int max = sbMaxCircle.getProgress();
            if (type == -1 && max < min) {
                sbMaxCircle.setProgress(min);
            } else if (type == 1 && max < min) {
                sbMinCircle.setProgress(max);
            } else {
                ((OpencvActivity) activity).circleOperate(lastPreBitmap, min, max, originalBitmap);
            }
        }
    }

    @BindView(R.id.layout_match_template)
    ViewGroup layoutMatchTemplate;
    @BindView(R.id.tv_match_template)
    TextView tvMatchTemplate;
    @BindView(R.id.sb_match_template)
    SeekBar sbMatchTemplate;

    private void initMatchTemplate() {
        sbMatchTemplate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMatchTemplate.setText(String.format("匹配模式(%d):", progress));
                if (activity instanceof OpencvActivity) {
                    ((OpencvActivity) activity).templateOperate(lastPreBitmap, templateBitmap, progress, originalBitmap);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @BindView(R.id.layout_in_range)
    ViewGroup layoutInRange;
    @BindView(R.id.tv_r_min)
    TextView tvRmin;
    @BindView(R.id.tv_g_min)
    TextView tvGmin;
    @BindView(R.id.tv_b_min)
    TextView tvBmin;
    @BindView(R.id.tv_r_max)
    TextView tvRmax;
    @BindView(R.id.tv_g_max)
    TextView tvGmax;
    @BindView(R.id.tv_b_max)
    TextView tvBmax;
    @BindView(R.id.sb_in_range)
    SeekBar sbInRange;

    private TextView selectView = null;

    /**
     * inRange阈值处理
     */
    private void initInRange() {
        sbInRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateInRangeUI(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        onClicked(tvRmin);
    }

    private void updateInRangeUI(int progress) {
        if (selectView != null) {
            selectView.setText(progress + "");
            switch (selectView.getId()) {
                case R.id.tv_r_min:
                    int value = Integer.parseInt(tvRmax.getText().toString());
                    if (progress > value) {
                        tvRmax.setText(progress + "");
                    }
                    break;
                case R.id.tv_r_max:
                    value = Integer.parseInt(tvRmin.getText().toString());
                    if (progress < value) {
                        tvRmin.setText(progress + "");
                    }
                    break;
                case R.id.tv_g_min:
                    value = Integer.parseInt(tvGmax.getText().toString());
                    if (progress > value) {
                        tvGmax.setText(progress + "");
                    }
                    break;
                case R.id.tv_g_max:
                    value = Integer.parseInt(tvGmin.getText().toString());
                    if (progress < value) {
                        tvGmin.setText(progress + "");
                    }
                    break;
                case R.id.tv_b_min:
                    value = Integer.parseInt(tvBmax.getText().toString());
                    if (progress > value) {
                        tvBmax.setText(progress + "");
                    }
                    break;
                case R.id.tv_b_max:
                    value = Integer.parseInt(tvBmin.getText().toString());
                    if (progress < value) {
                        tvBmin.setText(progress + "");
                    }
                    break;
            }
            updateInRange();
        }
    }

    private void updateInRange() {
        int rMin = Integer.parseInt(tvRmin.getText().toString());
        int gMin = Integer.parseInt(tvGmin.getText().toString());
        int bMin = Integer.parseInt(tvBmin.getText().toString());
        int rMax = Integer.parseInt(tvRmax.getText().toString());
        int gMax = Integer.parseInt(tvGmax.getText().toString());
        int bMax = Integer.parseInt(tvBmax.getText().toString());

        if (activity instanceof OpencvActivity && lastPreBitmap != null) {
            ((OpencvActivity) activity).inRangeOperate(lastPreBitmap, rMin, gMin, bMin, rMax, gMax, bMax);
        }
    }

    @OnClick({R.id.tv_r_min, R.id.tv_g_min, R.id.tv_b_min, R.id.tv_r_max, R.id.tv_g_max, R.id.tv_b_max})
    public void onClicked(View v) {
        tvRmin.setBackgroundResource(R.drawable.shape_box_white);
        tvGmin.setBackgroundResource(R.drawable.shape_box_white);
        tvBmin.setBackgroundResource(R.drawable.shape_box_white);
        tvRmax.setBackgroundResource(R.drawable.shape_box_white);
        tvGmax.setBackgroundResource(R.drawable.shape_box_white);
        tvBmax.setBackgroundResource(R.drawable.shape_box_white);

        if (selectView == v) {
            int progress = Integer.parseInt(selectView.getText().toString());
            sbInRange.setProgress(progress + 1);
//            if (progress < 255) {
//                updateInRangeUI(progress + 1);
//            }
        } else {
            selectView = (TextView) v;
            sbInRange.setProgress(Integer.parseInt(selectView.getText().toString()));
        }
        selectView.setBackgroundResource(R.drawable.shape_box_red);
    }


    private void showLog(String msg) {
        Log.e("operate ui control", msg);
    }
}
