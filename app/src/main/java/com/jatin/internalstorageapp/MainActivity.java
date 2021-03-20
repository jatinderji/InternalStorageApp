package com.jatin.internalstorageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Base64;

/*
DATA PERSISTENCE:
    - SHARED PREFERENCES
    - INTERNAL STORAGE:
        - Storage are of device's internal memory.
        - Keep your files
        - These files/data is only accessible to your app not others.
        - Once your app is removed you data stored in internal storage will be removed.
        - It is more secure than SharedPreferences as your data is stored in the form of bytes(i.e. 01010)
        - It is your responsibility to convert your data into byte before saving into internal storage and
          convert your data back to specific type after reading from internal storage.
        - To work with Internal Storage you need to use FileInputStream and FileOutputStream.
        - use MODE_PRIVATE for store data into internal storage.
 */

public class MainActivity extends AppCompatActivity {


    private EditText etData;
    private Button btnSave, btnRead;
    private static final String fileName = "myFile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etData = findViewById(R.id.etData);
        btnSave = findViewById(R.id.btnSave);
        btnRead = findViewById(R.id.btnRead);
        btnSave.setOnClickListener(view -> {

            String data = etData.getText().toString() + "\n";
            FileOutputStream fos = null;

            try {
                fos = openFileOutput(fileName, Context.MODE_APPEND);
                String enData = AESCrypt.encrypt("jatin",data);
                fos.write(enData.getBytes());// Exception
                Toast.makeText(this, "Data Saved..", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                        etData.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnRead.setOnClickListener(view -> {

            FileInputStream fin = null;

            try {
                fin = openFileInput(fileName);
                InputStreamReader inr = new InputStreamReader(fin);
                BufferedReader br = new BufferedReader(inr);

                StringBuilder sb = new StringBuilder();
                String data = "";
                while ((data = br.readLine()) != null) {
                    sb.append(data + "\n");
                }
                if (sb.length() > 0) {
                    String plainData = AESCrypt.decrypt("jatin",sb.toString());
                    etData.setText(plainData);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

          /*
          // To read Small amount of data //
            FileInputStream fin = null;
            try {
                fin = openFileInput(fileName);
                StringBuilder sb = new StringBuilder();
                int c;
                while ( (c=fin.read())!=-1){
                    sb.append((char)c);
                }
                if(sb.length()>0){
                    etData.setText(sb.toString());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/


        });

    }
}