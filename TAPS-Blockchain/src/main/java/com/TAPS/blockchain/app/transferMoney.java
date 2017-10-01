package com.TAPS.blockchain.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class transferMoney extends AppCompatActivity implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2 {

    TextView tv;
    Double transferamount = 0.00;
    SharedPreferences prefs;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_money);
        prefs = getSharedPreferences("TAPS", MODE_PRIVATE);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.transferMoney));

        TextView action = (TextView) findViewById(R.id.transactionAction);
        action.setText(getIntent().getExtras().getString("Key"));
        Button sendNow = (Button) findViewById(R.id.actionBtn);
        tv = (TextView) findViewById(R.id.amount);
        Spannable spannable = new SpannableString(String.valueOf("$0.00"));
        spannable.setSpan(new RelativeSizeSpan(2f), 1, 5 ,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(spannable);
        //Log.i("Hello", bund.getString("Amount"));


        if (getIntent().getExtras().getString("Key").equals("Transfer")) {
            TextView tv = (TextView) findViewById(R.id.sourceFund);
            TextView bank = (TextView) findViewById(R.id.transactionDuration);
            TextView contact = (TextView) findViewById(R.id.destinationFund);
            TextView availableFunds = (TextView) findViewById(R.id.availableFunds);
            TextView Amount = (TextView) findViewById(R.id.amount);
            if (getIntent().getExtras().getString("Amount") != null) {
                String amt = getIntent().getExtras().getString("Amount");
                Spannable spannable1 = new SpannableString(amt);
                spannable1.setSpan(new RelativeSizeSpan(2f), 1, amt.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                Amount.setText(spannable1);
                transferamount = Double.valueOf(amt.substring(1));
            }
            availableFunds.setText("Available " + prefs.getString("BalanceAmt", "0") + " MYD");
            contact.setText(prefs.getString("hisPhone","asddasd"));

            tv.setText("TAPS Account");
            bank.setText("Maybank");
            findViewById(R.id.fromRIght).setVisibility(View.INVISIBLE);
            sendNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (transferamount != 0) {
                        if (Integer.parseInt(prefs.getString("transactionMode","0")) == 0) {
                            // App local transaction
                            Double amount = Double.parseDouble(prefs.getString("BalanceAmt", "0").substring(1)) - transferamount;
                            Log.i("Hello", transferamount.toString());
                            SharedPreferences.Editor editor = getSharedPreferences("TAPS", MODE_PRIVATE).edit();
                            DecimalFormat df2 = new DecimalFormat("0.00");
                            editor.putString("BalanceAmt", "$"+ df2.format(amount));
                            editor.apply();
                            transferAction();

                        } else if (Integer.parseInt(prefs.getString("transactionMode","0")) == 1) {
                            // Transact using blockchain
                            blockchainTransfer();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"How much do you want to send?",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            TextView contact = (TextView) findViewById(R.id.sourceFund);
            TextView bank = (TextView) findViewById(R.id.availableFunds);
            bank.setText("CIMB Bank");
            contact.setText(prefs.getString("hisPhone","asddasd"));
            TextView tv = (TextView) findViewById(R.id.destinationFund);
            TextView recipient = (TextView) findViewById(R.id.transactionDuration);
            recipient.setText("TAPS");
            tv.setText("Balance Account");
            findViewById(R.id.toRight).setVisibility(View.INVISIBLE);
            sendNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (transferamount != 0) {
                        SharedPreferences.Editor editor = getSharedPreferences("TAPS", MODE_PRIVATE).edit();
                        DecimalFormat df2 = new DecimalFormat("0.00");
                        editor.putString("ReqAmt", "$"+ df2.format(transferamount));
                        editor.apply();
                        requestAction();
                    } else {
                        Toast.makeText(getApplicationContext(),"How much do you want to send?",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Button chgAmount = (Button) findViewById(R.id.button);
        chgAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setPlusMinusVisibility(View.INVISIBLE)
                        .setLabelText("RM.");
                npb.show();
                //npb.addNumberPickerDialogHandler(NumberPickerDialogFragment.NumberPickerDialogHandlerV2);
            }
        });
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        if (fullNumber.compareTo(BigDecimal.ZERO) == 1) {
            transferamount = fullNumber.doubleValue();
            Log.i("Hello", transferamount.toString());
            Log.i("Hello", String.valueOf(String.valueOf(transferamount).length()));
            DecimalFormat df2 = new DecimalFormat("0.00");
            Spannable spannable = new SpannableString(String.valueOf("$") + df2.format(transferamount));
            Log.i("Hello", String.valueOf(String.valueOf(df2.format(transferamount)).length()));
            spannable.setSpan(new RelativeSizeSpan(2f), 1, String.valueOf(transferamount).length() + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            tv.setText(spannable);
        }
    }

    public void requestAction() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(prefs.getString("hisPhone","sadda")+"/request");
        myRef.child(prefs.getString("myPhone","sadda")).setValue(transferamount.toString());
        //Snackbar to indicate action is done
        Intent resultIntent = new Intent();
        resultIntent.putExtra("101", "1000");
        setResult(Activity.RESULT_OK, resultIntent);
        this.finish();
    }

    public void transferAction() {

        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("dd");
        String d = s.format(new Date());
        Format formatter = new SimpleDateFormat("MMM");
        String ss = formatter.format(new Date());
        DatabaseReference myRef = database.getReference("user1/transactions").child(ss+ " " +d).push();
        String negativeAmt = String.valueOf(transferamount*-1);
        TransactionDetail tx = new TransactionDetail("Dev Archan",transferamount.toString());
        myRef.setValue(tx);
        */
        //Snackbar to indicate action is done
        Intent resultIntent = new Intent();
        resultIntent.putExtra("101", "100");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void blockchainTransfer() {

        SharedPreferences prefs = getSharedPreferences("TAPS", MODE_PRIVATE);

        JSONObject parent = new JSONObject();
        try {
            JSONObject paramsChild = new JSONObject();
            paramsChild.put("type",1);

            JSONObject chaincodeIDChild = new JSONObject();
            chaincodeIDChild.put("name", prefs.getString("chaincode","sda"));
            paramsChild.put("chaincodeID",chaincodeIDChild);

            JSONArray args = new JSONArray();
            args.put(prefs.getString("myPhone","Jasdfsdfon Thye"));
            args.put(prefs.getString("hisPhone","sdfs Thye"));
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

        post("https://75f1e34d4a174da68fb58398caf40a88-vp0.us.blockchain.ibm.com:5001/chaincode", parent.toString(),
                new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "No Internet Access",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.i("BlockchainJson",responseStr);
                    transferAction();
                } else {
                    Toast.makeText(getApplicationContext(),"Transaction Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    public class TransactionDetail {

        public String name;
        public String amount;

        public TransactionDetail(String name, String amount){
            this.name = name;
            this.amount =amount;
        }

        public String getName() {
            return name;
        }

        public String getAmount() {
            return amount;
        }
    }


}
