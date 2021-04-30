package com.demon.opencvbase.control_ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.opencvbase.R;
import com.demon.opencvbase.activity.OpencvActivity;
import com.demon.opencvbase.adapter.OperateAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OperateUIControl {

    public static final String GRAY = "灰度处理";
    public static final String LUT = "LUP处理";               // 查表处理像素
    public static final String CONVERT_TO = "饱和度和亮度";
    public static final String ERODE = "侵蚀";                // 亮度颜色范围变小
    public static final String DILATE = "扩张";               // 亮度颜色范围变大
    public static final String THRESHOLD = "二值化操作";       // 图像二值处理
    public static final String GAUSSIAN_BLUR = "高斯模糊";
    public static final String IN_RANGE = "inRange操作";
    public static final String BLUR = "归一化模糊";
    public static final String MEDIA_BLUR = "中值模糊";

    public static final String AND = "bitwise_and";           // 图片位图操作
    public static final String OR = "bitwise_or";
    public static final String XOR = "bitwise_xor";
    public static final String NOT = "bitwise_not";

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
    private Bitmap lastPreBitmap;
    private Bitmap preBitmap;

    private int resId = R.drawable.test;
    private String operateType;

    public OperateUIControl(Activity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initUI();
    }

    public void resetBitmap() {
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
            }
        }

//        layoutControl.setVisibility(View.VISIBLE);
//        btOk.setVisibility(View.VISIBLE);
        operateType = operate;
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
        }
    }

    /**
     * 隐藏所有控制板
     */
    private void hideControls() {
//        layoutControl.setVisibility(View.GONE);
        layoutLut.setVisibility(View.GONE);
        layoutConvertTo.setVisibility(View.GONE);
        layoutThreshold.setVisibility(View.GONE);
        layoutBlur.setVisibility(View.GONE);
        layoutMediaBlur.setVisibility(View.GONE);
        layoutGaussBlur.setVisibility(View.GONE);
        layoutInRange.setVisibility(View.GONE);
//        btOk.setVisibility(View.GONE);
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
