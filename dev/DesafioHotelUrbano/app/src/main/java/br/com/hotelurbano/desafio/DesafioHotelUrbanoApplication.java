package br.com.hotelurbano.desafio;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gustavoamg on 10/02/17.
 */

public class DesafioHotelUrbanoApplication extends Application {

    public static final String BACKENDSERVERNAME = "EIchrkJ";

    private static Context context;
    private static String APP = "desafiohotelurbano";
    private static String url;

    @Override
    public void onCreate() {
        super.onCreate();
        setAppContext(getApplicationContext());
    }

    public static Context getAppContext() {
        return context;
    }

    public static void setAppContext(Context _context) {
        context = _context;
    }

    public static boolean saveUrl (String url) {

        DesafioHotelUrbanoApplication.url =  url;

        SharedPreferences pref = context.getSharedPreferences(APP,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("url", url);

        return editor.commit();
    }

    public static String getUrl () {

        if(url == null) {
            SharedPreferences pref = context.getSharedPreferences(APP,
                    MODE_PRIVATE);
            url = pref.getString("url", null);
        }

        return url;
    }
}
