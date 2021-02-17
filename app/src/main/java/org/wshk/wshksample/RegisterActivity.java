package org.wshk.wshksample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    //RequestQueue for network operations
    RequestQueue queue;
    EditText emailEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        queue  = Volley.newRequestQueue(this);
        emailEditText = findViewById(R.id.emailET);
        passwordEditText = findViewById(R.id.passwordET);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordET);
    }

    public void onRegisterClicked(View v){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        this.register(email, password, confirmPassword);
    }

    void register(String email, String password, String confirmPassword){
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("email", email);
            requestObj.put("password", password);
        } catch (Exception e){

        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                WSHKAPI.registerString(), requestObj, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                //login success
                Toast.makeText(RegisterActivity.this, "Registration Success!", Toast.LENGTH_LONG).show();
                RegisterActivity.this.finish();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 400){
                    //user name not found
                    try {
                        JSONObject object = new JSONObject(new String(error.networkResponse.data));
                        Toast.makeText(RegisterActivity.this, object.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("WSHKApp", new String(error.networkResponse.data));
            }
        });
        queue.add(jsonObjectRequest);
    }
}