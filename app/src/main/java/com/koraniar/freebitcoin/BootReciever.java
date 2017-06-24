package com.koraniar.freebitcoin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.koraniar.freebitcoin.Enums.RequestType;

/**
 * Created by koraniar on 6/24/17.
 */

public class BootReciever extends BroadcastReceiver {

    WebView bootWebView = null;
    NotificationService _notificationService = new NotificationService();

    @Override
    public void onReceive(Context context, Intent intent) {


        _notificationService.showNotification(context, 103, "Free BTC", "It notification appear after start android", MainActivity.class);

        /*Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.putExtra("FROM_BOOT", true);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);*/

        /*_notificationService.showNotification(context, 104, "Free BTC", "On login test", MainActivity.class);
        bootWebView = new WebView(context);
        bootWebView.loadUrl("https://freebitco.in");
        WebSettings webSettings = bootWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        bootWebView.addJavascriptInterface(new BootReciever.BootAppInterface(context), "android");
        bootWebView.setWebViewClient(new BootReciever.BootWebViewClient(context));*/
    }

    /*public class BootAppInterface {
        Context mContext;

        BootAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void getPageInfo(int requestCode, String info) {
            switch (requestCode) {
                case RequestType.LoginTest:
                    _notificationService.showNotification(mContext, 103, "Free BTC", "On login test", MainActivity.class);
                    if (info.equals("0")) {
                        _notificationService.showNotification(mContext, 105, "Free BTC", "On login test true", MainActivity.class);
                        bootWebView.post(new Runnable() {
                            @Override
                            public void run() {
                                bootWebView.loadUrl(JavaScript.FreeGetCountDown);
                                bootWebView.loadUrl(JavaScript.FreeAddListenerToRollButton);
                            }
                        });
                    }
                    break;
                case RequestType.GetCountDown:
                    if (!info.equals("")) {
                        int time = (Integer.parseInt(info) + 1);
                        Toast.makeText(mContext, "We call you on " + Integer.toString(time) + " minutes", Toast.LENGTH_SHORT).show();
                        _notificationService.showClaimBtcNotification(mContext, time * 60000, 101, "com.koraniar.freebitcoin.MainActivity");
                    } else {
                        //is time
                    }
                    break;
                default:
                    Toast.makeText(mContext, "Request Code is not valid", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class BootWebViewClient extends WebViewClient {
        Context mContext;
        BootWebViewClient(Context c) {
            mContext = c;
        }

        public void onPageFinished(WebView view, String url) {
            _notificationService.showNotification(mContext, 105, "Free BTC", "On page finish load", MainActivity.class);
            bootWebView.loadUrl(JavaScript.GlobalTestLogin);
        }
    }*/

}
