package com.example.smartparking.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartparking.R;
import com.example.smartparking.models.ReservationRequest;
import com.example.smartparking.models.ReservationResponse;
import com.example.smartparking.services.ApiClient;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;

import java.sql.Time;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {

    @BindView(R.id.editTextEntryDate)
    EditText entryDate;

    @BindView(R.id.editTextExitTime)
    EditText exitTime;
    @BindView(R.id.editTextEntryTime)
    EditText entryTime;
    @BindView(R.id.reservationButton)
    Button reservationButton;
    private int mHour, mMinute,mSecond,mYear, mMonth, mDay;

    Time diff;
    String format;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        ButterKnife.bind(this);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        entryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReservationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String sDate=year+ "-"+(monthOfYear + 1)+"-"+dayOfMonth;
                                entryDate.setText(sDate);
                                //entryDate.setText(year+ "-"+(monthOfYear + 1)+"-"+dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        entryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                mSecond=c.get(Calendar.SECOND);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReservationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }
                                    entryTime.setText(hourOfDay + ":" + minute + ":" + mSecond);

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        exitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                mSecond=c.get(Calendar.SECOND);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReservationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }

                                exitTime.setText(hourOfDay + ":" + minute + ":" + mSecond);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReservation(addReservation());
            }
        });


    }

    public ReservationRequest addReservation(){
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setUser_id(1);
        reservationRequest.setParking_slot_id(1);
        reservationRequest.setBooking_date(entryDate.getText().toString());
        reservationRequest.setEntry_time(entryTime.getText().toString());
        reservationRequest.setExit_time(exitTime.getText().toString());
        reservationRequest.setPlate_No(7);
        reservationRequest.setLocation(1);
        return reservationRequest;
    }
    public void saveReservation(ReservationRequest reservationRequest){
        Call<ReservationResponse> reservationResponseCall = ApiClient.getReservationService().saveReservation(reservationRequest);
        reservationResponseCall.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ReservationActivity.this, "Booking successful", Toast.LENGTH_LONG).show();
                    new RaveUiManager(ReservationActivity.this).setAmount(100)
                            .setCurrency("RWF")
                            .setEmail("ubelyse1@gmail.com")
                            .setfName("Belyse")
                            .setlName("Uwambayinema")
                            .setNarration("narration")
                            .setPublicKey("FLWPUBK-153c54964b77d3382386e19706048494-X")
                            .setEncryptionKey("1a59522dfb665a5bd9e0c0e2")
                            .setTxRef("txRef")
                            .setPhoneNumber("+250787905576", true)
                            .acceptAccountPayments(false)
                            .acceptCardPayments(true)
                            .acceptMpesaPayments(false)
                            .acceptAchPayments(false)
                            .acceptGHMobileMoneyPayments(false)
                            .acceptUgMobileMoneyPayments(false)
                            .acceptZmMobileMoneyPayments(false)
                            .acceptRwfMobileMoneyPayments(true)
                            .acceptSaBankPayments(false)
                            .acceptUkPayments(false)
                            .acceptBankTransferPayments(false)
                            .acceptUssdPayments(false)
                            .acceptBarterPayments(false)
                            .acceptFrancMobileMoneyPayments(false)
                            .allowSaveCardFeature(false)
                            .onStagingEnv(false)
                            .withTheme(R.style.MyCustomTheme)
                            .initialize();
                }else{
                    Toast.makeText(ReservationActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {
                Toast.makeText(ReservationActivity.this, "Booking unsuccessful"+ t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ReservationActivity.this,ContactUsActivity.class);
                startActivity(intent);
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}