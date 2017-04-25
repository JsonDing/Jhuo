package com.yunma.domain;

import jp.wasabeef.recyclerview.animators.*;

/**
 * Created on 2017-03-31
 *
 * @author Json.
 */

public class AnimalType {
     public static final FadeInAnimator FadeIn = new FadeInAnimator();
     public static final FadeInDownAnimator FadeInDown = new FadeInDownAnimator();
     public static final FadeInUpAnimator FadeInUp = new FadeInUpAnimator();
     public static final FadeInLeftAnimator FadeInLeft = new FadeInLeftAnimator();
     public static final FadeInRightAnimator FadeInRight = new FadeInRightAnimator();
     public static final LandingAnimator Landing = new LandingAnimator();
     public static final ScaleInAnimator ScaleIn = new ScaleInAnimator();
     public static final ScaleInTopAnimator ScaleInTop = new ScaleInTopAnimator();
     public static final ScaleInBottomAnimator ScaleInBottom = new ScaleInBottomAnimator();
     public static final ScaleInLeftAnimator ScaleInLeft = new ScaleInLeftAnimator();
     public static final ScaleInRightAnimator ScaleInRight = new ScaleInRightAnimator();
     public static final FlipInTopXAnimator  FlipInTopX = new FlipInTopXAnimator();
     public static final FlipInBottomXAnimator  FlipInBottomX = new FlipInBottomXAnimator();
     public static final FlipInLeftYAnimator  FlipInLeftY = new FlipInLeftYAnimator();
     public static final FlipInRightYAnimator  FlipInRightY = new FlipInRightYAnimator();
     public static final SlideInLeftAnimator  SlideInLeft = new SlideInLeftAnimator();
     public static final SlideInRightAnimator  SlideInRight = new SlideInRightAnimator();
     public static final SlideInDownAnimator  SlideInDown = new SlideInDownAnimator();
     public static final SlideInUpAnimator  SlideInUp = new SlideInUpAnimator();
     public static final OvershootInRightAnimator  OvershootInRight = new OvershootInRightAnimator(1.0f);
     public static final OvershootInLeftAnimator  OvershootInLeft = new OvershootInLeftAnimator(1.0f);
}
