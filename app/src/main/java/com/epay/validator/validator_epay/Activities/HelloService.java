package com.epay.validator.validator_epay.Activities;

import android.app.Service;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.epay.validator.validator_epay.localDatabase.DBManager;
import com.epay.validator.validator_epay.Network.End_Points;
/**
 * Created by Rizwan Butt on 05-May-17.
 */

public class HelloService extends Service {

    private DBManager dbManager;

    String id,route_code,route_name,route_start,route_destination,route_added_date,time,route_added_by,route_updated_date,route_updated_by;
    String fare_id,fare_route,fare_price,fare_type,added_by,update_by,date_added,date_updated;
    String c_id,c_customer_id,c_customer_balance;
    String h_id,h_route_id,h_user_id,h_person_travling,h_date_added,h_date_modified;

    String customer_id;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HelloService.this);

        customer_id=preferences.getString("id",null);

        getLiveData();
        /*getData();
        getFareTableData();*/
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
    public void getLiveData() {

        dbManager = new DBManager(HelloService.this);
        dbManager.open();

        // loading = ProgressDialog.show(getApplicationContext(), "", "Please wait..", false, false);

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_LIVE_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // loading.dismiss();

                try {
                    JSONObject main_object = new JSONObject(response);
                    JSONArray routes = main_object.getJSONArray("routes");
                    for (int i = 0; i < routes.length(); i++) {
                        JSONObject object = routes.getJSONObject(i);
                        id=object.getString("id");
                        route_code=object.getString("route_code");
                        route_name=object.getString("route_name");
                        route_start=object.getString("rout_start");
                        route_destination=object.getString("rout_destination");
                        route_added_date=object.getString("route_added_date");
                        time=object.getString("time");
                        route_added_by=object.getString("route_added_by");
                        route_updated_date=object.getString("route_updated_date");
                        route_updated_by=object.getString("route_updated_by");

                        dbManager.insert_into_routes(id,route_code,route_name,route_start,route_destination,route_added_date,time,route_added_by,route_updated_date,route_updated_by);

                    }

                    JSONArray fare = main_object.getJSONArray("fare");
                    for (int i = 0; i < fare.length(); i++) {
                        JSONObject object = fare.getJSONObject(i);
                        fare_id=object.getString("Fare_ID");
                        fare_route=object.getString("Fare_route");
                        fare_price=object.getString("Fare_price");
                        fare_type=object.getString("Fare_type");
                        added_by=object.getString("added_by");
                        update_by=object.getString("update_by");
                        date_added=object.getString("date_added");
                        date_updated=object.getString("date_updated");
                        dbManager.insert_into_fare(fare_id,fare_route,fare_price,fare_type,added_by,update_by,date_added,date_updated);

                    }
                    JSONArray balance = main_object.getJSONArray("balance");
                    for (int i = 0; i < balance.length(); i++) {
                        JSONObject object = balance.getJSONObject(i);
                        c_id = object.getString("id");
                        c_customer_id = object.getString("customer_id");
                        c_customer_balance = object.getString("customer_balance");
                        dbManager.insert_into_customer_accounts(c_id, c_customer_id, c_customer_balance);
                    }
                    JSONArray history = main_object.getJSONArray("history");
                    for (int i = 0; i < history.length(); i++) {
                        JSONObject object = history.getJSONObject(i);
                        h_id=object.getString("id");
                        h_route_id=object.getString("route_id");
                        h_user_id=object.getString("user_id");
                        h_person_travling=object.getString("person_travling");
                        h_date_added=object.getString("date_added");
                        h_date_modified=object.getString("date_modified");
                        dbManager.insert_into_history_travel(h_route_id,h_user_id,h_person_travling,h_date_added,h_date_modified);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Not inserted",Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("customer_id",customer_id);
                map.put("user_id",customer_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HelloService.this);
        requestQueue.add(request);

    }
    public void getData() {
        dbManager = new DBManager(HelloService.this);
        dbManager.open();

       // loading = ProgressDialog.show(getApplicationContext(), "", "Please wait..", false, false);

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETDATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

               // loading.dismiss();

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = new JSONObject(array.getString(i));
                        id=object.getString("id");
                        route_code=object.getString("route_code");
                        route_name=object.getString("route_name");
                        route_start=object.getString("rout_start");
                        route_destination=object.getString("rout_destination");
                        route_added_date=object.getString("route_added_date");
                        time=object.getString("time");
                        route_added_by=object.getString("route_added_by");
                        route_updated_date=object.getString("route_updated_date");
                        route_updated_by=object.getString("route_updated_by");

                        dbManager.insert_into_routes(id,route_code,route_name,route_start,route_destination,route_added_date,time,route_added_by,route_updated_date,route_updated_by);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Not inserted",Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HelloService.this);
        requestQueue.add(request);

    }
    public void getFareTableData() {
        dbManager = new DBManager(HelloService.this);
        dbManager.open();

        // loading = ProgressDialog.show(getApplicationContext(), "", "Please wait..", false, false);

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_FARE_TABLE_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // loading.dismiss();

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = new JSONObject(array.getString(i));
                        fare_id=object.getString("Fare_ID");
                        fare_route=object.getString("Fare_route");
                        fare_price=object.getString("Fare_price");
                        fare_type=object.getString("Fare_type");
                        added_by=object.getString("added_by");
                        update_by=object.getString("update_by");
                        date_added=object.getString("date_added");
                        date_updated=object.getString("date_updated");
                        dbManager.insert_into_fare(fare_id,fare_route,fare_price,fare_type,added_by,update_by,date_added,date_updated);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Not inserted",Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HelloService.this);
        requestQueue.add(request);

    }

}
