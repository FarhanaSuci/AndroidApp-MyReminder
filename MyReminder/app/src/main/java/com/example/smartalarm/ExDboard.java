package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;

import com.example.smartalarm.databinding.ActivityExDboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExDboard extends AppCompatActivity {
    ActivityExDboardBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    int sumExpense=0;
    int sumIncome=0;
    ArrayList<TransactionModel>transactionModelArrayList;
    TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExDboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        transactionModelArrayList = new ArrayList<>();
        //transactionAdapter = new TransactionAdapter(this,transactionModelArrayList);
        //binding.historyRecyclerView.setAdapter(transactionAdapter);
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setHasFixedSize(true);

        /*firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(ExDboard.this, action_bar.class));
                    finish();
                }
            }
        });*/



        binding.addFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(ExDboard.this,AddTransactionActivity.class));
                }
                catch (Exception e){


                }
            }
        });
        loadData();
        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(ExDboard.this,ExDboard.class));
                    finish();
                }
                catch (Exception e){

                }
            }
        });

    }



    private void loadData(){
        firebaseFirestore.collection("MyExpense").document(firebaseAuth.getUid())
                .collection("Notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot ds : task.getResult()) {
                                TransactionModel model = new TransactionModel(ds.getString("id"),
                                        ds.getString("note"),
                                        ds.getString("amount"),
                                        ds.getString("type"),
                                        ds.getString("date")
                                );
                                int amount =Integer.parseInt(ds.getString("amount"));
                                if (ds.getString("type").equals("Expense")){
                                    sumExpense = sumExpense+amount;

                                }
                                else{
                                    sumIncome = sumIncome+amount;
                                }
                                transactionModelArrayList.add(model);
                            }
                            binding.totalIncome.setText(String.valueOf(sumIncome));
                            binding.totalExpense.setText(String.valueOf(sumExpense));
                            binding.totalBalance.setText(String.valueOf(sumIncome-sumExpense));
                            transactionAdapter = new TransactionAdapter(ExDboard.this,transactionModelArrayList);
                            binding.historyRecyclerView.setAdapter(transactionAdapter);
                        }
                    }
                });
    }

   /* private void loadData(){
        firebaseFirestore.collection("transation").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                for (DocumentSnapshot ds :task.getResult()){
                    TransactionModel model = new TransactionModel(ds.getString("id"),
                            ds.getString("note"),
                            ds.getString("amount"),
                            ds.getString("type"),
                            ds.getString("date")
                            );
                    transactionModelArrayList.add(model);
                }
                transactionAdapter = new TransactionAdapter(ExDboard.this,transactionModelArrayList);
                binding.historyRecyclerView.setAdapter(transactionAdapter);
            }
        });

    }
    /*private void loadData(){
        firebaseFirestore.collection("transation").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d:list){
                            TransactionModel model = new TransactionModel(d.getString("id"),
                                    d.getString("note"),
                                    d.getString("amount"),
                                    d.getString("type"),
                                    d.getString("date")
                            );
                            transactionModelArrayList.add(model);

                        }
                    }
                });

    }*/

}