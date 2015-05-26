package com.pp.a10dance.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.pp.a10dance.helper.ApiClientAsyncTask;
import com.pp.a10dance.helper.LogUtils;

/**
 * Created by saketagarwal on 4/19/15.
 */
public class DriveStudentImportActivity extends
        BaseGoogleDriveIntegrationActivity {

    private static final int REQUEST_CODE_OPENER = 1;
    private static final String TAG = LogUtils
            .getTag(DriveStudentImportActivity.class);

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(
                        new String[] { "application/vnd.google-apps.spreadsheet" })
                .build(getGoogleApiClient());
        try {
            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null,
                    0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.w(TAG, "Unable to send intent", e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CODE_OPENER:
            if (resultCode == RESULT_OK) {
                DriveId driveId = (DriveId) data
                        .getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                Toast.makeText(this, "Selected file's ID: " + driveId,
                        Toast.LENGTH_SHORT).show();
                new RetrieveDriveFileContentsAsyncTask(this).execute(driveId);
            }
            finish();
            break;
        default:
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    final private class RetrieveDriveFileContentsAsyncTask extends
            ApiClientAsyncTask<DriveId, Boolean, String> {

        public RetrieveDriveFileContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params) {
            String contents = null;
            DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(),
                    params[0]);
            DriveApi.DriveContentsResult driveContentsResult = file.open(
                    getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
                    .await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }
            DriveContents driveContents = driveContentsResult
                    .getDriveContents();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    driveContents.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                contents = builder.toString();
            } catch (IOException e) {
                Log.e(TAG, "IOException while reading from the stream", e);
            }

            driveContents.discard(getGoogleApiClient());
            return contents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(DriveStudentImportActivity.this,
                        "Error while reading from the file", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            Toast.makeText(DriveStudentImportActivity.this,
                    "File contents: " + result, Toast.LENGTH_SHORT).show();
        }
    }
}
