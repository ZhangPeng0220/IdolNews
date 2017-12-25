package com.idol.idolnews.Utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by 53478 on 2017/11/24.
 */

public class ContextHolder {
    static Context ApplicationContext;
    public static void initial(Context context) {
        ApplicationContext = context;
    }
    public static Context getContext() {
        return ApplicationContext;
    }
}

class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.initial(this);
    }
}

