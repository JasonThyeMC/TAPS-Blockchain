package com.TAPS.blockchain.app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by thyemunchun on 15/11/2016.
 */

public class BlockchainUtility {

    Context context;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void BlockchainUtility(Context context){
        this.context = context;
    }

    public void blockchainTransferJson(Double transferamount) {

        boolean result;
        JSONObject parent = new JSONObject();

        try {
            JSONObject paramsChild = new JSONObject();
            paramsChild.put("type",1);

            JSONObject chaincodeIDChild = new JSONObject();
            chaincodeIDChild.put("name", "abc771269cc5389fa8a4b9d0380de625d1f03622749d7e76ebb2d5b6f65895a575f5dece5e9d83a35ccf668cbf5a15ca9003e44bf4fcacd636769cfc09663a20");
            paramsChild.put("chaincodeID",chaincodeIDChild);

            JSONArray args = new JSONArray();
            args.put("hi");
            args.put("there");
            args.put(transferamount.toString());

            JSONObject function = new JSONObject();
            function.put("function","invoke");

            JSONObject ctorMsgChild = new JSONObject();
            ctorMsgChild.put("function","invoke");
            ctorMsgChild.put("args", args);

            paramsChild.put("ctorMsg",ctorMsgChild);

            paramsChild.put("secureContext", "user_type1_0");

            parent.put("jsonrpc","2.0");
            parent.put("method","invoke");
            parent.put("params",paramsChild);
            parent.put("id", 3);
            //Log.i("BlockchainJson",parent.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Callback results = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "No Internet Access",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.i("BlockchainJson",responseStr);
                    Toast.makeText(context,"Transaction Failed",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"Transaction Failed",Toast.LENGTH_SHORT).show();
                }
            }
        };

        post("https://75f1e34d4a174da68fb58398caf40a88-vp0.us.blockchain.ibm.com:5001/chaincode", parent.toString(),results);


    }

    Call post(String url, String json, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}
