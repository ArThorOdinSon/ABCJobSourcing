package com.example.abcjobsourcing;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextDependents, editTextHoursWorked;
    private Spinner spinnerRole;
    private TextView textViewRole, textViewGross, textViewNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextDependents = findViewById(R.id.editTextDependents);
        editTextHoursWorked = findViewById(R.id.editTextHoursWorked);
        spinnerRole = findViewById(R.id.spinnerRole);
        textViewRole = findViewById(R.id.textViewOutputRole);  // Corrected ID
        textViewGross = findViewById(R.id.textViewOutputGross);
        textViewNet = findViewById(R.id.textViewOutputNet);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // Optional: Set a default selection for the spinner
        spinnerRole.setSelection(0);
    }

    public void calculatePay(View view) {
        try {
            // Validate input
            if (validateInput()) {
                // Get input values
                String name = editTextName.getText().toString();
                String role = spinnerRole.getSelectedItem().toString();
                int dependents = Integer.parseInt(editTextDependents.getText().toString());
                int hoursWorked = Integer.parseInt(editTextHoursWorked.getText().toString());

                // Perform calculations
                double hourlyRate = 1000.00;
                double grossPay = hoursWorked * hourlyRate;
                double socialSecurityDeduction = 0.0785 * grossPay;
                double incomeTaxDeduction = (grossPay - (grossPay * 0.05 * dependents)) * 0.15;
                double membershipFee = 0.15 * grossPay;
                double netPay = grossPay - socialSecurityDeduction - incomeTaxDeduction - membershipFee;

                // Display results
                textViewRole.setText("Role: " + role);
                textViewGross.setText("Gross = Php " + String.format("%.2f", grossPay));
                textViewNet.setText("Net = Php " + String.format("%.2f", netPay));
            }
        } catch (NumberFormatException e) {
            // Handle the case where parsing of integers fails
            e.printStackTrace();
            Toast.makeText(this, "Invalid input. Please enter valid numbers.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {
        // Check if the EditText fields are not empty
        if (editTextName.getText().toString().isEmpty() ||
                editTextDependents.getText().toString().isEmpty() ||
                editTextHoursWorked.getText().toString().isEmpty()) {
            // Display an error message or toast
            Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if dependents and hours worked are positive
        int dependents = Integer.parseInt(editTextDependents.getText().toString());
        int hoursWorked = Integer.parseInt(editTextHoursWorked.getText().toString());

        if (dependents < 0 || hoursWorked < 0) {
            // Display an error message or toast
            Toast.makeText(this, "Please enter positive values for dependents and hours worked.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
