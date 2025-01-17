package com.example.sli;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class speakactivity extends AppCompatActivity {
    WebView webView;
    EditText speechtext;
    ArrayList<String> buff_data=new ArrayList<>();
    OkHttpClient client = new OkHttpClient();
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speakactivity);
        String giphy_url="file:///android_asset/index.html";
        ImageView mic=findViewById(R.id.micimgview);
        speechtext=findViewById(R.id.speechedittext);
        SpeechRecognizer speechRecognizer;
        webView=findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(giphy_url);
        webView.setWebViewClient(new WebViewClient());
        buff_data.add("Hello");
        load_gif(buff_data);
        speechtext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER){
                    buff_data.remove(0);
                    buff_data.add(speechtext.getText().toString());
                    load_gif(buff_data);
                    return true;
                }
                return false;
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.RECORD_AUDIO
            },1);
        }
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechrecognizerintent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        final int[] count = {0};
        mic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(count[0] ==0){
                    mic.setImageDrawable(getDrawable(R.drawable.redmic));
                    speechRecognizer.startListening(speechrecognizerintent);
                    count[0] =1;
                }
                else{
                    mic.setImageDrawable(getDrawable(R.drawable.micon));
                    speechRecognizer.stopListening();
                    count[0] =0;
                }
            }
        });
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {


            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data =results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                speechtext.setText(data.get(0));
                load_gif(data);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT);
            }
            else{
//                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT);
            }

        }
    }

    public void load_gif(ArrayList<String> data){
        String url="file:///android_asset/index.html";
        String searchterm= data.get(0);
        speechtext.setText(searchterm);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                webView.loadUrl("javascript:spellcheckngify('" + searchterm + "')");

            }
        });

    }




}