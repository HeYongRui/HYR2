package testcomponent.heyongrui.com.base.util;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lambert on 2018/10/25.
 */

public class UiUtil {
    /**
     * 利用字符串资源反射获取本地资源文件
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 计算两种颜色的中间某个值的颜色（利用自带API）
     */
    public static int getCurrentColorBySystem(@FloatRange(from = 0.0, to = 1.0) float percent, int startColor, int endColor) {
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();//渐变色计算类
        return (int) argbEvaluator.evaluate(percent, startColor, endColor);
    }

    /**
     * 计算两种颜色的中间某个值的颜色（通过计算）
     */
    public static int getCurrentColorByCalculate(float percent, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + percent * redDifference);
        blueCurrent = (int) (blueStart + percent * blueDifference);
        greenCurrent = (int) (greenStart + percent * greenDifference);
        alphaCurrent = (int) (alphaStart + percent * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

    /**
     * 判断是否有底部虚拟键盘
     */
    public static boolean isHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 底部导航栏是否显示
     */
    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }

    /**
     * 获取NavigationBar的高度
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && isHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 创建文本多个状态时颜色列表
     */
    public static ColorStateList createTextColorStateList(int selectedColor, int pressedColor, int normalColor) {
        int[] colors = new int[]{selectedColor, pressedColor, normalColor};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 创建多个状态时颜色列表
     */
    public static ColorStateList createColorStateList(Context context, int normalColor, int pressedColor, int selectedColor) {
        int[] colors = new int[]{normalColor, pressedColor, selectedColor};
        int[][] states = new int[3][];
        states[0] = new int[]{};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{android.R.attr.state_selected};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 创建正常和禁用状态颜色列表
     */
    public static ColorStateList createColorStateList(int normalColor, int unableColor) {
        ColorStateList colorStateList = createColorStateList(normalColor, normalColor, normalColor, unableColor);
        return colorStateList;
    }

    /**
     * 创建多个状态时颜色列表
     */
    public static ColorStateList createColorStateList(int normalColor, int pressedColor, int focusedColor, int unableColor) {
        int[] colors = new int[]{pressedColor, focusedColor, normalColor, focusedColor, unableColor, normalColor};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 为单个view设置点击效果，高版本带涟漪反馈
     *
     * @param context     上下文
     * @param normalColor 未点击的颜色
     * @param pressColor  按下的颜色
     * @param view        目标view
     */
    public static void setOnclickFeedBack(Context context, @ColorRes int normalColor, @ColorRes int pressColor, View view) {
        Drawable bgDrawble;
        ColorDrawable drawablePressed = new ColorDrawable(ContextCompat.getColor(context, pressColor));//分别解析两种颜色为colordrawble
        ColorDrawable drawableNormal = new ColorDrawable(ContextCompat.getColor(context, normalColor));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {//高版本设置RippleDrawable 而低版本设置 StateListDrawable也就是selector
            ColorStateList stateList = ColorStateList.valueOf(ContextCompat.getColor(context, pressColor));
            bgDrawble = new RippleDrawable(stateList, drawableNormal, drawablePressed);
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
            stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, drawableNormal);
            bgDrawble = stateListDrawable;
        }
        view.setBackgroundDrawable(bgDrawble);//最终设置给我们的view作为背景
    }

    /**
     * 支持同时设置多个view
     *
     * @param context     上下文
     * @param normalColor 正常颜色
     * @param pressColor  按下颜色
     * @param views       目标view群
     */
    public static void setOnclickFeedBack(Context context, @ColorRes int normalColor, @ColorRes int pressColor, View... views) {
        for (View view : views) {
            setOnclickFeedBack(context, normalColor, pressColor, view);
        }
    }

    /**
     * 动态设置progressBar的颜色样式
     */
    public static void setProgressBarColors(ProgressBar progressBar, int backgroundColor, int progressColor) {
        //Background
        ClipDrawable bgClipDrawable = new ClipDrawable(new ColorDrawable(backgroundColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        //Progress
        ClipDrawable progressClip = new ClipDrawable(new ColorDrawable(progressColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        //Setup LayerDrawable and assign to progressBar
        Drawable[] progressDrawables = {bgClipDrawable, progressClip/*second*/, progressClip};
        LayerDrawable progressLayerDrawable = new LayerDrawable(progressDrawables);
        progressLayerDrawable.setId(0, android.R.id.background);
        progressLayerDrawable.setId(1, android.R.id.secondaryProgress);
        progressLayerDrawable.setId(2, android.R.id.progress);
        progressBar.setProgressDrawable(progressLayerDrawable);
        bgClipDrawable.setLevel(10000);
    }

    /**
     * 动态设置SeekBar的颜色样式
     */
    public static void setSeekbarColors(SeekBar seekBar, int progressColor, int secondaryProgressColor, int backgroundColor) {
        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;

        LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
        Drawable progress = layerDrawable.findDrawableByLayerId(android.R.id.progress);
        Drawable secondary = layerDrawable.findDrawableByLayerId(android.R.id.secondaryProgress);
        Drawable background = layerDrawable.findDrawableByLayerId(android.R.id.background);
        Drawable th = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            th = seekBar.getThumb();
        }
        // Setting colors
        progress.setColorFilter(progressColor, mMode);
        secondary.setColorFilter(secondaryProgressColor, mMode);
        background.setColorFilter(backgroundColor, mMode);
        if (th != null) {
            th.setColorFilter(progressColor, mMode);
        }
        // Applying Tinted Drawables
        layerDrawable.setDrawableByLayerId(android.R.id.progress, progress);
        layerDrawable.setDrawableByLayerId(android.R.id.secondaryProgress, secondary);
        layerDrawable.setDrawableByLayerId(android.R.id.background, background);
    }
}
