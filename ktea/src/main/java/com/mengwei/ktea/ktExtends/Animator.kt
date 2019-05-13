
import android.animation.Animator
import android.support.annotation.RequiresApi

/**
 * Create by MengWei at 2018/7/13
 */

fun Animator.doOnEnd(action: (animator: Animator) -> Unit) = addListener(onEnd = action)

fun Animator.doOnStart(action: (animator: Animator) -> Unit) = addListener(onStart = action)

fun Animator.doOnCancel(action: (animator: Animator) -> Unit) = addListener(onCancel = action)

fun Animator.doOnRepeat(action: (animator: Animator) -> Unit) = addListener(onRepeat = action)

@RequiresApi(19)
fun Animator.doOnResume(action: (animator: Animator) -> Unit) = addPauseListener(onResume = action)

@RequiresApi(19)
fun Animator.doOnPause(action: (animator: Animator) -> Unit) = addPauseListener(onPause = action)


fun Animator.addListener(
        onEnd: ((animator: Animator) -> Unit)? = null,
        onStart: ((animator: Animator) -> Unit)? = null,
        onCancel: ((animator: Animator) -> Unit)? = null,
        onRepeat: ((animator: Animator) -> Unit)? = null
): Animator.AnimatorListener {
    val listener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animator: Animator) {
            onRepeat?.invoke(animator)
        }

        override fun onAnimationEnd(animator: Animator) {
            onEnd?.invoke(animator)
        }

        override fun onAnimationCancel(animator: Animator) {
            onCancel?.invoke(animator)
        }

        override fun onAnimationStart(animator: Animator) {
            onStart?.invoke(animator)
        }
    }
    addListener(listener)
    return listener
}

@RequiresApi(19)
fun Animator.addPauseListener(
        onResume: ((animator: Animator) -> Unit)? = null,
        onPause: ((animator: Animator) -> Unit)? = null
): Animator.AnimatorPauseListener {
    val listener = object : Animator.AnimatorPauseListener {
        override fun onAnimationPause(animator: Animator) {
            onPause?.invoke(animator)
        }

        override fun onAnimationResume(animator: Animator) {
            onResume?.invoke(animator)
        }
    }
    addPauseListener(listener)
    return listener
}
