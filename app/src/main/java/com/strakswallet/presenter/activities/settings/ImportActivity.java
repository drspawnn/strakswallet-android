package com.strakswallet.presenter.activities.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.strakswallet.BreadApp;
import com.strakswallet.R;
import com.strakswallet.presenter.activities.camera.ScanQRActivity;
import com.strakswallet.presenter.activities.util.BRActivity;
import com.strakswallet.presenter.customviews.BRDialogView;
import com.strakswallet.presenter.customviews.BRToast;
import com.strakswallet.tools.animation.BRAnimator;
import com.strakswallet.tools.animation.BRDialog;
import com.strakswallet.tools.util.BRConstants;


public class ImportActivity extends BRActivity {
    private Button scan;
    private static final String TAG = ImportActivity.class.getName();
    public static boolean appVisible = false;
    private static ImportActivity app;
    private ImageButton close;

    public static ImportActivity getApp() {
        return app;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        scan = findViewById(R.id.scan_button);
        close = findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageButton faq = findViewById(R.id.faq_button);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BRAnimator.isClickAllowed()) return;
                BRAnimator.showSupportFragment(app, BRConstants.importWallet);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BRAnimator.isClickAllowed()) return;
                BRAnimator.openScanner(ImportActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appVisible = true;
        app = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        appVisible = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    // Results from APPLICATION_DETAILS_SETTINGS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:
                // try open scanner again
                BRAnimator.openScanner(this);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case BRConstants.CAMERA_REQUEST_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, open camera
                    Intent intent = new Intent(app, ScanQRActivity.class);
                    app.startActivityForResult(intent, BRConstants.SCANNER_REQUEST);
                    app.overridePendingTransition(R.anim.fade_up, R.anim.fade_down);

                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(app, Manifest.permission.CAMERA))
                    BRDialog.showCustomDialog(app, getString(R.string.Send_cameraUnavailabeTitle_android), getString(R.string.Send_cameraUnavailabeMessage_android),
                            getString(R.string.Button_settings), getString(R.string.Button_cancel), new BRDialogView.BROnClickListener() {
                                @Override
                                public void onClick(BRDialogView brDialogView) {
                                    //Start security settings for user
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, 1);
                                    brDialogView.dismiss();
                                }
                            }, new BRDialogView.BROnClickListener() {
                                @Override
                                public void onClick(BRDialogView brDialogView) {
                                    brDialogView.dismissWithAnimation();
                                }
                            }, null, 0, true);
                else
                    BRToast.showCustomToast(app, "Camera permission DENIED",
                            BreadApp.DISPLAY_HEIGHT_PX - 200, Toast.LENGTH_SHORT, R.drawable.toast_layout_red, false);
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
