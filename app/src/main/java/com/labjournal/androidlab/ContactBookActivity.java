package com.labjournal.androidlab;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ContactBookActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etName, etPhone, etEmail;
    private Button btnAddContact;
    private LinearLayout layoutContactsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_book);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        etName = findViewById(R.id.et_contact_name);
        etPhone = findViewById(R.id.et_contact_phone);
        etEmail = findViewById(R.id.et_contact_email);
        btnAddContact = findViewById(R.id.btn_add_contact);
        layoutContactsContainer = findViewById(R.id.layout_contacts_container);

        btnAddContact.setOnClickListener(v -> addContact());

        loadContacts();
    }

    private void addContact() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Name and Phone are required", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.addContact(name, phone, email);
        if (id != -1) {
            etName.setText("");
            etPhone.setText("");
            etEmail.setText("");
            Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();
            loadContacts();
        } else {
            Toast.makeText(this, "Error saving contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadContacts() {
        layoutContactsContainer.removeAllViews();
        Cursor cursor = dbHelper.getAllContacts();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                TextView tv = new TextView(this);
                tv.setText(name + "\n📞 " + phone + (TextUtils.isEmpty(email) ? "" : "\n✉️ " + email));
                tv.setTextSize(16f);
                tv.setPadding(0, 16, 0, 24);
                
                tv.setOnLongClickListener(v -> {
                    dbHelper.deleteContact(id);
                    Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show();
                    loadContacts();
                    return true;
                });

                layoutContactsContainer.addView(tv);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
