package weatherwear.weatherwear.vacation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import weatherwear.weatherwear.R;

/**
 * Created by emilylin27 on 3/2/16.
 * Opens up dialogs for start and end dates when creating vacation
 */
public class VacationDialogFragment extends DialogFragment {
    public static final int END_DATE_KEY = 1;
    public static final String ID_KEY = "idkey";

    private int mId, mYear, mMonth, mDay;

    private boolean mCancelClicked;

    // Sets what dialog to bring up
    public void setDialogId(int dialogId) {
        mId = dialogId;
    }

    // Creates a dialog to pick end date
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity parent = getActivity();
        final Calendar cal = Calendar.getInstance();
        // Creates year, month, and day variables
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        switch (mId) {
            case END_DATE_KEY:
                // handles all picking of dates, and updates the information
                final DatePickerDialog.OnDateSetListener endListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int yearPicked, int monthPicked, int dayPicked) {
                        if (mCancelClicked) {
                            mCancelClicked = false;
                        } else {
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.MONTH, monthPicked);
                            cal.set(Calendar.DAY_OF_MONTH, dayPicked);
                            cal.set(Calendar.YEAR, yearPicked);
                            VacationCreatorActivity.setEndButtonText(cal.getTimeInMillis(), parent);
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
                endDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                endDialog.getDatePicker().setMaxDate(cal.getTimeInMillis() + 5 * 24 * 60 * 60 * 1000);
                endDialog.setCancelable(false);
                return endDialog;
            default:
                return null;
        }
    }
}