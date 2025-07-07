package com.example.blfatsamples;

import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {
    private String Display = "0";
    private double Number = 0;
    private boolean IsPreviousAnOperator = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.calculator_activity);

        Button[] buttonsNumbers = new Button[]{
                findViewById(R.id.btn0), findViewById(R.id.btn1), findViewById(R.id.btn2),
                findViewById(R.id.btn3),findViewById(R.id.btn4),findViewById(R.id.btn5),
                findViewById(R.id.btn6),findViewById(R.id.btn7),findViewById(R.id.btn8),
                findViewById(R.id.btn9),findViewById(R.id.btn0),
        };

        for (Button buttonNumber : buttonsNumbers) {
            buttonNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToDisplayAndShow(buttonNumber.getText().toString());
                    IsPreviousAnOperator = false;
                }
            });
        }

        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAndView();
                IsPreviousAnOperator = true;
            }
        });

        Button[] operatorsButtons = new Button[] {
                findViewById(R.id.btnAdd),findViewById(R.id.btnDivide),
                findViewById(R.id.btnMultiply),findViewById(R.id.btnSubtract),
        };

        for (Button operatorButton : operatorsButtons) {
            operatorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (IsPreviousAnOperator) return;

                    addToDisplayAndShow(" " + operatorButton.getText() + " ");

                    IsPreviousAnOperator = true;
                }
            });
        }

        findViewById(R.id.btnEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCalculationAndShow();

                IsPreviousAnOperator = false;
            }
        });
    }

    private void clearAndView(){
        Display = "";
        addToDisplayAndShow("0");
        Number = 0;
    }

    private void addToDisplayAndShow(String other){
        if (Display.equals("0") || Display.equals("0.00")) {
            Display = other;
        }
        else {
            Display = Display + other;
        }

        TextView txtView = findViewById(R.id.display);
        txtView.setText(Display);
    }

    private void makeCalculationAndShow(){
        try {
            Double result = getCalculationByText(Display);
            Display = ((Integer)result.intValue()).toString();
            TextView txtView = findViewById(R.id.display);
            txtView.setText(Display);
        }
        catch (Exception e){
            Display = "Error";
            TextView txtView = findViewById(R.id.display);
            txtView.setText(Display);
            Number = 0;
        }

    }

    private double getCalculationByText(String text){
        String[] operations = text.split( " ");
        double builder = 0;
        String previousOperator = "";

        for (String operationOrNumber : operations) {
            // TODO: change this by a list of +,-,/ and *, then check if is in the list
            switch (operationOrNumber){
                case "+":
                    previousOperator = operationOrNumber;
                    continue;
                case "-":
                    previousOperator = operationOrNumber;
                    continue;
                case "/":
                    previousOperator = operationOrNumber;
                    continue;
                case "*":
                    previousOperator = operationOrNumber;
                    continue;
                default:
                    break;
            }

            double parsedNumber = Double.parseDouble(operationOrNumber);
            switch (previousOperator){
                case "+":
                    builder += parsedNumber;
                    continue;
                case "-":
                    builder -= parsedNumber;
                    continue;
                case "/":
                    if (parsedNumber == 0) continue; // TODO: Add error message
                    builder = builder / parsedNumber;
                    continue;
                case "*":
                    builder *= parsedNumber;
                    continue;
                default:
                    break;
            }
            previousOperator = "";
            builder = parsedNumber;
        }

        return builder;
    }
}