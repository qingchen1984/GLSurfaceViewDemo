package cn.edu.bjtu.andrew.glsurfaceviewdemo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends Activity {
    private GLSurfaceViewVideo myGLSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        myGLSurfaceView = (GLSurfaceViewVideo) findViewById(R.id.my_glsurfaceview);

        String video_url = Environment.getExternalStorageDirectory() + "/video/t4.mp4";
        myGLSurfaceView.setConfig(this, video_url);

    }

    @Override
    protected void onPause() {
        super.onPause();
        myGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGLSurfaceView.onResume();
    }
}
