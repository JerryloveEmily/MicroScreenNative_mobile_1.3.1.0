package com.boding.microscreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by JerryloveEmily on 16/2/28.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 进入新界面
     * @param cls               新界面Activity
     * @param isFinishCurrentAct  是否关闭当前界面
     */
    public void enterActivity(Class<?> cls, boolean isFinishCurrentAct){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if (isFinishCurrentAct){
            finish();
        }
    }

    /**
     * 进入新界面
     * @param cls               新界面Activity
     * @param isFinishCurrentAct  是否关闭当前界面
     * @param enterAnim         进入界面的动画
     * @param exitAnim          退出界面的动画
     */
    public void enterActivity(Class<?> cls, boolean isFinishCurrentAct, int enterAnim, int exitAnim){
        enterActivity(cls, isFinishCurrentAct);
        overridePendingTransition(enterAnim, exitAnim);
    }
}
