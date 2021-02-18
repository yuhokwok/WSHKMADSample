package org.wshk.wshksample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemberListFragment extends Fragment {

    ListView lv;
    RequestQueue queue;

    //this is not a good practice, it would be better to use customized data model
    //and custom adapter (you will get higher marks)
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Integer> ids = new ArrayList<Integer>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = view.findViewById(R.id.listview);
        if (queue == null){
            queue  = Volley.newRequestQueue(this.getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //load the data from the backend
        this.loadUsers();
    }

    public void displayList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_list_item_1, names);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int userId = ids.get(position);
                Intent i = new Intent(MemberListFragment.this.getActivity(), MemberDetailActivity.class);
                i.putExtra("userId", userId);
                startActivity(i);
            }
        });
    }

    public void loadUsers() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WSHKAPI.listUsers(), null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                //login success
                JSONArray array = null;
                try {
                    //remove all
                    names.clear();
                    ids.clear();
                    array = response.getJSONArray("data");
                    for(int i =0; i< array.length(); i++){
                        JSONObject member = array.getJSONObject(i);
                        String name = member.getString("first_name") + " " + member.getString("last_name").toUpperCase();
                        Integer id = member.getInt("id");
                        names.add(name);
                        ids.add(id);
                        displayList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 400){
                    try {
                        JSONObject object = new JSONObject(new String(error.networkResponse.data));
                        Toast.makeText(MemberListFragment.this.getContext(),
                                object.getString("error"), Toast.LENGTH_LONG).show();
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