package edu.washington.ischool.pjjj.dynasee;
import android.app.ActionBar;
import android.app.Activity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import android.app.Activity;



import java.io.IOException;


public class MainActivity extends AppCompatActivity implements OnBottomNavigationSelectedListener {

    private static final String TAG = "Dynasee MainActivity";
    private static final int CAMERA_REQUEST_CODE = 12;

    private BottomNavigation mBottomNavigation;
    private Camera mCamera;
    private CameraPreview mPreview;

    private LinearLayout filters_array;
    private int[] filtersImg;
    private LayoutInflater mInflater;
    private String[] filtersText;
    //private HorizontalScrollView filterMenu = (HorizontalScrollView) findViewById(R.id.filter_menu);

    //sound
    MediaPlayer ColorSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Make permissions requests if needed
        int camPermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (camPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        } else {
            loadCameraView();
        }

        mInflater = LayoutInflater.from(this);
        initData();
        initView();
        ColorSound = MediaPlayer.create(this, R.raw.colorblindness);
    }


    private void loadCameraView() {
        mBottomNavigation = (BottomNavigation) findViewById(R.id.kbn);
        mBottomNavigation.setBottomNavigationSelectedListener(this);
        mCamera = getCameraInstance();

        //check if device has continuous autofocus, and enable if so
        /*
        Camera.Parameters params = mCamera.getParameters();
        if (params.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCamera.setParameters(params);
        */                                      //Proved to usually be a nuisance

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    loadCameraView();
                } else {
                    Toast.makeText(this, "This app requies camera permissions",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onValueSelected(int index) {

        ToastUtil.show(this, "index = " + index);
        //if selected the filter buttoo
        /*if(index == 2) {
            filterMenu.setVisibility(filterMenu.GONE);

            mBottomNavigation.setBottomNavigationClick(index);
        }
        */
       }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /** A basic Camera preview class */
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
              //  mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    private void initData()
    {
        filtersImg = new int[] { R.drawable.a, R.drawable.b};
        filtersText = new String[] {"Colorblindness", "Cataract"};
    }


    //intialize all the filters
    private void initView()
    {
        filters_array = (LinearLayout) findViewById(R.id.id_filers_array);

        for (int i = 0; i < filtersImg.length; i++) {

            View view = mInflater.inflate(R.layout.filter_item,
                    filters_array, false);
            LinearLayout item = (LinearLayout) view.findViewById(R.id.item);
            ImageView img = (ImageView) view
                    .findViewById(R.id.id_index_filter_item_image);
            img.setImageResource(filtersImg[i]);

            TextView txt = (TextView) view
                    .findViewById(R.id.id_index_filter_item_text);
            txt.setText(filtersText[i]);

            int uniqueID = i;
            img.setId(uniqueID);
            filters_array.addView(view);
        }
    }


    //onclick
    public void horizentalOnClick(View v) {
        for(int i=0; i<filtersImg.length; i++) {
            if(v.getId() == i) {
                Toast.makeText(this, "Index "+(i), Toast.LENGTH_SHORT).show();
                if(i == 0){
                    ColorSound.start();
                }else{
                    ColorSound.stop();
                }
            }
        }
    }
}
