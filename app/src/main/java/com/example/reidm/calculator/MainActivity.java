/* CSCI 4176: Assignment 2

Matthew Reid
B00728822
Matthew.Reid@dal.ca
 */
package com.example.reidm.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView resulttxt;
    private String operation, memOperation, first, second, lastOperationUsed, tempResult;
    private boolean resetValue, operatorUsed, firstValue, secondValue, equalsUsed, decimalUsed, negativeNum;
    private double result, memoryValue;
    private int digitCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resulttxt = findViewById(R.id.Result);
        resulttxt.setEnabled(false);

        //Initially assign a default value of zero for the result, which is displayed in the text field
        result = 0;
        resulttxt.setText(String.valueOf((int) result));

        //Set the first numerical value, second numerical value, current operation, and the last operation to nothing
        first = "";
        second = "";
        operation = "";
        lastOperationUsed = "";

        //digitCount tracks the number of digits entered (max 10), initialized to 0
        digitCount = 0;
        //Initial value stored in memory is 0
        memoryValue = 0;

        //resetValue tracks whether the displayed value should be reset to 0
        resetValue = true;
        //operatorUsed tracks whether an operation (+, -, etc.) was the last button pressed
        operatorUsed = false;
        //firstValue and secondValue track whether the first or second value for a calculation has been entered, respectively
        firstValue = false;
        secondValue = false;
        //equalsUsed tracks whether Equals was the last button pressed
        equalsUsed = false;
        //decimalUsed tracks whether a decimal has been entered into the value, this is to prevent incorrect input (ex: 5...3..2.1.231)
        decimalUsed = false;
        //negativeNum tracks whether the value is positive or negative, using the +/- button
        negativeNum = false;
    }

    //Method to determine which numerical button is pressed
    public void numClick(View v)
    {
        Button btn = findViewById(v.getId());
        String text = btn.getText().toString();

        //Clear the display, so that numbers do not get appended to the default value
        if (resetValue == true)
        {
            resulttxt.setText("");
            resetValue = false;
        }

        //If 10 digits have been entered or a decimal has been used, don't do anything. This essentially disables the respective buttons
        if ((decimalUsed == true && text.equals(".")) || digitCount == 10)
        {
            //Do nothing
        }

        //Add a decimal and set the flag to true
        else if (text.equals("."))
        {
            decimalUsed = true;
            resulttxt.setText(resulttxt.getText() + text);
        }

        //Otherwise add the number to the shown value
        else {
            negativeNum = false;
            resulttxt.setText(resulttxt.getText() + text);
            digitCount++;
        }

        operatorUsed = false;
    }

    //Method to determine which operation was clicked
    public void operationClick(View v)
    {
        if (operatorUsed == true)
        {
            //Do nothing, button disabled. Prevents incorrect input (ex: 5+++++++-3)
        }
        else {
            Button btn = findViewById(v.getId());
            operation = btn.getText().toString();

            //Set the first value if a first value is not present
            if (firstValue == false) {
                first = "" + resulttxt.getText();
                firstValue = true;
                secondValue = false;
                equalsUsed = false;
                operatorUsed = true;
            }
            else {
                //If there is a first value present and equals was not used, then assign a second value
                if (equalsUsed == false) {
                    second = "" + resulttxt.getText();
                    secondValue = true;
                }

                //Based on the last operation entered, do a calculation
                if (lastOperationUsed.equals("+")) {
                    result = Double.parseDouble(first) + Double.parseDouble(second);
                } else if (lastOperationUsed.equals("-")) {
                    result = Double.parseDouble(first) - Double.parseDouble(second);
                } else if (lastOperationUsed.equals("*")) {
                    result = Double.parseDouble(first) * Double.parseDouble(second);
                } else if (lastOperationUsed.equals("/")) {
                    result = Double.parseDouble(first) / Double.parseDouble(second);
                }

                //Use modulus to determine if the value is an integer or a double.
                if (result % 1 == 0) {
                    resulttxt.setText(String.valueOf((int) result));
                }
                else
                {
                    String formatResult = Double.toString(result);
                    if (formatResult.length() > 10 )
                    {
                        int count = 0;
                        for (int i = 0; i < formatResult.length(); i++)
                        {
                            if (formatResult.charAt(i) == '-' || formatResult.charAt(i) == '.')
                            {
                                //Do nothing
                            }
                            else
                            {
                                count++;
                            }
                            if (count >= 10)
                            {
                                formatResult = formatResult.substring(0, i);
                            }
                        }
                    }

                    resulttxt.setText(String.valueOf(formatResult));
                }

                //Set the first value to be the result of the above computation
                first = "" + resulttxt.getText();
                secondValue = false;
                operatorUsed = true;
            }
            //Now, the "last" operation was the one that was just pressed
            lastOperationUsed = operation;
        }

        resetValue = true;
        operatorUsed = true;
        decimalUsed = false;
        digitCount = 0;
    }

    //Method to do calculations when Equals is clicked
    public void equalsClick(View v)
    {
        //Try to perform a calculation
        try
        {
            //Store a second value if one is not present
            if (secondValue == false)
            {
                second = ""+ resulttxt.getText();
                secondValue = true;
            }

            //Do calculations based on the operation used
            if (operation.equals("+"))
                result = Double.parseDouble(first) + Double.parseDouble(second);
            else if (operation.equals("-"))
                result = Double.parseDouble(first) - Double.parseDouble(second);
            else if (operation.equals("*"))
                result = Double.parseDouble(first) * Double.parseDouble(second);
            else if (operation.equals("/"))
                result = Double.parseDouble(first) / Double.parseDouble(second);
            //If no operator was used, don't perform an operation
            else if (operatorUsed == false)
            {
                tempResult = "" + resulttxt.getText();
                result = Double.parseDouble(tempResult);
            }

            //Cast to an int if the result is an integer, otherwise format the output
            if (result % 1 == 0)
            {
                resulttxt.setText(String.valueOf((int) result));
            }
            else
            {
                String formatResult = Double.toString(result);
                if (formatResult.length() > 10 )
                {
                    int count = 0;
                    for (int i = 0; i < formatResult.length(); i++)
                    {
                        if (formatResult.charAt(i) == '-' || formatResult.charAt(i) == '.')
                        {
                            //Do nothing
                        }
                        else
                        {
                            count++;
                        }
                        if (count >= 10)
                        {
                            formatResult = formatResult.substring(0, i);
                        }
                    }
                }

                resulttxt.setText(String.valueOf(formatResult));
            }

            first = "" + resulttxt.getText();

            //If the result (which is now stored in first) is a negative number, activate the flag
            if (first.contains("-"))
            {
                negativeNum = true;
            }
            else
            {
                negativeNum = false;
            }

            //Reset values as necessary
            operation = "";
            equalsUsed = true;
            firstValue = false;
            operatorUsed = false;
            decimalUsed = false;
            digitCount = 0;
            resetValue = true;
        }

        //Catch any incorrect input. Note: this should never happen based on my previous checks, but this is in place as a
        catch (NumberFormatException e)
        {
            Toast.makeText(this, "SYNTAX ERROR", Toast.LENGTH_LONG).show();
            result = 0;
            resulttxt.setText(String.valueOf((int) result));
            first = "";
            second = "";
            operation = "";
            resetValue = true;
            firstValue = false;
            secondValue = false;
        }
    }

    //Method for clearing the calculator. Values are all reset.
    public void clearClick(View v)
    {
        result = 0;
        resulttxt.setText(String.valueOf((int) result));

        first = "";
        second = "";
        operation = "";
        lastOperationUsed = "";
        digitCount = 0;
        memoryValue = 0;
        resetValue = true;
        operatorUsed = false;
        negativeNum = false;
        decimalUsed = false;
    }

    //Method to toggle whether the number is positive or negative
    public void signClick(View v)
    {
        if (resulttxt.getText().equals("0") || operatorUsed == true)
        {
            //Do nothing, can't negate a zero or immediately after an operator was selected
        }

        else if (negativeNum == false)
        {
            resulttxt.setText("-" + resulttxt.getText());
            negativeNum = true;
        }

        else
        {
            tempResult = "" + resulttxt.getText();
            tempResult = tempResult.replace("-", "");
            resulttxt.setText(tempResult);
            negativeNum = false;
        }
    }

    //Method for the memory functions
    public void memoryClick(View v)
    {
        Button btn = findViewById(v.getId());
        memOperation = btn.getText().toString();

        if (memOperation.equals("MC"))
        {
            memoryValue = 0;
        }

        else if (memOperation.equals("M+"))
        {
            tempResult = "" + resulttxt.getText();
            memoryValue = memoryValue + Double.parseDouble(tempResult);
        }

        else if (memOperation.equals("M-"))
        {
            tempResult = "" + resulttxt.getText();
            memoryValue = memoryValue - Double.parseDouble(tempResult);
        }

        //Display memory value if MR is used
        else
        {
            if (memoryValue % 1 == 0)
            {
                resulttxt.setText(String.valueOf((int) memoryValue));
            }
            else
            {
                resulttxt.setText(String.valueOf(memoryValue));
            }
        }
    }
}