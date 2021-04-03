package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class BookSearchActivity extends Activity { //make sure to get rid of appcombat and make it just activity

    EditText editText;
    Button search;
    Button cancel;


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            Bundle bundle;
            try {

                JSONArray jsonArray = new JSONArray((String)msg.obj); // json array of json objects
                ArrayList<JSONObject> objects = new ArrayList<JSONObject>();
                bundle= new Bundle();
                //JSONObject jsonObject = jsonArray.getJSONObject(0); //convert the array object in the index to a object
                for(int i =0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    objects.add(jsonObject);
                }
                bundle.putParcelableArrayList("Objects", (ArrayList<? extends Parcelable>) objects);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("BUNDLE",bundle);
                setResult(RESULT_OK, resultIntent); // need to place result Intent in setResult or else intent later is null in calling class
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;


        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);
        Log.d("String", "BookSearch Line: 75");

        Intent intent = getIntent();
        editText = findViewById(R.id.editText);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        Log.d("String", "BookSearch Line: 87");
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            String urlString = "https://Kamorris.com/lab/cis3515/search.php?term=" + editText.getText().toString();
                            Log.d("String", "line: 95: "+urlString);
                            URL url = new URL(urlString);
                            Log.d("String", "onClick: 71");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())); //only good for reading text data

                            Message msg = Message.obtain();
                            StringBuilder builder = new StringBuilder(); //allows easy concate of strings
                            String tmpString;

                            while((tmpString = reader.readLine()) != null){
                                //Log.d("String", "Line: 106");
                                builder.append(tmpString);
                            }
                            Log.d("String", "BookSearch Line: 110");
                            msg.obj = builder.toString(); //only reads one line // apply builder here

                            handler.sendMessage(msg);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        });



    }
}