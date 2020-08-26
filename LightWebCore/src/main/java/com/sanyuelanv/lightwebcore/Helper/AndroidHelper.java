package com.sanyuelanv.lightwebcore.Helper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.Objects;

/**
 * Create By songhang in 2020/8/24
 */
public class AndroidHelper {
    public static String getClipContent(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && Objects.requireNonNull(manager.getPrimaryClip()).getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }
    public static void setClipContent(Context context,String content) {
        if (!TextUtils.isEmpty(content)) {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            assert cmb != null;
            ClipData clipData = ClipData.newPlainText("lightWeb text", content);
            cmb.setPrimaryClip(clipData);
        }
    }
}
