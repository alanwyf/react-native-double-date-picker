package com.alanwyf.doubleDatePicker;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.alanwyf.doubleDatePicker.R;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

/**
 * A simple dialog containing an {@link DatePicker}.
 * <p/>
 * <p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 * </p>
 */
public class DoubleDatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener {

    private static final String START_YEAR = "start_year";
    private static final String END_YEAR = "end_year";
    private static final String START_MONTH = "start_month";
    private static final String END_MONTH = "end_month";
    private static final String START_DAY = "start_day";
    private static final String END_DAY = "end_day";

    private final DatePicker mDatePicker_start;
    private final DatePicker mDatePicker_end;
    private final OnDateSetListener mCallBack;
    private final Promise mPromise;
    private String startDate = "", endDate = "", maxDate = "", language = "", buttonConfirmText = "确 定", buttonCancelText = "取 消";

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {

        void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                       DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth, Promise promise);
    }

    /**
     * @param context     The context the dialog is to run in.
     * @param theme       the theme to apply to this dialog
     * @param callBack    How the parent is notified that the date is set.
     * @param options         Options to set date picker
     * @param mPromise    Result to be returned to JS
     */
    public DoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, ReadableMap options, boolean isDayVisible, Promise mPromise) {
        super(context, theme);

        mCallBack = callBack;
        this.mPromise = mPromise;

        try{
            startDate = endDate = DateUtils.getAddDayString(0);
            ReadableMapKeySetIterator keys = options.keySetIterator();
            while(keys.hasNextKey()){
                String key = keys.nextKey();
                switch (key){
                    case "startDate":
                        startDate = options.getString(key);
                        break;
                    case "endDate":
                        endDate = options.getString(key);
                        break;
                    case "maxDate":
                        maxDate = options.getString(key);
                        break;
                    case "language":
                        language = options.getString(key);
                        break;
                    default:
                        break;
                }
            }
        }catch(Exception e){
            mPromise.reject("-1", e);
        }
        if(language.equals("English")){
            buttonConfirmText = "OK";
            buttonCancelText = "Cancel";
        }

        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, buttonConfirmText, this);
        setButton(BUTTON_NEGATIVE, buttonCancelText, this);
        setIcon(0);

        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
        Date st = new Date();
        Date et = new Date();
        try {
            st = formatter.parse(startDate);
            et = formatter.parse(endDate);
        } catch (ParseException e){
            mPromise.reject("-1", e);
        }

        Calendar stCal = Calendar.getInstance();
        stCal.setTime(st);

        Calendar etCal = Calendar.getInstance();
        etCal.setTime(et);

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layoutId  = R.layout.date_picker_dialog;
        if(language.equals("English")){
            layoutId = R.layout.date_picker_dialog_en;
        }
        View view = inflater.inflate(layoutId, null);
        setView(view);
        mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
        mDatePicker_end = (DatePicker) view.findViewById(R.id.datePickerEnd);
        mDatePicker_start.init(stCal.get(Calendar.YEAR), stCal.get(Calendar.MONTH), stCal.get(Calendar.DAY_OF_MONTH), this);
        mDatePicker_end.init(etCal.get(Calendar.YEAR), etCal.get(Calendar.MONTH), etCal.get(Calendar.DAY_OF_MONTH), this);

        // hide Day
        if (!isDayVisible) {
            hidDay(mDatePicker_start);
            hidDay(mDatePicker_end);
        }

        // set maxDate
        if(!maxDate.equals("")){
            try {
                mDatePicker_start.setMaxDate(formatter.parse(maxDate).getTime());
                mDatePicker_end.setMaxDate(formatter.parse(maxDate).getTime());
            } catch (ParseException e){
//                e.printStackTrace();
                mPromise.reject("-1", e);
            }
        }
    }

    /**
     * 隐藏DatePicker中的日期显示
     *
     * @param mDatePicker
     */
    private void hidDay(DatePicker mDatePicker) {
        Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
        for (Field datePickerField : datePickerfFields) {
            if ("mDaySpinner".equals(datePickerField.getName())) {
                datePickerField.setAccessible(true);
                Object dayPicker = new Object();
                try {
                    dayPicker = datePickerField.get(mDatePicker);
                } catch (Exception e) {
                    mPromise.reject("-1", e);
                }
                ((View) dayPicker).setVisibility(View.GONE);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == BUTTON_POSITIVE)
            tryNotifyDateSet();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        if (view.getId() == R.id.datePickerStart)
            mDatePicker_start.init(year, month, day, this);
        if (view.getId() == R.id.datePickerEnd)
            mDatePicker_end.init(year, month, day, this);
    }

    /**
     * Get start date picker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerStart() {
        return mDatePicker_start;
    }

    /**
     * Get en date picker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerEnd() {
        return mDatePicker_end;
    }

    /**
     * Sets the start date.
     *
     * @param year        The date year.
     * @param monthOfYear The date month.
     * @param dayOfMonth  The date day of month.
     */
    public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
    }

    /**
     * Sets the end date.
     *
     * @param year        The date year.
     * @param monthOfYear The date month.
     * @param dayOfMonth  The date day of month.
     */
    public void updateEndDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_end.updateDate(year, monthOfYear, dayOfMonth);
    }

    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mDatePicker_start.clearFocus();
            mDatePicker_end.clearFocus();
            mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
                    mDatePicker_start.getDayOfMonth(), mDatePicker_end, mDatePicker_end.getYear(),
                    mDatePicker_end.getMonth(), mDatePicker_end.getDayOfMonth(), mPromise);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(START_YEAR, mDatePicker_start.getYear());
        state.putInt(START_MONTH, mDatePicker_start.getMonth());
        state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());
        state.putInt(END_YEAR, mDatePicker_end.getYear());
        state.putInt(END_MONTH, mDatePicker_end.getMonth());
        state.putInt(END_DAY, mDatePicker_end.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int start_year = savedInstanceState.getInt(START_YEAR);
        int start_month = savedInstanceState.getInt(START_MONTH);
        int start_day = savedInstanceState.getInt(START_DAY);
        mDatePicker_start.init(start_year, start_month, start_day, this);

        int end_year = savedInstanceState.getInt(END_YEAR);
        int end_month = savedInstanceState.getInt(END_MONTH);
        int end_day = savedInstanceState.getInt(END_DAY);
        mDatePicker_end.init(end_year, end_month, end_day, this);

    }
}