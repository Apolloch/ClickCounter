package com.example.bacquet.myapplication.clickCounting;

/**
 * Created by DIENG on 02/12/2016.
 */

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * Aspect that count CLICKS done for an Action
 */
@Aspect()
public class ClickCounterAspect {

    /**
     * Logcat Tag
     */
    private static final String TAG = "ClickCounterAspect";
    private static final String CLICKS = "clicks";
    private static final String SUB_ACTIONS = "sub_actions";
    private static final String SUCCESS = "success";
    private static final String NAME = "name";


    /**
     * holds the number of CLICKS for each activity, mapped by Action
     */
    private LinkedHashMap<String,JSONObject> actionsClicMap = new LinkedHashMap<>();

    @Pointcut("@annotation(ActionBegin) && this(activity)")
    public void actionBegin(Activity activity){}

    @Pointcut("@annotation(ActionEnd) && this(activity)")
    public void actionEnd(Activity activity){}

    //@Pointcut("execution(* *.*(android.view.View)) && this(activity)")
    //The activity should override dispatchTouchEvent(MotionEvent)
    @Pointcut("execution(* (android.app.Activity+).dispatchTouchEvent(..)) && this(activity) && args(ev)")
    public void click(Activity activity, MotionEvent ev){
    }


    /**
     *register the activity that is now displaying
     * @param joinPoint thisJoinPoint
     */
    @After("actionBegin(activity)")
    public void registerAction(JoinPoint joinPoint, Activity activity){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ActionBegin actionBegin = method.getAnnotation(ActionBegin.class);
        Log.i(TAG, "registerAction: action begin"+actionBegin.name());
        try {
            actionsClicMap.put(actionBegin.name(),new JSONObject().put(NAME,actionBegin.name()).put(CLICKS,0).put(SUB_ACTIONS,new JSONArray()));
        } catch (JSONException e) {
            Log.e(TAG, "registerAction: "+e);
        }
    }


    @After("actionEnd(activity)")
    public void endClickCount(JoinPoint joinPoint,Activity activity) throws UnfinishedAction {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ActionEnd actionEnd = method.getAnnotation(ActionEnd.class);
        int position = position(actionsClicMap, actionEnd.name());
        if
        (actionsClicMap.size()>1 && position ==actionsClicMap.size()-1){
            try {
                actionsClicMap.get(actionEnd.name()).put(SUCCESS,actionEnd.success());
                elementAt(actionsClicMap,position-1).getJSONArray(SUB_ACTIONS).put(actionsClicMap.get(actionEnd.name()));

            } catch (JSONException e) {
                Log.e(TAG, "endClickCount: "+e);
            }
        }
        else if(actionsClicMap.size()==1){
            try {
                actionsClicMap.get(actionEnd.name()).put(SUCCESS,actionEnd.success());
            } catch (JSONException e) {
                Log.e(TAG, "endClickCount: "+e);
            }
            Log.i(TAG, "endClickCount: "+actionsClicMap.get(actionEnd.name()));
            new ClickReportSender(activity.getApplicationContext()).sendClickReport(actionsClicMap.get(actionEnd.name()));
        }
        else{
            throw new UnfinishedAction();
        }

        actionsClicMap.remove(actionEnd.name());
    }

    private int position(LinkedHashMap<String,JSONObject> linkedHashMap, String key) {
        int i = 0;
        for (String mapKey : linkedHashMap.keySet()) {
            if(mapKey.equals(key)){
                return i ;
            }
            i++;
        }
        return -1;
    }

    @Nullable
    private JSONObject elementAt(LinkedHashMap<String,JSONObject> linkedHashMap, int pos) {
        int i = 0;
        for (String mapKey : linkedHashMap.keySet()) {
            if(i == pos){
                return linkedHashMap.get(mapKey) ;
            }
            i++;
        }
        return null;
    }

    @After("click(activity,ev)")
    public void countClick(JoinPoint joinPoint,Activity activity,MotionEvent ev){
        Log.i(TAG, "click: " + joinPoint.getSignature().toLongString() + " " + joinPoint.getSignature().getName());
        if(ev.getAction()==MotionEvent.ACTION_UP) {
            Log.i(TAG, "ok");
            for (String action : actionsClicMap.keySet()) {
                try {

                    actionsClicMap.get(action).put(CLICKS, actionsClicMap.get(action).getInt(CLICKS) + 1);
                } catch (JSONException e) {
                    Log.e(TAG, "countClick: " + e);
                }
            }
        }
    }


    private class UnfinishedAction extends RuntimeException {
        UnfinishedAction(){
            super("An action have been finished before its child(s) get finished");
        }
    }
}
