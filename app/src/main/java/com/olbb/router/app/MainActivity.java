package com.olbb.router.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.olbb.router.annotations.Router;

@Router(path = "main")
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClick(View view) {
        Intent intent = ((MApplication)getApplication()).getProxy().getActivityIntent(this, "secondAct");
        startActivity(intent);
    }

    public void onClick2(View view) throws InstantiationException, IllegalAccessException {
       Fragment fragment = ((MApplication)getApplication()).getProxy().getV4Fragment("fragment1");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root,fragment);
        transaction.commitAllowingStateLoss();

    }

    public void onClick3(View view) {
        Intent intent = ((MApplication)getApplication()).getProxy().getActivityIntent(this, "sub_activity");
        startActivity(intent);
    }
}
