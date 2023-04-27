package com.riftech.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String h_unit,w_unit,gender;
    double height,weight,bmr,age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button=(Button)findViewById(R.id.button);
        Spinner dropdown = findViewById(R.id.spinner9);
        Spinner dropdown1 = findViewById(R.id.spinner8);
        Spinner dropdown2 = findViewById(R.id.spinner11);
        TextView txt1=(TextView) findViewById(R.id.textView22);

        EditText editText1 = (EditText)findViewById(R.id.editTextNumberDecimal7);
        EditText editText2 = (EditText)findViewById(R.id.editTextNumberDecimal6);
        EditText editText3 = (EditText)findViewById(R.id.editTextNumberDecimal8);
        ProgressBar pgbar =(ProgressBar) findViewById(R.id.progressBar);


        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setVisibility(View.INVISIBLE);
                pgbar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {



                        pgbar.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                        startActivity(intent);

                        //main.setVisibility(View.VISIBLE);
                    }
                }, 6000);

            }
        });


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // click handling code
                h_unit = dropdown.getSelectedItem().toString();
                w_unit = dropdown1.getSelectedItem().toString();

                gender = dropdown2.getSelectedItem().toString();
                if(editText1.getText().toString().equals("") || editText2.getText().toString().equals("")|| editText3.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter your height, weight and age.", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    button.setVisibility(View.INVISIBLE);
                    pgbar.setVisibility(View.VISIBLE);
                    weight = Float.parseFloat(editText2.getText().toString());
                    height = Float.parseFloat(editText1.getText().toString());
                    age = Float.parseFloat(editText3.getText().toString());

                    if (Objects.equals(h_unit, "ft")) {
                        height = height * 30.48;
                    }

                    if (Objects.equals(w_unit, "lbs")) {
                        weight = weight * 0.45359237;
                    }

                    if(Objects.equals(gender, "Male")){
                       bmr=(10*weight)+(6.25*height)-(5*age)+5;
                    }else{
                        bmr=(10*weight)+(6.25*height)-(5*age)-161;
                    }

                    bmr = Math.round(bmr * 10.0) / 10.0;

                    /*Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(bfp), Toast.LENGTH_SHORT);
                    toast.show();*/
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("bmr", bmr);
                    intent.putExtra("gender", gender);


                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {



                            pgbar.setVisibility(View.GONE);
                            button.setVisibility(View.VISIBLE);
                            startActivity(intent);

                            //main.setVisibility(View.VISIBLE);
                        }
                    }, 7000);

                }
            }
        });

        //get the spinner from the xml.

//create a list of items for the spinner.
        String[] items = new String[]{"ft", "cm"};
        String[] items1 = new String[]{"kg", "lbs"};
        String[] items2 = new String[]{"Female", "Male"};


//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown1.setAdapter(adapter1);
        dropdown2.setAdapter(adapter2);

    }
}