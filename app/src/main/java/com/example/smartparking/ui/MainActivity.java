package com.example.smartparking.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartparking.R;
import com.example.smartparking.services.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    Button button2;
    private List<ImageResponse> imageResponses =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView =findViewById(R.id.gridview);
        button2=findViewById(R.id.button2);
        getAllImages();


    }
    public  void  getAllImages(){
        Call<List<ImageResponse>>imagesResponse = ApiClient.getInterface().getAllImages();
        imagesResponse.enqueue(new Callback<List<ImageResponse>>() {
            @Override
            public void onResponse(Call<List<ImageResponse>> call, Response<List<ImageResponse>> response) {
                if(response.isSuccessful()){
                    String message ="Request successful";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                    imageResponses=response.body();
                    CustomAdapter customAdapter = new CustomAdapter(imageResponses,MainActivity.this);
                    gridView.setAdapter(customAdapter);

                }
                else{
                    String message ="an error occured try again later..";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<ImageResponse>> call, Throwable t) {
                String message =t.getLocalizedMessage();
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

            }
        });
    }
    public class CustomAdapter extends BaseAdapter{
        private List<ImageResponse>imageResponseList;
        private Context context;
        private LayoutInflater layoutInflater;


        public CustomAdapter(List<ImageResponse> imageResponseList, Context context) {
            this.imageResponseList = imageResponseList;
            this.context = context;
            this.layoutInflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imageResponseList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=layoutInflater.inflate(R.layout.row,parent,false);

                TextView block=convertView.findViewById(R.id.block);
//               TextView location= convertView.findViewById(R.id.location);
                TextView slot= convertView.findViewById(R.id.slot);
                Button button2 =convertView.findViewById(R.id.button2);
                TextView Locationname=convertView.findViewById(R.id.lname);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, ReservationActivity.class));
                    }
                });

//               location.setText( "Location"+" "+imageResponseList.get(position).getLocation());
                Locationname.setText("Location Name:"+imageResponseList.get(position).getLocation_name());
//
                slot.setText("Number of Slot:"+" "+imageResponseList.get(position).getNumber_of_slots());
                block.setText("Block Code:"+" "+imageResponseList.get(position).getBlock_code());

//                GlideApp.with(context).load(imageResponseList.get(position).getBlock_photo()).into(imageView);
            }
            return convertView;
        }
    }
}