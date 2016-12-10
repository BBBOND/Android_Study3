package com.kim.html;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    private ContactService contactService;

    Handler showContactsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String json = (String) msg.obj;
            webView.loadUrl("javascript:show(\'" + json + "\')");
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        contactService = new ContactService();
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        //设置支持js
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

        });
        //设置不掉用外部浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new ContactPlugin(), "contact");
        webView.loadUrl("file:///android_asset/index.html");
    }

    class ContactPlugin {

        @JavascriptInterface
        public void getContacts() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Contact> contacts = contactService.getContacts();
                    try {
                        JSONArray array = new JSONArray();
                        for (Contact contact : contacts) {
                            JSONObject item = new JSONObject();
                            item.put("id", contact.getId());
                            item.put("name", contact.getName());
                            item.put("mobile", contact.getMobile());
                            array.put(item);
                        }
                        String json = array.toString();
                        Log.d("ContactPlugin", json);
                        Message message = Message.obtain();
                        message.obj = json;
                        showContactsHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @JavascriptInterface
        public void call(String mobile) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
            startActivity(intent);
        }
    }
}
