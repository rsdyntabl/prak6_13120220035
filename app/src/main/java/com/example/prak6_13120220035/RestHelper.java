package com.example.prak6_13120220035;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestHelper {
    private Context context;
    private RequestQueue requestQueue;
    private final int REQ_METHOD = Request.Method.POST;
    private final String URL = "http:// 192.168.1.18/praktikum5/";
    private ArrayList<Mahasiswa> arrListMhs = new ArrayList<>();

    public RestHelper(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }
    private void tampilpesan (Context context, String teks) {
        Toast.makeText(context, "teks", Toast.LENGTH_SHORT).show();
    }
    public void insertData (JSONObject jsonObjectMhs) {
        JsonObjectRequest jsonObjectRequest;
        Response.Listener<JSONObject> jsonObjectListener;
        Response.ErrorListener errorListener;

        jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("hasil") == 1) {
                        tampilpesan(context, "Data berhasil disimpan...");
                    }
                    else {
                        tampilpesan(context, "Data gagal tersimpan...");
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tampilpesan(context, error.toString());
            }
        };
        jsonObjectRequest = new JsonObjectRequest(
                REQ_METHOD,
                URL + "InsertData.php",
                jsonObjectMhs,
                jsonObjectListener,
                errorListener
        );
        requestQueue.add(jsonObjectRequest);
    }
    public void getDataMhs(final RestCallbackMahasiswa callbackMahasiswa) {
        arrListMhs.clear();

        JsonArrayRequest jsonArrayRequest;
        Response.Listener<JSONArray> jsonArrayListener;
        Response.ErrorListener errorListener;

        jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        arrListMhs.add(new Mahasiswa(
                                response.getJSONObject(i).getString("stb"),
                                response.getJSONObject(i).getString("nama"),
                                response.getJSONObject(i).getInt("angkatan")
                        ));
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    callbackMahasiswa.requestDataMhsSuccess(arrListMhs);
                }
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tampilpesan(context, error.toString());
            }
        };
        jsonArrayRequest = new JsonArrayRequest(
                REQ_METHOD,
                URL + "TampilData.php",
                null,
                jsonArrayListener,
                errorListener
        );
        requestQueue.add(jsonArrayRequest);
    }
    public void editData (String stb, Mahasiswa mhs) {
        JsonObjectRequest jsonObjectRequest;
        JSONObject jsonObject;
        Response.Listener<JSONObject> jsonObjectListener;
        Response.ErrorListener errorListener;

        jsonObject = new JSONObject();
        try {
            jsonObject.put("stb_lama", stb);
            jsonObject.put("stb", mhs.getStb());
            jsonObject.put("nama", mhs.getNama());
            jsonObject.put("angkatan", mhs.getAngkatan());
        } catch (JSONException e){
            e.printStackTrace();
        }
        System.out.println("data json: " + jsonObject.toString());
        jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("hasil") ==1){
                        tampilpesan(context, "Data berhasil disimpan...");
                    }
                    else{
                        tampilpesan(context, "Data Gagal Tersimpan...");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tampilpesan(context, error.toString());
            }
        };
        jsonObjectRequest = new JsonObjectRequest(
                REQ_METHOD,
                URL + "EditData.php",
                jsonObject,
                jsonObjectListener,
                errorListener
        );
        requestQueue.add(jsonObjectRequest);
    }
    public void hapusData (String stb, final RestCallbackMahasiswa callbackMahasiswa) {
        JsonObjectRequest jsonObjectRequest;
        JSONObject jsonObject;
        Response.Listener<JSONObject> jsonObjectListener;
        Response.ErrorListener errorListener;

        jsonObject = new JSONObject();
        try {
            jsonObject.put("stb", stb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("data json: " + jsonObject.toString());
        jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("hasil")==1){
                        tampilpesan(context, "Data Berhasil Disimpan...");
                    }
                    else {
                        tampilpesan(context, "Data Gagal Dihapus...");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getDataMhs(callbackMahasiswa);
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tampilpesan(context, error.toString());
            }
        };
        jsonObjectRequest = new JsonObjectRequest(
                REQ_METHOD,
                URL + "HapusData.php",
                jsonObject,
                jsonObjectListener,
                errorListener
        );
        requestQueue.add(jsonObjectRequest);
    }
}
