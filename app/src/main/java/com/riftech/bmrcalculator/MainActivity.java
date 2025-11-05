package com.riftech.bmrcalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int UPDATE_REQUEST_CODE = 123;
    private AppUpdateManager appUpdateManager;

    String h_unit,w_unit,gender;
    double height,weight,bmr,age;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
//                button.setVisibility(View.INVISIBLE);
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
                }, 2000);

            }
        });


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // Hide keyboard when user taps calculate
                hideKeyboard(view);

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
        String[] items2 = new String[]{"Male", "Female"};


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

    @Override
    protected void onResume() {
        super.onResume();
        // Always check for updates when user returns to the app
        checkForUpdate();
    }

    private void checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            this,
                            UPDATE_REQUEST_CODE
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // Update is downloaded but not installed
                showCompleteUpdateSnackbar();
            }
        });
    }

    private void showCompleteUpdateSnackbar() {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Update ready! Restart to apply.",
                Snackbar.LENGTH_INDEFINITE
        );
        snackbar.setAction("Restart", view -> appUpdateManager.completeUpdate());
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Update canceled. You will be reminded again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.change, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_share) {
            // do something here
            // single item array instance to store which element is selected by user initially
            // it should be set to zero meaning none of the element is selected by default
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BMR Calculator");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}