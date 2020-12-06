package com.dixitpatel.pokemondemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class Utils {

    Context mConText;

    public Utils(Context context) {
        mConText = context;
    }

    public static float dpToPx(@NonNull Context context, @Dimension(unit = Dimension.DP) int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvail = false;
        try {
            if (cm != null) {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    isNetworkAvail = (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));

                    return isNetworkAvail;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void hideKeyboard(Activity activity, int flags) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        View viewWithFocus = activity.getCurrentFocus();
        IBinder windowToken = viewWithFocus != null ? viewWithFocus.getWindowToken() : null;

        if (windowToken != null) {
            imm.hideSoftInputFromWindow(windowToken, flags);
        }
    }


    public static void hideKeyboard(Fragment fragment, int flags) {
        Activity activity = fragment.getActivity();
        if (activity != null) {
            hideKeyboard(activity, flags);
        }
    }

    public static void hideKeyboard(Fragment fragment) {
        hideKeyboard(fragment, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftKeyboard(Activity me, EditText edt) {
        try {
            InputMethodManager imm = (InputMethodManager) me.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
