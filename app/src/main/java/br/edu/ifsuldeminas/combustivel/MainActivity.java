package br.edu.ifsuldeminas.combustivel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextEthanol;
    private TextInputEditText textInputEditTextGas;
    private Button buttonCalculate;
    private ImageView imageViewResult;
    private ImageView imageViewShare;
    private TextView textViewTip;
    private double ethanolValue;
    private double gasValue;
    private String betterOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputEditTextEthanol = findViewById(R.id.imputEditTextEthanolValueId);
        textInputEditTextGas = findViewById(R.id.imputEditTextGasValueId);
        buttonCalculate = findViewById(R.id.buttonCalculateId);
        imageViewResult = findViewById(R.id.imageViewResultId);
        imageViewShare = findViewById(R.id.imageViewShareId);
        textViewTip = findViewById(R.id.textViewTipId);
        }

    @Override
    protected void onStart(){
        super.onStart();

        imageViewResult.setVisibility(ImageView.INVISIBLE);
        imageViewShare.setVisibility(ImageView.INVISIBLE);
        textViewTip.setVisibility(TextView.INVISIBLE);
    }

    public void buttonCalculateClick(View view){
        String ethanolStringValue = textInputEditTextEthanol.getText().toString();
        String gasStringValue = textInputEditTextGas.getText().toString();

        if(ethanolStringValue.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.input_edit_text_ethanol_hint), Toast.LENGTH_SHORT).show();

            return;
        }

        if(gasStringValue.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.input_edit_text_gas_hint), Toast.LENGTH_SHORT).show();

            return;
        }

        ethanolValue = Double.parseDouble(ethanolStringValue);
        gasValue = Double.parseDouble(gasStringValue);

        if((ethanolValue / gasValue) < 0.7){
            // Ethanol é melhor!
            imageViewResult.setImageResource(R.drawable.ethanol);
            betterOption = getString(R.string.better_option_ethanol);
        } else {
            // Gasolina é melhor!
            imageViewResult.setImageResource(R.drawable.gas);
            betterOption = getString(R.string.better_option_gas);
        }
            textViewTip.setText(betterOption);
            imageViewResult.setVisibility(ImageView.VISIBLE);
            imageViewShare.setVisibility(ImageView.VISIBLE);
            textViewTip.setVisibility(TextView.VISIBLE);
    }

    public void setImageViewShareClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.alert_title);

        LayoutInflater inflater = getLayoutInflater();
        View alertDialogView = inflater.inflate(R.layout.alert_dialog_gas_station_view,null);

        builder.setView(alertDialogView);
        builder.setNegativeButton(R.string.alert_negative_button, null);
        builder.setPositiveButton(R.string.alert_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText editText = alertDialogView.findViewById(R.id.editTextAlertDialogGasStationId);
                String gasStation = editText.getText().toString();

                String message = "Preços no posto %s. Etanol %.2f - Gasolina %.2f %s, relação %.0f%s .";
                message = String.format(message, gasStation, ethanolValue, gasValue, betterOption, (ethanolValue / gasValue)*100, "%");

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                Intent shareIntent  = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
        builder.create().show();
    }
}