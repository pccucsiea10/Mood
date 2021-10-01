package com.ehappy.mood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class user extends AppCompatActivity{

    static public Spinner Sex,Age;
    Button Next;
    static public EditText UserName;
    String[] Sexes = new String[]{"男","女"};
    String[] Ages = new String[]{"1","2","3","4","5",
            "6","7","8","9","10",
            "11","12","13","14","15",
            "16","17","18","19","20",
            "21","22","23","24","25",
            "26","27","28","29","30",
            "31","32","33","34","35",
            "36","37","38","39","40",
            "41","42","43","44","45",
            "46","47","48","49","50",
            "51","52","53","54","55",
            "56","57","58","59","60",
            "61","62","63","64","65",
            "66","67","68","69","70",
            "71","72","73","74","75",
            "76","77","78","79","80",
            "81","82","83","84","85",
            "86","87","88","89","90",
            "91","92","93","94","95",
            "96","97","98","99","100"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        //取得此Button的實體tText
        Next = (Button)findViewById(R.id.Next1);
        UserName = (EditText)findViewById(R.id.UserName);
        Age = (Spinner)findViewById(R.id.spinner);
        Sex = (Spinner)findViewById(R.id.spinne);

        //建立ArrayAdapter
        ArrayAdapter<String> adapterAges  = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,Ages);
        //設定Spinner顯示的格式
        adapterAges.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //設定Spinner的資料來源
        Age.setAdapter(adapterAges);
        Age.setOnItemSelectedListener(spnPrefer);

        //建立ArrayAdapter
        ArrayAdapter<String> adapterSexs  = new ArrayAdapter<String>
        (this, android.R.layout.simple_spinner_item, Sexes);
        //設定Spinner顯示的格式
        adapterSexs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //設定Spinner的資料來源
        Sex.setAdapter(adapterSexs);
        Sex.setOnItemSelectedListener(spnPreferListener);

        //實做OnClickListener界面
        Next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

//                JSONObject j = new JSONObject();
//
//                try {
//                    j.put("name", UserName.getText().toString());
//                    j.put("age", Age.getSelectedItem().toString());
//                    j.put("sex",Sex.getSelectedItem().toString());
//
//                } catch (JSONException var3) {
//                }
//
//                StringEntity entity ;
//                entity = new StringEntity(j.toString(), HTTP.UTF_8);
//                ElasticRestClient.post(null,"hh/_doc", entity, "application/json",new JsonHttpResponseHandler(){
//


                //初始化Intent物件
                Intent intent = new Intent();
                //從MainActivity 到user
                intent.setClass(user.this, Status.class);
                //開啟Activity
                startActivity(intent);

            }
            });
        }
        private Spinner.OnItemSelectedListener spnPrefer =
                new Spinner.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?>parent, View view,int position, long id){
                        Age.setSelection(position);

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?>parent){
                        //TODO Auto-generated methed stub
                    }
                };
        private Spinner.OnItemSelectedListener spnPreferListener =
            new Spinner.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?>parent, View view,int position, long id){
                    Sex.setSelection(position);

                }
                @Override
                public void onNothingSelected(AdapterView<?>parent){
                    //TODO Auto-generated methed stub
                }
            };



}

