package com.demon.tool.databind_demo.util;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

/**
 * Binding 自定义属性类
 */
public class ValueUtil {

    /**
     * 为 loadImage 增加自定义属性，自动加载图片
     * app:imageUrl="@{url}"
     *
     * @param imageView
     * @param url
     */
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

}
