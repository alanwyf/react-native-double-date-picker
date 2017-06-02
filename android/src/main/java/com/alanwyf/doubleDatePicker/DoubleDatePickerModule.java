package com.alanwyf.doubleDatePicker;

import android.util.Log;
import android.widget.Toast;
import android.app.Activity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.widget.DatePicker;

/**
 * Created by alan on 2017/6/2.
 */

public class DoubleDatePickerModule extends ReactContextBaseJavaModule {

    ReactApplicationContext mContext ;
    String statBeginDate, statEndDatge;
    private final String TAG = "DatePicker";


    public DoubleDatePickerModule (ReactApplicationContext reactApplicationContext){
        super(reactApplicationContext);
        mContext = reactApplicationContext;
        final ReactApplicationContext ctx = reactApplicationContext;
    }

    @Override
    public String getName() {
        return "DoubleDatePicker";
    }

    @ReactMethod
    public void show() {
        final Activity activity = getCurrentActivity();
        statBeginDate = statEndDatge = DateUtils.getAddDayString(-1);

//        Toast.makeText(getReactApplicationContext(), message, duration).show();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(activity, 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                  int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                  int endDayOfMonth) {

                String startYearNum = ""+startYear;
                String startMonthOfYearNum = (startMonthOfYear + 1) < 10 ? "0"+(startMonthOfYear + 1) : ""+(startMonthOfYear + 1);
                String startDayOfMonthNum = startDayOfMonth < 10 ? "0"+startDayOfMonth : ""+startDayOfMonth;
                String endYearNum = ""+endYear;
                String endMonthOfYearNum = (endMonthOfYear + 1) < 10 ? "0"+(endMonthOfYear + 1) : ""+(endMonthOfYear + 1);
                String endDayOfMonthNum = endDayOfMonth < 10 ? "0"+endDayOfMonth : ""+endDayOfMonth;

                statBeginDate = startYearNum + "-" + startMonthOfYearNum + "-" + startDayOfMonthNum;
                statEndDatge = endYearNum + "-" + endMonthOfYearNum + "-" + endDayOfMonthNum;
                Log.d(TAG, "开始时间：" + statBeginDate + " 结束时间" + statEndDatge);
                if(statEndDatge.compareTo(statBeginDate) < 0){
                    Toast.makeText(getReactApplicationContext(), "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
//                    MiToast.show(AppContext.getContext(), "结束时间不能小于开始时间", Toast.LENGTH_SHORT);
                } else {
//                    selectTypeTextViews.get(listType).setTextColor(getResources().getColor(R.color.font_deactive));
//                    listType = LIST_YPE_FILTER;
//                    getMainData(listType, statBeginDate, statEndDatge);
//                    selectTypeTextViews.get(listType).setTextColor(getResources().getColor(R.color.font_blue));
                }

            }
        }, DateUtils.getAddDayString(0), DateUtils.getAddDayString(0), true);
        dialog.show();
    }
}
