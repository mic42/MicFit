package de.wullenweber_informatik.micfit;

/**
 * Created by wullmicz on 06.11.2018.
 */

public class TrPlanDisplayEngine {
    Trainingsplan _TP;
    long _StartTime_ms,_StartTimeLesson_ms;

    int _iCurrentLessonIdx;
    boolean _bStartLesson;
    boolean _bChangeSide;
    boolean _bStartet;

    Trainingsplan.TrainingsEinheit _CurrentLesson;

    int _iChangeEverySec;

    public static class DisplayUpdate {
        public String _sTimeRemainingLesson;
        public boolean _bTimeRemainingLessonChange;
        public String _sTimeRemainingChangeSide;
        public boolean _bTimeRemainingChangeSideChange;
        public String _sLessonText;
        public boolean _bLessonTextChange;
        public boolean _bSignalChangeSide;
        public boolean _bSignalNextLesson;
        public boolean _bSignalLessonDone;
        public boolean _bTrainingFinished;
    }

    public TrPlanDisplayEngine(Trainingsplan TP) {
        super();
        _TP = TP;
        _bStartet = false;
    }

    void Start(DisplayUpdate upd)
    {
        _StartTime_ms = System.currentTimeMillis();
        _StartTimeLesson_ms = _StartTime_ms;
        _iCurrentLessonIdx = -1;
        _CurrentLesson = null;
        _bStartLesson = false;
        _bChangeSide = false;

        upd._sTimeRemainingLesson = "-" + _TP._iWarteZeitAnfang_sec;
        upd._bTimeRemainingLessonChange = true;
        upd._sTimeRemainingChangeSide = "";
        upd._bTimeRemainingChangeSideChange = true;
        upd._sLessonText = "Gleich geht's los!";
        upd._bLessonTextChange = true;
    }

    public void GetDisplay(DisplayUpdate upd)
    {
        if (! _bStartet)
        {
            Start(upd);
            _bStartet = true;
            return;
        }

        int iTimeExpired = (int)((System.currentTimeMillis() - _StartTime_ms) / 1000);
        int iTimeExpLesson = (int)((System.currentTimeMillis() - _StartTimeLesson_ms) / 1000);

        if (iTimeExpired < _TP._iWarteZeitAnfang_sec)
        {
            upd._sTimeRemainingLesson = "-" + (_TP._iWarteZeitAnfang_sec - iTimeExpired);
            upd._bTimeRemainingLessonChange = true;
            return;
        }
        else
        {
            if ((_iCurrentLessonIdx == -1) || (iTimeExpLesson > (_CurrentLesson._iDauer_sec + _TP._iPause_sec)))
            {
                _iCurrentLessonIdx ++;
                if (_iCurrentLessonIdx >= _TP._Einheiten.size())
                {
                    // Done
                    upd._sTimeRemainingLesson = "";
                    upd._bTimeRemainingLessonChange = true;
                    upd._sTimeRemainingChangeSide = "";
                    upd._bTimeRemainingChangeSideChange = true;
                    upd._sLessonText = "Geschafft!";
                    upd._bLessonTextChange = true;
                    upd._bTrainingFinished = true;

                    return;
                }

                _CurrentLesson = _TP._Einheiten.get(_iCurrentLessonIdx);
                _StartTimeLesson_ms = System.currentTimeMillis();
                _bStartLesson = true;

                if (_CurrentLesson._iAbschnitte > 0)
                    _iChangeEverySec = _CurrentLesson._iDauer_sec / _CurrentLesson._iAbschnitte;
                else
                    _iChangeEverySec = _CurrentLesson._iDauer_sec + 1;

                upd._sTimeRemainingLesson = "w";
                upd._bTimeRemainingLessonChange = true;
                upd._sTimeRemainingChangeSide =  "-" + _TP._iPause_sec;
                upd._bTimeRemainingChangeSideChange = true;
                upd._sLessonText = _CurrentLesson._sName;
                upd._bLessonTextChange = true;
                upd._bSignalLessonDone = true;
            }
            else
            {
                // Lesson
                if (iTimeExpLesson < _TP._iPause_sec)
                {
                    upd._sTimeRemainingLesson = "w";
                    upd._bTimeRemainingLessonChange = true;
                    upd._sTimeRemainingChangeSide =  "-" + (_TP._iPause_sec - iTimeExpLesson);
                    upd._bTimeRemainingChangeSideChange = true;
                }
                else
                {
                    int iTimeExpAfterPause = iTimeExpLesson - _TP._iPause_sec;
                    int iTimeSinceSideChange = iTimeExpAfterPause % _iChangeEverySec;
                    upd._sTimeRemainingLesson =  "-" + (_CurrentLesson._iDauer_sec - iTimeExpAfterPause);
                    upd._bTimeRemainingLessonChange = true;
                    if (_CurrentLesson._iAbschnitte > 0)
                    {
                        upd._sTimeRemainingChangeSide =  "-" + (_iChangeEverySec - iTimeSinceSideChange);
                    }
                    else
                    {
                        upd._sTimeRemainingChangeSide =  "";
                    }
                    upd._bTimeRemainingChangeSideChange = true;
                    if (_bStartLesson)
                    {
                        upd._bSignalNextLesson = true;
                    }
                    if ((iTimeSinceSideChange == 0) && (iTimeExpAfterPause != 0))
                    {
                        upd._bSignalChangeSide = true;
                    }

                    _bStartLesson = false;
                }
            }
        }
    }
}