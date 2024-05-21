package com.prakhar.notes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText et_name, et_email, et_notes;
    TextView tv_name;
    Button btn_save, btn_show;
    SQLiteDatabase sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btn_save = findViewById(R.id.btn_save);
        btn_show = findViewById(R.id.btn_show);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_notes = findViewById(R.id.et_notes);
        tv_name = findViewById(R.id.tv_name);

        //to create or open the database
        sd = openOrCreateDatabase("mynotesdb", 0, null);
        String createTable = "CREATE TABLE IF NOT EXISTS savedinfo (name VARCHAR(150), email VARCHAR(150), notes VARCHAR(150))";
        sd.execSQL(createTable);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String email = et_email.getText().toString();
                String notes = et_notes.getText().toString();
                String insertData = "INSERT INTO savedinfo (name, email, notes) VALUES ('" + name + "', '" + email + "', '" + notes + "')";
                sd.execSQL(insertData);
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectData = "SELECT * FROM savedinfo";
                Cursor cursor = sd.rawQuery(selectData, null);
                StringBuilder data = new StringBuilder();

                int nameIndex = cursor.getColumnIndex("name");
                int emailIndex = cursor.getColumnIndex("email");
                int notesIndex = cursor.getColumnIndex("notes");

                while (cursor.moveToNext()) {
                    String name = cursor.getString(nameIndex);
                    String email = cursor.getString(emailIndex);
                    String notes = cursor.getString(notesIndex);
                    data.append("Name: ").append(name).append("\n").append("Email: ").append(email).append("\n").append("Notes: ").append(notes).append("\n\n");
                }
                cursor.close();
                tv_name.setText(data.toString());
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
