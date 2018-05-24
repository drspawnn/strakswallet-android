package com.strakswallet.tools.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on <mihail@breadwallet.com> 6/8/17.
 * Copyright (c) 2017 breadwallet LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class BRDateUtil {

    public static String getCustomSpan(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();

        now.setTime(new Date());
        then.setTime(date);

        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;

        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffMinutes < 60) {
            return diffMinutes + " m";

        } else if (diffHours < 24) {
            return diffHours + " h";

        } else if (diffDays < 7) {
            return diffDays + " d";

        } else {
            SimpleDateFormat todate = new SimpleDateFormat("MMM dd",
                    Locale.getDefault());

            return todate.format(date);
        }
    }

    public static String getShortDate(long timestamp) {
        //long millTimestamp = timestamp * 1000;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Log.d("BRDateUtil", "Timestamp -> " + timestamp);

        String dateString = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(calendar.getTimeInMillis());
        Log.d("BRDateUtil", "Transaction date string -> " + dateString);

        return dateString;
    }

    public static String getLongDate(long timestamp){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Log.d("BRDateUtil", "Timestamp -> " + timestamp);

        String dateString = new SimpleDateFormat("MMMM dd, yyyy, hh:mm a", Locale.getDefault()).format(calendar.getTimeInMillis());
        Log.d("BRDateUtil", "Transaction date string -> " + dateString);

        return dateString;

    }
}
