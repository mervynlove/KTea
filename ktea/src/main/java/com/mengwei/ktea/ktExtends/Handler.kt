
import android.os.Handler
import android.os.Message

/**
 * Create by MengWei at 2018/7/13
 */

inline fun Handler.postDelayed(
        delayInMillis: Long,
        token: Any? = null,
        crossinline action: () -> Unit
): Runnable {
    val runnable = Runnable { action() }
    if (token == null) {
        postDelayed(runnable, delayInMillis)
    } else {
        val message = Message.obtain(this, runnable)
        message.obj = token
        sendMessageDelayed(message, delayInMillis)
    }
    return runnable
}

inline fun Handler.postAtTime(
        uptimeMillis: Long,
        token: Any? = null,
        crossinline action: () -> Unit
): Runnable {
    val runnable = Runnable { action() }
    postAtTime(runnable, token, uptimeMillis)
    return runnable
}
