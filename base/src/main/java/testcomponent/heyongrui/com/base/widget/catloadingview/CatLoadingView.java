package testcomponent.heyongrui.com.base.widget.catloadingview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import testcomponent.heyongrui.com.base.R;

/**
 * Created by lambert on 2018/10/15.
 */

public class CatLoadingView extends Dialog {

    private Animation operatingAnim, eye_left_Anim, eye_right_Anim;
    private View mouse, eye_left, eye_right;
    private EyelidView eyelid_left, eyelid_right;
    private GraduallyTextView mGraduallyTextView;
    private String text;
    private boolean isClickCancelAble = true;

    public CatLoadingView(@NonNull Context context) {
        this(context, R.style.Dialog);
    }

    public CatLoadingView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.dialog_catloading);
        setCanceledOnTouchOutside(isClickCancelAble);
        getWindow().setGravity(Gravity.CENTER);

        operatingAnim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        operatingAnim.setRepeatCount(Animation.INFINITE);
        operatingAnim.setDuration(2000);

        eye_left_Anim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        eye_left_Anim.setRepeatCount(Animation.INFINITE);
        eye_left_Anim.setDuration(2000);

        eye_right_Anim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        eye_right_Anim.setRepeatCount(Animation.INFINITE);
        eye_right_Anim.setDuration(2000);

        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        eye_left_Anim.setInterpolator(lin);
        eye_right_Anim.setInterpolator(lin);

        View view = getWindow().getDecorView();
        mouse = view.findViewById(R.id.mouse);
        eye_left = view.findViewById(R.id.eye_left);
        eye_right = view.findViewById(R.id.eye_right);
        eyelid_left = view.findViewById(R.id.eyelid_left);
        eyelid_left.setColor(Color.parseColor("#d0ced1"));
        eyelid_left.setFromFull(true);
        eyelid_right = view.findViewById(R.id.eyelid_right);
        eyelid_right.setColor(Color.parseColor("#d0ced1"));
        eyelid_right.setFromFull(true);
        mGraduallyTextView = view.findViewById(R.id.graduallyTextView);

        if (!TextUtils.isEmpty(text)) {
            mGraduallyTextView.setText(text);
        }

        operatingAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                eyelid_left.resetAnimator();
                eyelid_right.resetAnimator();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        mouse.setAnimation(operatingAnim);
        eye_left.setAnimation(eye_left_Anim);
        eye_right.setAnimation(eye_right_Anim);
        eyelid_left.startLoading();
        eyelid_right.startLoading();
        mGraduallyTextView.startLoading();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        operatingAnim.reset();
        eye_left_Anim.reset();
        eye_right_Anim.reset();

        mouse.clearAnimation();
        eye_left.clearAnimation();
        eye_right.clearAnimation();

        eyelid_left.stopLoading();
        eyelid_right.stopLoading();
        mGraduallyTextView.stopLoading();
    }

    public void setText(String str) {
        text = str;
    }

    public void setClickCancelAble(boolean bo) {
        isClickCancelAble = bo;
    }
}
