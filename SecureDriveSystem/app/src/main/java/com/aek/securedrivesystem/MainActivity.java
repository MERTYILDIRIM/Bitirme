package com.aek.securedrivesystem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    DatabaseReference buzzerReference;
    DatabaseReference distanceWarningReference;
    DatabaseReference distanceReference;
    DatabaseReference detectReference;
    DatabaseReference relayReference;
    private Button buzzerBtn,kaydetBtn,relayBtn;
    private EditText distanceEditText;
    private TextView distanceText,detectText;
    int allDataHere=0;
    private void declareWidgets()
    {
        buzzerBtn = (Button)findViewById(R.id.buzzerBtn);
        kaydetBtn = (Button)findViewById(R.id.kaydetBtn);
        relayBtn = (Button)findViewById(R.id.relayBtn);
        distanceEditText = (EditText)findViewById(R.id.distanceEditText);
        distanceText = (TextView)findViewById(R.id.distanceText);
        detectText = (TextView)findViewById(R.id.detectText);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(),"Firebase Bağlantısı Kurulup Bilgiler Güncellenirken Lütfen Bekleyiniz...",Toast.LENGTH_LONG).show();
        declareWidgets();
        buzzerReference = FirebaseDatabase.getInstance().getReference("buzzer");
        distanceWarningReference = FirebaseDatabase.getInstance().getReference("distanceWarning");
        distanceReference = FirebaseDatabase.getInstance().getReference("distance");
        detectReference = FirebaseDatabase.getInstance().getReference("detect");
        relayReference = FirebaseDatabase.getInstance().getReference("relay");
        detectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(false).toString();
                if(value.charAt(0)=='1')
                    detectText.setText("Araç Algılandı");
                else if(value.charAt(0)=='2')
                    detectText.setText("İnsan Algılandı");
                else
                    detectText.setText("Algılama Yok");
                if(allDataHere<4)
                {
                    allDataHere++;
                    if(allDataHere==4)
                        Toast.makeText(getApplicationContext(),"Tüm Bilgiler Güncellendi",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buzzerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(false).toString();
                if(value.charAt(0)=='1')
                    buzzerBtn.setText("Buzzer Kapat");
                else
                    buzzerBtn.setText("Buzzer Aç");
                if(allDataHere<4)
                {
                    allDataHere++;
                    if(allDataHere==4)
                        Toast.makeText(getApplicationContext(),"Tüm Bilgiler Güncellendi",Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(),key+":"+value,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        distanceWarningReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(false).toString();
                distanceEditText.setText(value);
                if(allDataHere<4)
                {
                    allDataHere++;
                    if(allDataHere==4)
                        Toast.makeText(getApplicationContext(),"Tüm Bilgiler Güncellendi",Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(),key+":"+value,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        distanceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(false).toString();
                if(detectText.getText()=="Araç Algılandı")
                    distanceText.setText("Mesafe: "+value);
                else
                    distanceText.setText("");
                if(allDataHere<4)
                {
                    allDataHere++;
                    if(allDataHere==4)
                        Toast.makeText(getApplicationContext(),"Tüm Bilgiler Güncellendi",Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(),key+":"+value,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value1
            }
        });
        buzzerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buzzerBtn.getText()=="Buzzer Kapat")
                    buzzerReference.setValue("0");
                else
                    buzzerReference.setValue("1");
            }
        });
        relayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relayReference.setValue("0");
            }
        });
        kaydetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distance = distanceEditText.getText().toString();
                if(Integer.parseInt(distance)<100 || Integer.parseInt(distance)>300)
                    Toast.makeText(getApplicationContext(),"İlk Uyarı Mesafesi 100'den Küçük Veya 300'den Büyük Olamaz", Toast.LENGTH_LONG).show();
                else {
                    distanceWarningReference.setValue(distance);
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
