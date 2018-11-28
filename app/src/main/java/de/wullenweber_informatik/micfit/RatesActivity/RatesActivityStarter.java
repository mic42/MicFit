package de.wullenweber_informatik.micfit.RatesActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.wullenweber_informatik.micfit.MicFitMainActivity;

/**
 * Created by wullmicz on 08.11.2018.
 */

public class RatesActivityStarter {

    AppCompatActivity _activity;

    public RatesActivityStarter(AppCompatActivity activity)
    {
        _activity = activity;
    }


    boolean _bRatesReady;
    public static ArrayList<String> _theRates = new ArrayList<>();
    public static boolean _bInfoTextReady;
    public static String _sInfoText = "";

    class MyTimerTask extends TimerTask
    {

        @Override
        public void run()
        {
            _activity.runOnUiThread(
                    new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            if (_bRatesReady) {
                                _bRatesReady = false;
                                Intent intent = new Intent(_activity, ShowRatesActivity.class);

                                _activity.startActivity(intent);

                            }

                            if (_bInfoTextReady) {
                                MicFitMainActivity._textInfo.setText(_sInfoText);
                                _bInfoTextReady = false;
                            }

                        }}
            );
        }

    }

    Timer timer;
    MyTimerTask myTimerTask;

    public void startRatesActivity()
    {
        _bRatesReady = false;
        _theRates.clear();
        _bInfoTextReady = false;

        Log.i("MicFit", "Starting rates activity");

        if (checkInternetConenction()) {
            downloadRates();

            timer = new Timer();
            myTimerTask = new MyTimerTask();

            timer.schedule(myTimerTask, 0, 1000);
        }
    }

    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)_activity.getSystemService(_activity.getBaseContext().CONNECTIVITY_SERVICE);

        Toast.makeText(_activity, " Connecting ... ", Toast.LENGTH_LONG).show();

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            Toast.makeText(_activity, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(_activity, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    private void downloadRates() {
        //progressDialog = ProgressDialog.show(this, "", "Downloading Image from " + urlStr);
        //final String url = urlStr;

        new Thread() {
            public void run() {
                try {
                    String xmlFilePath = MicFitMainActivity. _sMicFitDir + "/Rates.xml";

                    RateCollector rc = new RateCollector();

                    RateInfo ri = RateInfo.Parse(xmlFilePath);

                    for (RateInfo.RateTrend rt : ri._RateList)
                    {
                        if (rt._KK > 0.0 && rt._VK <= 0.0)
                        {
                            Log.i("MicFit", "Getting rate for: " + rt._sName);
                            Log.i("MicFit", "Link: " + rt._sLinkDiBa);
                            while (_bInfoTextReady)
                            {
                                // Wait until false
                            }
                            _sInfoText = "Getting rate for: " + rt._sName;
                            _bInfoTextReady = true;


                            String sContent = rc.getWebPage(rt._sLinkDiBa);
                            Log.i("MicFit", "Successfully got page");
                            rt._AK = rc.getCurrentRate(sContent);
                            Log.i("MicFit", "Rate for " + rt._sName + ": " + rt._AK);

                            _theRates.add(String.format("%1$s: KK: %2$.1f RK: %3$.1f AK: %4$.1f",
                                    rt._sName,
                                    rt._KK,
                                    rt._RealTimeK,
                                    rt._AK
                            ));

                        }
                    }
                    _bRatesReady = true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.i("MicFit", "Exception: " + e.getMessage());
                    _sInfoText = "Exception: " + e.getMessage();
                    _bInfoTextReady = true;
                }
            }
        }.start();
    }

}
