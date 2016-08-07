package id.technomotion.hujanndakya;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView textViewStatus, textViewDescription;
    ImageView imageViewIcon;
    Button buttonRefresh;
    String API_KEY="5f2b8b0da584f46f49235c65b7f9576c";
    String KOTA = "yogyakarta";
    String urlRequest = "http://api.openweathermap.org/data/2.5/weather?q="+KOTA+"&appid="+API_KEY;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        imageViewIcon = (ImageView) findViewById(R.id.imageViewIcon);
        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadWeather();
            }
        });
        reloadWeather();
    }
    private void reloadWeather() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println(response);
                        processResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }
    private void processResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);
            String status = weatherObject.getString("main");
            String description = weatherObject.getString("description");
            refreshUI(status,description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshUI(String status, String description) {
        textViewStatus.setText(status);
        textViewDescription.setText(description);
        if(status.equalsIgnoreCase("clear")){
            imageViewIcon.setImageResource(R.drawable.art_clear);
        }
    }
}
