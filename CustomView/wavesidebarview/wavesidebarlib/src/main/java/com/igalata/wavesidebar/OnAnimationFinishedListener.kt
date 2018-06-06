package com.igalata.wavesidebar

import android.animation.Animator


interface OnAnimationFinishedListener : Animator.AnimatorListener {

    override fun onAnimationCancel(animation: Animator?) = Unit

    override fun onAnimationStart(animation: Animator?) = Unit

    override fun onAnimationRepeat(animation: Animator?) = Unit

}