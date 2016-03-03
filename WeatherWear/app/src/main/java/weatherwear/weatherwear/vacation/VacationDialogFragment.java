package weatherwear.weatherwear.vacation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import weatherwear.weatherwear.R;

/**
 * Created by emilylin27 on 3/2/16.
 * Opens up dialogs for start and end dates when creating vacation
 */
public class VacationDialogFragment extends DialogFragment {
    public static final int START_DATE_KEY = 0;
    public static final int END_DATE_KEY = 1;

    private int mId, mYear, mMonth, mDay;
    public static final String ID_KEY = "idkey";
    private boolean mCancelClicked;

    // Sets what dialog to bring up
    public void setDialogId(int dialogId) {
        mId = dialogId;
    }

    // From 1970 epoch time in seconds to something like "10/24/2012"
    public static String parseDate(long msDate) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(msDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.d.yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Activity parent = getActivity();

        final Calendar cal = Calendar.getInstance();

        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        switch (mId) {
            case START_DATE_KEY:
                final DatePickerDialog.OnDateSetListener startListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int yearPicked, int monthPicked, int dayPicked) {
                        if(mCancelClicked) {
                            mCancelClicked = false;

                        } else{
                            ((VacationCreatorActivity) parent).getVacation()
                                    .setStartDate(monthPicked, dayPicked, yearPicked);
                            VacationCreatorActivity.setStartButtonText(((VacationCreatorActivity) parent)
                                    .getVacation().getStartInMillis());
                            mYear = yearPicked;
                            mMonth = monthPicked;
                            mDay = dayPicked;
                        }
                    }
                };
                final DatePickerDialog startDialog = new DatePickerDialog(
                        parent, R.style.DialogTheme, startListener,
                        mYear, mMonth, mDay);

                startDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                        getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    mCancelClicked = true;
                                    startDialog.dismiss();
                                }
                            }
                        });
                startDialog.setCancelable(false);
                return startDialog;
            case END_DATE_KEY:
                final DatePickerDialog.OnDateSetListener endListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int yearPicked, int monthPicked, int dayPicked) {
                        if(mCancelClicked) {
                            mCancelClicked = false;

                        } else{
                            ((VacationCreatorActivity) parent).getVacation()
                                    .setEndDate(monthPicked, dayPicked, yearPicked);
                            VacationCreatorActivity.setEndButtonText(((VacationCreatorActivity) parent)
                                    .getVacation().getEndInMillis(), parent);
                            mYear = yearPicked;
                            mMonth = monthPicked;
                            mDay = dayPicked;
                        }
                    }
                };
                final DatePickerDialog endDialog = new DatePickerDialog(
                        parent, R.style.DialogTheme, endListener,
                        mYear, mMonth, mDay);

                endDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                        getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    mCancelClicked = true;
                                    endDialog.dismiss();
                                }
                            }
                        });
                endDialog.setCancelable(false);
                return endDialog;
            default:
                return null;
        }
    }
}