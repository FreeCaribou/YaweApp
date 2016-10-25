package com.caribou.yaweapp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by caribou on 25/10/16.
 */

public class FunctionClass {

    public static boolean isNotToLongOrEmpty(String message, int lengthMax, Context context) {
        if (message.isEmpty() || message.length() == 0) {
            Toast.makeText(context, context.getString(R.string.message_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (message.length() >= lengthMax) {
            String beginSentence = context.getString(R.string.its_maximum);
            String endSentence = context.getString(R.string.character);
            String toastMessage = beginSentence + " " + String.valueOf(lengthMax) + " " + endSentence;
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


}
