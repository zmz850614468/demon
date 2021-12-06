package com.demon.tool.zxingscan.scan1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;


public class CustomScannerView extends ZXingScannerView {
    public CustomScannerView(Context context) {
        super(context);
    }

    public CustomScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected IViewFinder createViewFinderView(Context context) {
        return new CustomViewFinder(context);
    }

    class CustomViewFinder extends ViewFinderView {
        private String maskText = "将二维码/条码放入框内，即可自动扫描";
        private Paint textPaint;

        public CustomViewFinder(Context context) {
            super(context);
            init();
        }

        private void init() {
            mBorderPaint.setColor(Color.WHITE);
            mBorderPaint.setStrokeWidth((int) (getResources().getDisplayMetrics().density * 2 + 0.5));
            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        }

        public CustomViewFinder(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        @Override
        protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
            super.onSizeChanged(xNew, yNew, xOld, yOld);
            Rect framingRect = getFramingRect();
            setBorderLineLength((framingRect.right - framingRect.left) / 2);
        }

        @Override
        public void drawLaser(Canvas canvas) {
            //不绘制扫描标志
        }

        @Override
        public void drawViewFinderBorder(Canvas canvas) {
            canvas.drawRect(getFramingRect(), mBorderPaint);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Rect framingRect = getFramingRect();
            float textWidth = textPaint.measureText(maskText);
            float x = (getWidth() - textWidth) / 2;
            float y = framingRect.bottom + textPaint.getTextSize() + 8 * getResources().getDisplayMetrics().density + 0.5F;
            canvas.drawText(maskText, x, y, textPaint);
        }
    }
}
