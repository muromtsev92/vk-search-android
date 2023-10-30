package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.androidapp.utils.NetworkUtils.generateURL;
import static com.example.androidapp.utils.NetworkUtils.getResponseFromUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;
    private TextView result;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    private void showResultTextView() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorTextView() {
        errorMessage.setVisibility(View.VISIBLE);
        result.setVisibility(View.INVISIBLE);

    }

    class VkQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute(){
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String id = null;
            String firstName = null;
            String lastName = null;

            if(response != null && !response.equals("")){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    JSONObject userInfo = jsonArray.getJSONObject(0);

                    id = userInfo.getString("id");
                    firstName = userInfo.getString("first_name");
                    lastName = userInfo.getString("last_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String summary = "Id: " + id + "\n" + "Name: " + firstName + "\n" + "Last Name: " + lastName + "\n";

                result.setText(summary);

                showResultTextView();
            } else {
                showErrorTextView();
            }
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchButton = findViewById(R.id.b_search_vk);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL generatedURL = generateURL(searchField.getText().toString());

                new VkQueryTask().execute(generatedURL);
            }
        };

        searchButton.setOnClickListener(onClickListener);
    }
}