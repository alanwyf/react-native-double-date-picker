package com.alanwyf.doubleDatePicker;

import android.util.Log;
import android.app.Activity;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import android.widget.DatePicker;

public class DoubleDatePickerModule extends ReactContextBaseJavaModule {

    private String startDate, endDate;
    private final String TAG = "DatePicker";


    public DoubleDatePickerModule (ReactApplicationContext reactApplicationContext){
        super(reactApplicationContext);
    }

    @Override
    public String getName() {
        return "DoubleDatePicker";
    }

    @ReactMethod
    public void show(ReadableMap options, Promise promise) {
        final Activity activity = getCurrentActivity();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(activity, 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                  int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                  int endDayOfMonth, Promise promise) {
                String startYearNum = ""+startYear;
                String startMonthOfYearNum = (startMonthOfYear + 1) < 10 ? "0"+(startMonthOfYear + 1) : ""+(startMonthOfYear + 1);
                String startDayOfMonthNum = startDayOfMonth < 10 ? "0"+startDayOfMonth : ""+startDayOfMonth;
                String endYearNum = ""+endYear;
                String endMonthOfYearNum = (endMonthOfYear + 1) < 10 ? "0"+(endMonthOfYear + 1) : ""+(endMonthOfYear + 1);
                String endDayOfMonthNum = endDayOfMonth < 10 ? "0"+endDayOfMonth : ""+endDayOfMonth;

                startDate = startYearNum + "-" + startMonthOfYearNum + "-" + startDayOfMonthNum;
                endDate = endYearNum + "-" + endMonthOfYearNum + "-" + endDayOfMonthNum;
                Log.d(TAG, "result : " + startDate + "   " + endDate);

                if(endDate.compareTo(startDate) < 0){
                    promise.reject("0", "End date cannot be earlier than start date");
                } else {
                    WritableMap result = new WritableNativeMap();
                    result.putString("startDate", startDate);
                    result.putString("endDate", endDate);
                    promise.resolve(result);
                }
            }
        }, options, true, promise);
        dialog.show();
    }
}
