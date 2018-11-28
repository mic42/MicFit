package de.wullenweber_informatik.micfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TrainingActivity extends AppCompatActivity {
    TextView textTimer1;
    TextView textTimer2;
    TextView textLesson;
    Timer timer;
    MyTimerTask myTimerTask;

    TrPlanDisplayEngine tpde = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Get the Intent that started this activity and extract the string
        //  Intent intent = getIntent();
        //  String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Trainingsplan tp = MicFitMainActivity._tp;

        // Capture the layout's TextView and set the string as its text
        textTimer1 = findViewById(R.id.text_timer_1);
        textTimer2 = findViewById(R.id.text_timer_2);
        textLesson = findViewById(R.id.text_lesson);

        timer = new Timer();
        myTimerTask = new MyTimerTask();

        tpde = new TrPlanDisplayEngine(tp);

        timer.schedule(myTimerTask, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }


    class MyTimerTask extends TimerTask
    {

        @Override
        public void run()
        {
            runOnUiThread(
                    new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            TrPlanDisplayEngine.DisplayUpdate upd = new TrPlanDisplayEngine.DisplayUpdate();
                            tpde.GetDisplay(upd);
                            if (upd._bTimeRemainingLessonChange)
                            {
                                textTimer1.setText(upd._sTimeRemainingLesson);
                            }
                            if (upd._bTimeRemainingChangeSideChange)
                            {
                                textTimer2.setText(upd._sTimeRemainingChangeSide);
                            }
                            if (upd._bLessonTextChange)
                            {
                                textLesson.setText(upd._sLessonText);
                            }
                        }}
            );
        }

    }
}
