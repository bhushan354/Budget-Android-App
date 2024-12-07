package com.example.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textTotalBalance;
    private List<Transaction> transactionList = new ArrayList<>();
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTotalBalance = findViewById(R.id.textTotalBalance);
        Button buttonAddIncome = findViewById(R.id.buttonAddIncome);
        Button buttonAddExpense = findViewById(R.id.buttonAddExpense);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        transactionAdapter = new TransactionAdapter(transactionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(transactionAdapter);

        buttonAddIncome.setOnClickListener(v -> showAddTransactionDialog(true));
        buttonAddExpense.setOnClickListener(v -> showAddTransactionDialog(false));
    }

    private void showAddTransactionDialog(boolean isIncome) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isIncome ? "Add Income" : "Add Expense");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null);
        EditText editDescription = view.findViewById(R.id.editDescription);
        EditText editAmount = view.findViewById(R.id.editAmount);

        builder.setView(view);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String description = editDescription.getText().toString();
            double amount = Double.parseDouble(editAmount.getText().toString());
            Transaction transaction = new Transaction(description, amount, isIncome);
            transactionList.add(transaction);
            transactionAdapter.notifyDataSetChanged();
            updateBalance();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateBalance() {
        double total = 0;
        for (Transaction transaction : transactionList) {
            total += transaction.isIncome() ? transaction.getAmount() : -transaction.getAmount();
        }
        textTotalBalance.setText(String.format("Total Balance: $%.2f", total));
    }
}
