package com.example.newbabyproject.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {




    public static String dataSplitFormat(String data, String type){

        String[] resultData;
        String result = "";

        if(data != null){
            if(!data.equals("")){
                if("date".equals(type)){
                    resultData = data.split("-");
                    result = resultData[0] + "년" + resultData[1] + "월" + resultData[2] + "일";
                }else if("time".equals(type)){
                    resultData = data.split(":");
                    result = " " + resultData[0] + "시" + resultData[1] + "분 출생";
                }
            }else{
                if("date".equals(type)){
                    result = "출생일자 미등록";
                }
            }
        }else{
            result = "미등록";
        }



        return result;
    }

    //현재 시간
    public static String nowDate(String format){
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date nowdate = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat(format);
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(nowdate);

        return formatDate;
    }
    // 몇분 전, 방금 전
    private static class TIME_MAXIMUM{
        public static final int SEC = 60;       //초
        public static final int MIN = 60;       //분
        public static final int HOUR = 24;      //시
        public static final int DAY = 30;       //일
        public static final int MONTH = 12;     //월
    }

    public static String formatTimeString(String regTime){

        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date temdate = null;
        long diffTime = 0;
        long time = 0;
        long curTime = 0;
        String msg = null;
        try {
            temdate = tempDate.parse(regTime);
            time = temdate.getTime();

            curTime = System.currentTimeMillis();
            diffTime = (curTime - time) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(diffTime < TIME_MAXIMUM.SEC){
            msg = "방금 전";
        }else if((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN){
            msg = diffTime +"분 전";
        }else if((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR){
            msg = diffTime +"시간 전";
        }/*else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        }*/
        else {
            msg = regTime.substring(0,16) +"";
            Log.d("board","날짜 ===> " + msg);
        }
        return msg;
    }


    public static SpannableStringBuilder Spannable(String str, int start, int end) {

        int color = Color.rgb(240, 130, 95);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(str);
        sb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    //ProgressDialog 공통
    public static class ProgressDialogHandler extends Handler {

        private final int START_PROGRESSDIALOG = 100;
        private final int END_PROGRESSDIALOG = 101;
        public ProgressDialog mProgressDialog = null;
        private Context context = null;

        public ProgressDialogHandler(Context context){
            this.context = context;
        }

        public void handleMessage(int gubun){
            switch (gubun){
                case START_PROGRESSDIALOG:
                    if(mProgressDialog == null){
                        mProgressDialog = new ProgressDialog(context);
                        mProgressDialog.setTitle("Working...");
                        mProgressDialog.setMessage("wait for complete working..");
                    }
                    mProgressDialog.show();
                    break;
                case END_PROGRESSDIALOG:
                    if(mProgressDialog != null){
                        mProgressDialog.dismiss();
                    }
                    break;
            }
        }
    }

    public static void intentCommon(Activity activity, Class cls2) {

        Intent intent = new Intent(activity, cls2);
        activity.startActivity(intent);
    }


}
