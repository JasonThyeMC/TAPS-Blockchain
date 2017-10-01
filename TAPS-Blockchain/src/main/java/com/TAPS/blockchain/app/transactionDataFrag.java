package com.TAPS.blockchain.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;


public class transactionDataFrag extends Fragment {

    List<Object> transactions = new ArrayList<>();
    FirebaseDatabase database;
    SampleAdapter adapter;
    int i;

    public transactionDataFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             i = bundle.getInt("title", 1);
        }
        switch (i) {
            case 1:
                populate();
                break;
            case 2:
                populate1();
                break;
            case 3:
                populate2();
        }
/*        DatabaseReference myRef = database.getReference("user1/transactions/Nov 16");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TransactionDetail tx = dataSnapshot.getValue(TransactionDetail.class);
                transactions.add(new Transaction(tx.getName(), "Payment Sent", "dfsf", tx.getAmount()));
                Collections.reverse(transactions);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(postListener);

        myRef.child("transactions").addChildEventListener(new ChildEventListener() {

            String amt;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.getChildren().iterator().hasNext()){
                    Collections.reverse(transactions);
                    adapter.notifyDataSetChanged();
                }
                //DataSnapshot data = dataSnapshot.getChildren().iterator().next();
                transactions.add(new Transaction(dataSnapshot.getChildren().iterator().next().getKey(),
                        "Pending Payment", "dfs",
                        String.valueOf(dataSnapshot.getChildren().iterator().next().getValue())));



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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


    }

    private void populate() {

        this.transactions.add("NOV 16");
        this.transactions.add(new Transaction("Dev Archan", "Recurring Payment", "http://static.bookstck.com/books/einstein-his-life-and-universe-400.jpg","+$24.50"));
        this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg","-$29.50"));
        this.transactions.add(new Transaction("Jason Thye", "Money Received", "http://static.bookstck.com/books/tesla-inventor-of-the-electrical-age-400.jpg","+$47.00"));
        this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/orwells-revenge-400.jpg","+$58.30"));
        this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg","-$82.90"));
        this.transactions.add(new Transaction("Dev Archan", "Payment Sent", "http://static.bookstck.com/books/how-to-lie-with-statistics-400.jpg","-$72.80"));
        this.transactions.add("NOV 15");
        this.transactions.add(new Transaction("Dev Archan", "Payment Pending", "http://static.bookstck.com/books/abundance-400.jpg","+$183.90"));
        this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/turing-s-cathedral-400.jpg","-$58.20"));
        this.transactions.add(new Transaction("Dev Archan", "Payment Sent", "http://static.bookstck.com/books/where-good-ideas-come-from-400.jpg","+$46.00"));
        this.transactions.add(new Transaction("Jason Thye", "Money Received", "http://static.bookstck.com/books/the-information-history-theory-flood-400.jpg","+$71.90"));
        this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg","-$62.10"));
        this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/turing-s-cathedral-400.jpg","-$22.50"));

    }

    private void populate1() {

        this.transactions.add("NOV 16");
        this.transactions.add(new Transaction("Dev Archan", "Recurring Payment", "http://static.bookstck.com/books/einstein-his-life-and-universe-400.jpg","+$24.50"));
        //this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg
        this.transactions.add(new Transaction("Jason Thye", "Money Received", "http://static.bookstck.com/books/tesla-inventor-of-the-electrical-age-400.jpg","+$47.00"));
        this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/orwells-revenge-400.jpg","+$58.30"));
        //this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg","-$82.90"));
        //this.transactions.add(new Transaction("Dev Archan", "Payment Sent", "http://static.bookstck.com/books/how-to-lie-with-statistics-400.jp
        this.transactions.add("NOV 15");
        this.transactions.add(new Transaction("Dev Archan", "Payment Pending", "http://static.bookstck.com/books/abundance-400.jpg","+$183.90"));
        //this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/turing-s-cathedral-400.jpg","-$58.20"));
        this.transactions.add(new Transaction("Dev Archan", "Payment Sent", "http://static.bookstck.com/books/where-good-ideas-come-from-400.jpg","+$46.00"));
        this.transactions.add(new Transaction("Jason Thye", "Money Received", "http://static.bookstck.com/books/the-information-history-theory-flood-400.jpg","+$71.90"));
        //this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg","-$62.10"));
        //this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/turing-s-cathedral-4

    }

    private void populate2() {

        this.transactions.add("NOV 16");
        //this.transactions.add(new Transaction("Dev Archan", "Recurring Payment", "http://static.bookstck.com/books/einstein-his-life-and-universe-400.jpg","+
        this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg","-$29.50"));
        //this.transactions.add(new Transaction("Jason Thye", "Money Received", "http://static.bookstck.com/books/tesla-inventor-of-the-electrical-age-400.jpg"
        //this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/orwells-revenge-400.jpg
        this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg","-$82.90"));
        this.transactions.add(new Transaction("Dev Archan", "Payment Sent", "http://static.bookstck.com/books/how-to-lie-with-statistics-400.jpg","-$72.80"));
        this.transactions.add("NOV 15");
        //this.transactions.add(new Transaction("Dev Archan", "Payment Pending", "http://static.bookstck.com/books/abundance-400.jp
        this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/turing-s-cathedral-400.jpg","-$58.20"));
        //this.transactions.add(new Transaction("Dev Archan", "Payment Sent", "http://static.bookstck.com/books/where-good-ideas-come-from-400
        //this.transactions.add(new Transaction("Jason Thye", "Money Received", "http://static.bookstck.com/books/the-information-history-theory-flood
        this.transactions.add(new Transaction("Jason Thye", "Payment Sent", "http://static.bookstck.com/books/zero-to-one-400.jpg","-$62.10"));
        this.transactions.add(new Transaction("Dev Archan", "Pending Payment", "http://static.bookstck.com/books/turing-s-cathedral-400.jpg","-$22.50"));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_blank, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new transactionDataFrag.SampleAdapter(this.transactions);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Object> items;

        public class TransactionViewHolder extends SwipeToAction.ViewHolder<Object> {
            public TextView name;
            public TextView status;
            public SimpleDraweeView imageView;
            public TextView sectionText;
            public TextView amountText;

            public TransactionViewHolder(View v) {
                super(v);
                name = (TextView) v.findViewById(R.id.title);
                status = (TextView) v.findViewById(R.id.author);
                imageView = (SimpleDraweeView) v.findViewById(R.id.image);
                sectionText = (TextView) v.findViewById(R.id.sectionText);
                amountText = (TextView) v.findViewById(R.id.amount);
            }
        }

        public SampleAdapter(List<Object> items) {
            this.items = items;
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getItemViewType(int position) {

            if(getItem(position) instanceof Transaction) {
                return 1;
            }
            return 0;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;

            if(viewType == 1)
            {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.transaction_item, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.section_header, parent, false);
            }

            return new TransactionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder.getItemViewType() == 1) {
                Transaction item = (Transaction) items.get(position);
                TransactionViewHolder vh = (TransactionViewHolder) holder;

                vh.name.setText(item.getTitle());
                vh.status.setText(item.getAuthor());
                vh.amountText.setText(item.getAmount());

                if(item.getAmount().contains("-")){
                    vh.amountText.setTextColor(Color.parseColor("#e51c23"));
                    Log.i("Data 101", item.getAmount());
                }
                ColorGenerator generator = ColorGenerator.MATERIAL;
                TextDrawable drawable2;
                if(item.getTitle() == "Dev Archan"){
                    int color1 = generator.getColor(item.getTitle());
                     drawable2 = TextDrawable.builder()
                            .buildRound("D", color1);
                } else {
                    int color1 = generator.getColor(item.getTitle());
                     drawable2 = TextDrawable.builder()
                            .buildRound("J", color1);
                }

                vh.imageView.setImageDrawable(drawable2);
                vh.data = item;
            }

            if (holder.getItemViewType() == 0) {
                TransactionViewHolder vh = (TransactionViewHolder) holder;
                vh.sectionText.setText((String) items.get(position));
            }

            //holder.getItemViewType();
        }


    }

    public class Transaction {
        private String title;
        private String author;
        private String imageUrl;
        private String amount;

        public Transaction(String title, String author, String imageUrl, String amount) {
            this.title = title;
            this.author = author;
            this.imageUrl = imageUrl;
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getAmount() {
            return amount;
        }
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
