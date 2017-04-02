package com.android.showmeeapp.views;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

/**
 * Created by yuva on 18/7/16.
 */
public class ExpandView extends Activity {
	public static String Expand="expand";
	public static String Collapse="collapse";



	public static void expand(final View v) {
		v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final int targetHeight = v.getMeasuredHeight();

		// Older versions of android (pre API 21) cancel animations for views with a height of 0.
		v.getLayoutParams().height = 1;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1
						? ViewGroup.LayoutParams.WRAP_CONTENT
						: (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 4);
		v.startAnimation(a);
	}


	public static void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
	}

	/// for Top to down and down to top animation
	public static void setAnaimation(final View v, String type) {
		TranslateAnimation anim = null;
		if (type.equals(Expand)) {
			anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);

		} else {
			anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
			Animation.AnimationListener collapselistener = new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					v.setVisibility(View.GONE);
				}
			};

			anim.setAnimationListener(collapselistener);
		}

		anim.setDuration(300);
		anim.setInterpolator(new AccelerateInterpolator(0.5f));
		v.startAnimation(anim);
		v.setVisibility(View.VISIBLE);
	}

}
