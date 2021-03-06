package com.koraniar.freebitcoin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.koraniar.freebitcoin.Enums.RequestType;

import android.os.Handler;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "FreeMain";

    NotificationService _notificationService = new NotificationService();
    WebView mainWebView = null;
    boolean loggedIn = false;
    boolean fromBoot = false;
    boolean notificationEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        /*Intent intent = getIntent();

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                fromBoot = extras.getBoolean("FROM_BOOT", false);
            } else {
                Log.e(LOG_TAG, "extras null");
            }
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //web
        CookieManager.getInstance().setAcceptCookie(true);
        mainWebView = (WebView) findViewById(R.id.MainWebview);
        mainWebView.loadUrl("https://freebitco.in");
        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        mainWebView.addJavascriptInterface(new WebAppInterface(this), "android");
        mainWebView.setWebViewClient(new MyWebViewClient());
        CookieSyncManager.createInstance(this.getBaseContext());
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void getPageInfo(int requestCode, String info) {
            switch (requestCode) {
                case RequestType.LoginTest:
                    if (info.equals("0")) {
                        loggedIn = true;
                        mainWebView.post(new Runnable() {
                            @Override
                            public void run() {
                                mainWebView.loadUrl(JavaScript.FreeGetCountDown);
                                mainWebView.loadUrl(JavaScript.FreeAddListenerToRollButton);
                            }
                        });
                    } else {
                        loggedIn = false;
                    }
                    break;
                case RequestType.GetCountDown:
                    if (!info.equals("")) {
                        notificationEnabled = true;
                        int time = (Integer.parseInt(info) + 1);
                        Toast.makeText(mContext, "We call you on " + Integer.toString(time) + " minutes", Toast.LENGTH_SHORT).show();
                        _notificationService.showClaimBtcNotification(mContext, time * 60000, 101, "Free BTC available", "Tap to claim it!", "com.koraniar.freebitcoin.MainActivity");
                    }
                    break;
                case RequestType.RollButtonPressed:
                    notificationEnabled = false;
                    _notificationService.showClaimBtcNotification(mContext, 61 * 60000, 101, "Free BTC", "This is a recordatory to play at freebitco.in", "com.koraniar.freebitcoin.MainActivity");
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainWebView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mainWebView.loadUrl(JavaScript.FreeGetCountDown);
                                }
                            });
                        }
                    }, 2000);
                    break;
                default:
                    Toast.makeText(mContext, "Request Code is not valid", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {

        public void onPageFinished(WebView view, String url) {
            Log.e(LOG_TAG, "onPageFinished");
            Log.e(LOG_TAG, url);
            mainWebView.loadUrl(JavaScript.GlobalTestLogin);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(LOG_TAG, "shouldOverrideUrlLoading");
            Log.e(LOG_TAG, url);
            Log.e(LOG_TAG, Uri.parse(url).getHost());
            CookieSyncManager.getInstance().sync();
            if (Uri.parse(url).getHost().equals("freebitco.in")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            return false;
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            //startActivity(intent);
            //return true;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mainWebView.canGoBack()) {
                mainWebView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_reload_page) {
            mainWebView.loadUrl("https://freebitco.in");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mainWebView.loadUrl(JavaScript.GoToHome);
        } else if (id == R.id.nav_multiply) {
            mainWebView.loadUrl(JavaScript.GoToMultiply);
        } else if (id == R.id.nav_lottery) {
            mainWebView.loadUrl(JavaScript.GoToLottery);
        } else if (id == R.id.nav_refer) {
            mainWebView.loadUrl(JavaScript.GoToRefer);
        } else if (id == R.id.nav_profile) {
            mainWebView.loadUrl(JavaScript.GoToProfile);
        } else if (id == R.id.nav_share) {
            _notificationService.showNotification(getApplicationContext(), 102, "Free BTC available", "Tap to claim it!", MainActivity.class);
        } else if (id == R.id.nav_send) {
            mainWebView.loadUrl(JavaScript.FreeTestSound);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
