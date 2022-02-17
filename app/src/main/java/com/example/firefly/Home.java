package com.example.firefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class   Home extends AppCompatActivity implements View.OnClickListener {

    private CardView callFireService;
    private CardView goMedicalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        callFireService=findViewById(R.id.callFireService);
        callFireService.setOnClickListener(this);
        goMedicalService=findViewById(R.id.goMedicalService);
        goMedicalService.setOnClickListener(this);

    }
    //dramabaaaz er dol amake just notun code de!!!
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.callFireService:
                Intent intent=new Intent(getApplicationContext(),MapView.class);
                startActivity(intent);
                break;
            case R.id.goMedicalService:
                Toast.makeText(getApplicationContext(), "Medical Services Alerted", Toast.LENGTH_SHORT).show();
                intent=new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
