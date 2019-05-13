import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.shashank.sony.fancytoastlib.FancyToast

/**
 * Create by MengWei at 2018/7/13
 */

object Toastor {
    var success: Toast? = null
    var info: Toast? = null
    var error: Toast? = null
    var normal: Toast? = null
    var warning: Toast? = null
}

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toastor.normal?.cancel()
    Toastor.normal = Toast.makeText(this, text, duration)
    Toastor.normal?.show()
}


fun Context.infoToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toastor.info?.cancel()
    Toastor.info = FancyToast.makeText(this, text, duration, FancyToast.INFO, false)
    Toastor.info?.run {
        setGravity(Gravity.CENTER, 0, 200)
        show()
    }

}

fun Context.errorToast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toastor.error?.cancel()
    Toastor.error = FancyToast.makeText(this, text, duration, FancyToast.ERROR, false)
    Toastor.error?.run {
        setGravity(Gravity.CENTER, 0, 200)
        show()
    }
}

fun Context.successToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toastor.success?.cancel()
    Toastor.success = FancyToast.makeText(this, text, duration, FancyToast.SUCCESS, false)
    Toastor.success?.run {
        setGravity(Gravity.CENTER, 0, 200)
        show()
    }
}

fun Context.warningToast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toastor.warning?.cancel()
    Toastor.warning = FancyToast.makeText(this, text, duration, FancyToast.WARNING, false)
    Toastor.warning?.run {
        setGravity(Gravity.CENTER, 0, 200)
        show()
    }
}