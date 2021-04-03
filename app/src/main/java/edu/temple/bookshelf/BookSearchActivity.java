package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class BookSearchActivity extends AppCompatActivity {

    EditText editText;
    Button search;
    Button cancel;


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            String title;
            String author;
            String cover_url;
            Bundle bundle;
            int id;

            try {

                JSONArray jsonArray = new JSONArray((String)msg.obj); // json array of json objects
                JSONObject jsonObject = jsonArray.getJSONObject(0); //convert the array object in the index to a object

                Log.d("String", "48: msg "+jsonObject.getString("title"));

                title = (jsonObject.getString("title"));
                author = jsonObject.getString("author");
                cover_url = jsonObject.getString("cover_url");
                id = jsonObject.getInt("id");
                //Picasso.get().load(Uri.parse(jsonObject.getString("img"))).into(comicImageView);
                bundle= new Bundle();
                bundle.putString("title", title);
                bundle.putString("author", author);
                bundle.putString("cover_url", cover_url);
                bundle.putInt("id", id);
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