package de.wullenweber_informatik.micfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.wullenweber_informatik.micfit.RatesActivity.RateCollector;
import de.wullenweber_informatik.micfit.RatesActivity.RateInfo;
import de.wullenweber_informatik.micfit.RatesActivity.RatesActivityStarter;

public class MicFitMainActivity extends AppCompatActivity {
    public static Trainingsplan _tp;
    public static String _sMicFitDir;
    public static TextView _textInfo;

    RatesActivityStarter _showRatesActivityStarter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic_fit_main);
        _textInfo = findViewById(R.id.txtInfo);
        findWorkingDir();

        _showRatesActivityStarter = new RatesActivityStarter(this);
    }

    private void findWorkingDir()
    {
        String sWorkDir1 =  "/storage/1259-1E02/MicFit"; // Device
        String sWorkDir2 =  "/storage/18F1-251F/MicFit"; // Emulator

        _sMicFitDir = null;
        File dir1 = new File(sWorkDir1);
        if (dir1.exists() && dir1.isDirectory())
        {
            _sMicFitDir = sWorkDir1;
        }
        else {
            File dir2 = new File(sWorkDir2);
            if (dir2.exists() && dir2.isDirectory()) {
                _sMicFitDir = sWorkDir2;
            } else {
                _sMicFitDir = "";
                Toast.makeText(getApplicationContext(), "MicFit directory not valid!", Toast.LENGTH_LONG).show();
            }
        }

        _textInfo.setText("Dir: " + _sMicFitDir);
    }

    public void startTrainingActivity(View view) {
        Intent intent = new Intent(this, TrainingActivity.class);

        if (_tp == null)
        {
            String xmlFilePath =  _sMicFitDir + "/TrPlan.xml";

            Toast.makeText(getApplicationContext(), "Parsing: " + xmlFilePath, Toast.LENGTH_LONG).show();

            _tp = Trainingsplan.Parse(xmlFilePath);
            System.out.println("TP: " + _tp);
        }

        if (_tp == null)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Could not read");
            alertDialog.setMessage("File");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        startActivity(intent);
    }

    public void startRatesActivity(View view) {
        _showRatesActivityStarter.startRatesActivity();
    }
}
