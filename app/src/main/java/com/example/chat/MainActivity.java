package com.example.chat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final int MAX_LENGH = 100;
    ArrayList<String> list = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText editTextMessages;
    Button sendButton;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("messages");
        editTextMessages = findViewById(R.id.messageIn);
        sendButton = findViewById(R.id.send_message);
        rv = findViewById(R.id.messages_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DataAdapter dataAdapter = new DataAdapter(this, list);
        rv.setAdapter(dataAdapter);
        sendButton.setOnClickListener(v -> {
            String ms = editTextMessages.getText().toString().toString();
            if (ms.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter the message!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ms.length() > MAX_LENGH) {
                Toast.makeText(getApplicationContext(), "Message is too long!", Toast.LENGTH_SHORT).show();
                return;
            }
            myRef.push().setValue(ms);
            editTextMessages.setText("");
        });
        myRef.addChildEventListener(new ChildEventListener() {


            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msg = snapshot.getValue(String.class);
                list.add(msg);
                dataAdapter.notifyDataSetChanged();
                rv.smoothScrollToPosition(list.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}