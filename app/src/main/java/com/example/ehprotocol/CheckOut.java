package com.example.ehprotocol;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class CheckOut extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "FrontendDebug";

    private TextView code1, code2, code3, code4, code5, code6, code7, code8;
    private TextView dateTextView;
    private CheckBox positive, caution;
    private Button chooseDate, finalize;

    private ImageButton backbuttonCO;

    private String fullCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        code7 = findViewById(R.id.code7);
        code8 = findViewById(R.id.code8);

        code1.requestFocus();

        dateTextView = findViewById(R.id.dateTextView);

        positive = findViewById(R.id.positivityCB);
        caution = findViewById(R.id.cautionsCB);

        chooseDate = findViewById(R.id.chooseDate);
        finalize = findViewById(R.id.finalize);

        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code1.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode != event.KEYCODE_DEL && keyCode != event.KEYCODE_ENTER) {
                            code2.requestFocus();
                        }

                        return true;
                    }
                });
            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code2.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_DEL && code2.getText().toString().isEmpty()) {
                            code1.requestFocus();
                        } else if (keyCode != event.KEYCODE_DEL && !code2.getText().toString().isEmpty())
                            code3.requestFocus();
                        return true;
                    }
                });
            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code3.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_DEL && code3.getText().toString().isEmpty()) {
                            Log.d(TAG, "Here3");
                            code2.requestFocus();
                        } else if (keyCode != event.KEYCODE_DEL && !code3.getText().toString().isEmpty())
                            code4.requestFocus();
                        return true;
                    }
                });
            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code4.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_DEL && code4.getText().toString().isEmpty()) {
                            code3.requestFocus();
                        } else if (keyCode != event.KEYCODE_DEL && !code4.getText().toString().isEmpty())
                            code5.requestFocus();
                        return true;
                    }
                });
            }
        });

        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code5.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_DEL && code5.getText().toString().isEmpty()) {
                            code4.requestFocus();
                        } else if (keyCode != event.KEYCODE_DEL && !code5.getText().toString().isEmpty())
                            code6.requestFocus();
                        return true;
                    }
                });
            }
        });

        code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code6.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_DEL && code6.getText().toString().isEmpty()) {
                            code5.requestFocus();
                        } else if (keyCode != event.KEYCODE_DEL && !code6.getText().toString().isEmpty())
                            code7.requestFocus();
                        return true;
                    }
                });
            }
        });

        code7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code7.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_DEL && code7.getText().toString().isEmpty()) {
                            code6.requestFocus();
                        } else if (keyCode != event.KEYCODE_DEL && !code7.getText().toString().isEmpty())
                            code8.requestFocus();
                        return true;
                    }
                });
            }
        });

        code8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code8.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_DEL && code8.getText().toString().isEmpty()) {
                            code7.requestFocus();
                        }
                        return true;
                    }
                });
            }
        });

        Calendar c = Calendar.getInstance();
        dateTextView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));

        finalize.setOnClickListener(e->{
            if(validateCode(fullCode) && positive.isChecked() && caution.isChecked()){
                Toast.makeText(getApplicationContext(), "ok bro", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "You have missing information or the code is invalid", Toast.LENGTH_SHORT).show();
            }
        });

        chooseDate.setOnClickListener(e->{
            DialogFragment datePicker = new DatePickerFragment(true, true);
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        backbuttonCO = findViewById(R.id.backbutton);

        backbuttonCO.setOnClickListener(e->{
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
        });

    }

    private boolean validateCode(String fullCode) {
        getCode();
        if(fullCode.length() != 9)
            return false;
        return true;
    }

    private void clearAllFields() {
        code1.setText(null);
        code2.setText(null);
        code3.setText(null);
        code4.setText(null);
        code5.setText(null);
        code6.setText(null);
        code7.setText(null);
        code8.setText(null);
    }

    private void getCode() {
        fullCode = code1.getText().toString()
                + code2.getText().toString()
                + code3.getText().toString()
                + code4.getText().toString()
                + "-"
                + code5.getText().toString()
                + code6.getText().toString()
                + code7.getText().toString()
                + code8.getText().toString();
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        dateTextView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));
    }
}
