package com.mengwei.ktea.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mengwei.ktea.R;

/**
 * Create by MengWei at 2019/5/18
 */
public class FancyToast extends Toast {
    public static int SUCCESS = 1;
    public static int WARNING = 2;
    public static int ERROR = 3;
    public static int INFO = 4;
    public static int DEFAULT = 5;
    public static int CONFUSING = 6;

    public FancyToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, String message, int duration, int type) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        View layout = LayoutInflater.from(context).inflate(R.layout.fancytoast_layout, null, false);
        TextView l1 = (TextView) layout.findViewById(R.id.toast_text);
        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.toast_type);
        ImageView img = (ImageView) layout.findViewById(R.id.toast_icon);
        l1.setText(message);
        switch (type) {
            case 1:
                linearLayout.setBackgroundResource(R.drawable.success_shape);
                img.setImageResource(R.drawable.ic_check_black_24dp);
                break;
            case 2:
                linearLayout.setBackgroundResource(R.drawable.warning_shape);
                img.setImageResource(R.drawable.ic_pan_tool_black_24dp);
                break;
            case 3:
                linearLayout.setBackgroundResource(R.drawable.error_shape);
                img.setImageResource(R.drawable.ic_clear_black_24dp);
                break;
            case 4:
                linearLayout.setBackgroundResource(R.drawable.info_shape);
                img.setImageResource(R.drawable.ic_info_outline_black_24dp);
                break;
            case 5:
                linearLayout.setBackgroundResource(R.drawable.default_shape);
                img.setVisibility(View.GONE);
                break;
            case 6:
                linearLayout.setBackgroundResource(R.drawable.confusing_shape);
                img.setImageResource(R.drawable.ic_refresh_black_24dp);
                break;
        }
        toast.setView(layout);
        return toast;
    }




    public static Toast makeText(Context context, String message, int duration, int type, int ImageResource) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        View layout = LayoutInflater.from(context).inflate(R.layout.fancytoast_layout, null, false);
        TextView l1 = (TextView) layout.findViewById(R.id.toast_text);
        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.toast_type);
        ImageView img = (ImageView) layout.findViewById(R.id.toast_icon);
        l1.setText(message);
        img.setImageResource(ImageResource);
        switch (type) {
            case 1:
                linearLayout.setBackgroundResource(R.drawable.success_shape);
                break;
            case 2:
                linearLayout.setBackgroundResource(R.drawable.warning_shape);
                break;
            case 3:
                linearLayout.setBackgroundResource(R.drawable.error_shape);
                break;
            case 4:
                linearLayout.setBackgroundResource(R.drawable.info_shape);
                break;
            case 5:
                linearLayout.setBackgroundResource(R.drawable.default_shape);
                img.setVisibility(View.GONE);
                break;
            case 6:
                linearLayout.setBackgroundResource(R.drawable.confusing_shape);
                break;
            default:
                linearLayout.setBackgroundResource(R.drawable.default_shape);
                img.setVisibility(View.GONE);
                break;
        }
        toast.setView(layout);
        return toast;
    }

}
