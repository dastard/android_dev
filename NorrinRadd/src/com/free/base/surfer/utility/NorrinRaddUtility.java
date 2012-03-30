package com.free.base.surfer.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class NorrinRaddUtility {
	public static final String OK = "OK";

	public static Runnable manageAlertDialog(final Activity fourerrActivity, final String dialogTitle, final String dialogMessage) {

		return new Runnable() {

			public void run() {
				final AlertDialog alertDialog = buildAlertDialog(dialogTitle, dialogMessage);
				alertDialog.show();
			}

			private AlertDialog buildAlertDialog(final String dialogTitle, final String dialogMessage) {

				final AlertDialog alertDialog = new AlertDialog.Builder(fourerrActivity).create();
				alertDialog.setTitle(dialogTitle);
				alertDialog.setMessage(dialogMessage);
				alertDialog.setButton(OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						fourerrActivity.setResult(Activity.RESULT_OK);
						fourerrActivity.finish();
						return;
					}
				});

				return alertDialog;

			}
		};
	}

}
