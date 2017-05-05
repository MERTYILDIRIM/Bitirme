package com.aek.firstfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private static final String database_url = "https://firstfirebase-b74f0.firebaseio.com/";
    DatabaseReference buzzerReference;
    DatabaseReference distanceWarningReference;
    DatabaseReference picSaveReference;
    DatabaseReference distanceReference;
    DatabaseReference detectReference;
    DatabaseReference relayReference;
    //private FirebaseAuth mFirebaseAuth;
    //private FirebaseUser mFirebaseUser;
    //private FirebaseAnalytics mFirebaseAnalytics;
    //private FirebaseRemoteConfig mFirebaseRemoteConfig;
    //private GoogleApiClient mGoogleApiClient;
    private Button buzzerBtn,kaydetBtn,relayBtn;
    private EditText distanceEditText;
    private TextView distanceText,detectText;
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
        declareWidgets();
        buzzerReference = FirebaseDatabase.getInstance().getReference("buzzer");
        distanceWarningReference = FirebaseDatabase.getInstance().getReference("distanceWarning");
        distanceReference = FirebaseDatabase.getInstance().getReference("distance");
        detectReference = FirebaseDatabase.getInstance().getReference("detect");
        relayReference = FirebaseDatabase.getInstance().getReference("relay");

        // Initialize Firebase Measurement.
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Initialize Firebase Remote Config.
        //mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // Apply config settings and default values.
        detectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(false).toString();
                if(value.charAt(0)=='1')
                    detectText.setText("Araç Algılandı");
                else
                    detectText.setText("Algılama Yok");
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
                Toast.makeText(getApplicationContext(),key+":"+value,Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),key+":"+value,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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
