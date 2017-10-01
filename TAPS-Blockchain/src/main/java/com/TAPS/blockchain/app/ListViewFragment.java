package com.TAPS.blockchain.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.TAPS.blockchain.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.TAPS.blockchain.app.transferMoney.JSON;


public class ListViewFragment extends BaseRefreshFragment {

    private PullToRefreshView mPullToRefreshView;
    View balanceView;
    DataHolder holder;
    SampleAdapter mAdapter;
    SharedPreferences prefs;
    CoordinatorLayout coord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_view, container, false);
        prefs = getActivity().getSharedPreferences("TAPS", MODE_PRIVATE);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        mAdapter = new SampleAdapter(getActivity(), R.layout.list_item, mSampleList);
        listView.setAdapter(mAdapter);
        coord = (CoordinatorLayout) rootView.findViewById(R.id
                .coordinatorLayout);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //./getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0D66AB")));
        /*
        DatabaseReference ref = database.getReference(prefs.getString("myPhone","sdads")+ "/request");
        ref.addChildEventListener(new ChildEventListener() {

                                      @Override
                                      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                          showNotification(dataSnapshot.getKey(),dataSnapshot.getValue().toString());
                                      }

                                      @Override
                                      public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                          showNotification(dataSnapshot.getKey(),dataSnapshot.getValue().toString());
                                      }

                                      @Override
                                      public void onChildRemoved(DataSnapshot dataSnapshot) {

                                      }

                                      @Override
                                      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                      }

                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {

                                      }
                                  });

        */


        holder = new DataHolder();
        holder.setBalanceAmt(prefs.getString("BalanceAmt","$0"));
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("TAPS", MODE_PRIVATE).edit();
        //0 for local transaction
        //1 for blockchain transaction
        editor.putString("BalanceAmt", "$153.20");
        editor.putString("transactionMode", "0");
        editor.putString("myPhone", "Dev Archan");
        editor.putString("myID", "user_type1_1");
        editor.putString("hisPhone", "Jason Thye");
        editor.putString("chaincode", "a0638500f65ede3bc74fcd54bf23d8a616e4a1f693c4c8e35cd85b8d2cc78c208ccb8fb6ab244c389dddf0fd390d3420bb30e3faf5008d1dcf0a1fe3a5df9be1");
        editor.commit();

        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);

                getBalance();
            }
        });

        return rootView;
    }

    public void showNotification(String from, String amount) {

        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound("D", generator.getColor(from));
        Intent intent = new Intent(getActivity(),transferMoney.class);
        Bundle bund = new Bundle();
        bund.putString("Amount", amount);
        bund.putString("Key", "Transfer");


        intent.putExtras(bund);
        Log.i("Hello in" , intent.getExtras().getString("Amount"));
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(getContext())
                .setTicker("New Pending Request")
                .setSmallIcon(R.drawable.send_out_money)
                .setContentTitle("Pending Request")
                .setContentText(from + " has just requests " + amount + " from you. ")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public void openTransactionHistory(View view){

        Intent intent = new Intent(getActivity(),transactionListView.class);
        startActivity(intent);
    }

    public void openTransferMoney(String purpose){

        Intent intent = new Intent(getActivity(),transferMoney.class);
        String action = purpose;
        intent.putExtra("Key",action);
        Log.i("Hello in" , "wala");
        startActivityForResult(intent,123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String code = "1231";
        if(data != null)
        {
            code = data.getStringExtra("101");
        }
        switch(code) {
            case "100" : {
                if (resultCode == Activity.RESULT_OK) {
                    getBalance();
                    Snackbar snackbar = Snackbar
                            .make(coord, "Processing Transaction", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
            }
            case "1000":
                if (resultCode == Activity.RESULT_OK) {
                    getBalance();
                    Snackbar snackbar = Snackbar
                            .make(coord, "Request Sent", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    showNotification("Jason Thye", prefs.getString("ReqAmt", "0"));
                }
                break;
        }
    }


        class SampleAdapter extends ArrayAdapter<Map<String, Integer>> {

        public static final String KEY_ICON = "icon";
        public static final String KEY_COLOR = "color";

        private final LayoutInflater mInflater;
        private final List<Map<String, Integer>> mData;

        public SampleAdapter(Context context, int layoutResourceId, List<Map<String, Integer>> data) {
            super(context, layoutResourceId, data);
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                switch (position) {
                    case 0:
                        convertView = mInflater.inflate(R.layout.list_item, parent, false);
                        balanceView = convertView;
                        viewHolder.balanceIcon = (ImageView) convertView.findViewById(R.id.balanceRightIcon);
                        viewHolder.historyIcon = (ImageView) convertView.findViewById(R.id.historyRightIcon);
                        TextView amountBalance = (TextView) balanceView.findViewById(R.id.balanceNumber);
                        amountBalance.setText(prefs.getString("BalanceAmt", "$0"));
                        convertView.findViewById(R.id.seeHistory).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openTransactionHistory(view);
                            }
                        });
                        convertView.findViewById(R.id.manageBal).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {openTransferMoney("Transfer");}
                        });
                        convertView.setTag(viewHolder);
                        break;
                    case 1:
                        convertView = mInflater.inflate(R.layout.list_item2, parent, false);
                        viewHolder.sendIcon = (ImageView) convertView.findViewById(R.id.sendIcon);
                        viewHolder.reqIcon = (ImageView) convertView.findViewById(R.id.reqIcon);
                        convertView.setTag(viewHolder);
                        viewHolder.sendIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openTransferMoney("Transfer");
                        }
                    });
                        viewHolder.reqIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openTransferMoney("Request");
                            }
                        });
                        break;
                    case 2:
                        convertView = mInflater.inflate(R.layout.list_item3, parent, false);
                        viewHolder.diningIcon = (ImageView) convertView.findViewById(R.id.diningIcon);
                        viewHolder.shoppingIcon = (ImageView) convertView.findViewById(R.id.shoppingIcon);
                        viewHolder.parkingIcon = (ImageView) convertView.findViewById(R.id.parkingIcon);
                        convertView.setTag(viewHolder);
                        break;
                }

            } else {

                viewHolder = (ViewHolder) convertView.getTag();

            }

            switch (position) {
                case 0:
                    viewHolder.balanceIcon.setImageResource(R.drawable.scale);
                    viewHolder.historyIcon.setImageResource(R.drawable.scale);
                    convertView.setBackgroundResource(R.color.saffron);
                    break;
                case 1:
                    viewHolder.sendIcon.setImageResource(R.drawable.scale_send);
                    viewHolder.reqIcon.setImageResource(R.drawable.scale_req);
                    convertView.setBackgroundResource(R.color.eggplant);
                    break;
                case 2:
                    viewHolder.diningIcon.setImageResource(R.drawable.scale_dining);
                    viewHolder.shoppingIcon.setImageResource(R.drawable.scale_shopping);
                    viewHolder.parkingIcon.setImageResource(R.drawable.scale_parking);
                    convertView.setBackgroundResource(R.color.sienna);
                    viewHolder.diningIcon.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {

                            Callback result = new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Toast.makeText(getContext(), "No Internet Access",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        String responseStr = response.body().string();
                                        Log.i("BlockchainJson",responseStr);
                                        getBalance();

                                    } else {
                                        Toast.makeText(getContext(),"Transaction Failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            //Add $50 if long press
                            if (Integer.parseInt(prefs.getString("transactionMode","0")) == 0) {
                                // App local transaction
                                Double amount = Double.parseDouble(prefs.getString("BalanceAmt", "0").substring(1)) + 50.00;
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("TAPS", MODE_PRIVATE).edit();
                                DecimalFormat df2 = new DecimalFormat("0.00");
                                editor.putString("BalanceAmt", "$"+ df2.format(amount));
                                editor.apply();
                                getBalance();

                            } else if (Integer.parseInt(prefs.getString("transactionMode","0")) == 1) {
                                // Transact using blockchain
                                blockchainTransfer(50.00,result);
                            }

                            return true;
                        }
                    });
                    break;
            }

            return convertView;
        }

        class ViewHolder {
            ImageView balanceIcon;
            ImageView historyIcon;
            ImageView sendIcon;
            ImageView reqIcon;
            ImageView diningIcon;
            ImageView shoppingIcon;
            ImageView parkingIcon;


        }

        public void blockchainTransfer(Double transferamount, Callback callback) {

            JSONObject parent = new JSONObject();
            try {
                JSONObject paramsChild = new JSONObject();
                paramsChild.put("type",1);

                JSONObject chaincodeIDChild = new JSONObject();
                chaincodeIDChild.put("name", prefs.getString("chaincode","sda"));
                paramsChild.put("chaincodeID",chaincodeIDChild);

                JSONArray args = new JSONArray();
                args.put(prefs.getString("myPhone","sdad"));
                args.put(prefs.getString("hisPhone","sdad"));
                args.put(transferamount.toString());

                JSONObject function = new JSONObject();
                function.put("function","invoke");

                JSONObject ctorMsgChild = new JSONObject();
                ctorMsgChild.put("function","invoke");
                ctorMsgChild.put("args", args);

                paramsChild.put("ctorMsg",ctorMsgChild);

                paramsChild.put("secureContext", "user_type1_0");

                parent = new JSONObject();
                parent.put("jsonrpc","2.0");
                parent.put("method","invoke");
                parent.put("params",paramsChild);
                parent.put("id", 3);
                //Log.i("BlockchainJson",parent.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            post("https://75f1e34d4a174da68fb58398caf40a88-vp0.us.blockchain.ibm.com:5001/chaincode", parent.toString(), callback);

        }

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


    public void getBalance() {

        if (Integer.parseInt(prefs.getString("transactionMode","0")) == 0) {
            TextView amountBalance = (TextView) balanceView.findViewById(R.id.balanceNumber);
            amountBalance.setText(prefs.getString("BalanceAmt", "$0"));

        } else if (Integer.parseInt(prefs.getString("transactionMode","0")) == 1) {
            String json = "{\n" +
                    "  \"jsonrpc\": \"2.0\",\n" +
                    "  \"method\": \"query\",\n" +
                    "  \"params\": {\n" +
                    "    \"type\": 1,\n" +
                    "    \"chaincodeID\": {\n" +
                    "      \"name\": \"a0638500f65ede3bc74fcd54bf23d8a616e4a1f693c4c8e35cd85b8d2cc78c208ccb8fb6ab244c389dddf0fd390d3420bb30e3faf5008d1dcf0a1fe3a5df9be1\"\n" +
                    "    },\n" +
                    "    \"ctorMsg\": {\n" +
                    "      \"function\": \"read\",\n" +
                    "      \"args\": [\n" +
                    "        \"Dev Archan\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    \"secureContext\": \"user_type1_1\"\n" +
                    "  },\n" +
                    "  \"id\": 2\n" +
                    "}";

            post("https://75f1e34d4a174da68fb58398caf40a88-vp0.us.blockchain.ibm.com:5001/chaincode", json ,
                    new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            //Toast.makeText(getApplicationContext(), "No Internet Access",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                try {
                                    JSONObject jsonObj = new JSONObject(responseStr);
                                    JSONObject jsonObj2 = new JSONObject(jsonObj.get("result").toString());
                                    holder.setBalanceAmt("$"+ String.format("%.2f", Double.valueOf(jsonObj2.get("message").toString())));
                                    Log.i("BlockchainJson123",jsonObj2.get("message").toString());
                                    //do something with content string
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("BlockchainJson",responseStr);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                                        TextView amountBalance = (TextView) balanceView.findViewById(R.id.balanceNumber);
                                        amountBalance.setText(holder.getBalanceAmt());
                                    }
                                });

                            } else {
                                Toast.makeText(getContext(),"Transaction Failed",Toast.LENGTH_LONG).show();
                                Log.i("HELLO", "Transaction Failed");
                            }
                        }
                    });
        }

    }

    class DataHolder {

        String balanceAmt;

        public void DataHolder(){}

        public String getBalanceAmt() {
            return balanceAmt;
        }

        public void setBalanceAmt(String balanceAmt) {
            this.balanceAmt = balanceAmt;
        }
    }


}
