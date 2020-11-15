package com.example.contactlessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CustomerQRcodeActivity extends AppCompatActivity {

    ImageView userQrCode;

    private String getUsername;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userRef;
    private String QR_text_ID;

    private Button generatepdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_q_rcode);
        firebaseAuth = FirebaseAuth.getInstance();

        userQrCode = findViewById(R.id.userQrCode);
        generatepdf = findViewById(R.id.btngeneratepdf);

        Intent intent = getIntent();
        getUsername = intent.getStringExtra("getUsername");

        UserGenerateQR_code();

        genpdf();
    }

    private void genpdf() {
        generatepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
            }
        });
    }


    public void UserGenerateQR_code(){
        userRef = firebaseDatabase.getReference("Registered_Users/" + getUsername);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    QR_text_ID = snapshot.child("QR_Text_ID").getValue().toString().trim();
//                    Toast.makeText(CustomerQRcodeActivity.this," " + QR_text_ID, Toast.LENGTH_SHORT).show();
                    String data = QR_text_ID;
                    QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT,250);
                    //Get QR code as bitmap
                    Bitmap bitmap = qrgEncoder.getBitmap();
                    //set bitmap as image
                    userQrCode.setImageBitmap(bitmap);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}