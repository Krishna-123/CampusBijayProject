package com.techfiesta.aot.campusplacement;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView quesBlock, ansBlock;
    RadioButton option1, option2, option3, option4;
    RadioGroup radioGroup;
    String url1 = "http://192.168.43.239:3001/getReady.json";
    String url2 = "http://192.168.43.239:3001/getquestion.json";
    int id = 1;
    JSONObject jsonObject;
    JSONObject jsonResp;
    JSONObject options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quesBlock = findViewById(R.id.quesBlock);
        radioGroup = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        ansBlock = findViewById(R.id.ansBlock);
        quesBlock.setMovementMethod(new ScrollingMovementMethod());
    }

    public void nextQuestion(View view) {

        new myAsyncTask("GET").execute(url2, "" + id);
        id++;
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
        radioGroup.clearCheck();
        ansBlock.setText("");
    }

    public void startTest(View view) {
        new myAsyncTask("GET").execute(url1, "123");

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        String correct_ans = "Wrong!!\n";
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.option1:
                if (checked) {
                    option2.setEnabled(false);
                    option3.setEnabled(false);
                    option4.setEnabled(false);

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (Objects.equals(option1.getText().toString(), options.getString(jsonResp.getString("answer")))) {
                                correct_ans = "Correct@!!\n";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.option2:
                if (checked) {
                    option1.setEnabled(false);
                    option3.setEnabled(false);
                    option4.setEnabled(false);
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (Objects.equals(option2.getText().toString(), options.getString(jsonResp.getString("answer")))) {
                                correct_ans = "Correct@!!\n";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.option3:
                if (checked) {
                    option2.setEnabled(false);
                    option1.setEnabled(false);
                    option4.setEnabled(false);
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (Objects.equals(option3.getText().toString(), options.getString(jsonResp.getString("answer")))) {
                                correct_ans = "Correct@!!\n";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.option4:
                if (checked) {
                    option2.setEnabled(false);
                    option3.setEnabled(false);
                    option1.setEnabled(false);
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (Objects.equals(option4.getText().toString(), options.getString(jsonResp.getString("answer")))) {
                                correct_ans = "Correct@!!\n";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        try {
            correct_ans = correct_ans + options.getString(jsonResp.getString("answer"));
            ansBlock.setText(correct_ans);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class myAsyncTask extends AsyncTask<String, Integer, String> {

        ProgressDialog myDialog = new ProgressDialog(MainActivity.this);

        String ReqTYPE = "GET";

        public myAsyncTask(String TYPE) {
            ReqTYPE = TYPE;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            myDialog.setMessage("Waiting for response !");
//            myDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser myRquest = new JSONParser();

            JSONObject myObj = new JSONObject();
            HashMap myParam = new HashMap();
            myParam.put("quesno", params[1]);
            String response = "";

            if (ReqTYPE.equals("GET")) {
                myObj = myRquest.makeHttpRequest(params[0], "GET", myParam);
            }

//            publishProgress(10);

            return myObj.toString();
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            //myDialog.cancel();

            Log.d("campusquestions", "Response" + resp);
            try {

                jsonObject = new JSONObject(resp);
                jsonResp = new JSONObject(jsonObject.getString("result"));
                options = new JSONObject(jsonResp.getString("option"));

                quesBlock.setText(jsonResp.getString("question"));
                option1.setText(options.getString("a"));
                option2.setText(options.getString("b"));
                option3.setText(options.getString("c"));
                option4.setText(options.getString("d"));


                Log.d("JSONQuestion", jsonResp.getString("answer"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            RespText.setText(resp);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
