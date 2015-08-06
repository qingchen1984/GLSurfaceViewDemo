package cn.edu.bjtu.andrew.glsurfaceviewdemo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by AndrewZJ on 2015/7/24.
 */
public class GLSurfaceViewVideo extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private Context context;
    private MediaPlayer mediaPlayer;
    private SurfaceTexture surfaceTexture;
    private NormalVideo normalVideo;
    private String videopath;

    public GLSurfaceViewVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(videopath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        normalVideo = NormalVideo.getInstance(context);
        surfaceTexture = new SurfaceTexture(normalVideo.getTextureID());
        surfaceTexture.setOnFrameAvailableListener(this);

        Surface surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);
        surface.release();

        try {
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this){
            surfaceTexture.updateTexImage();
        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glViewport(0, 0, getWidth(), getHeight());
        normalVideo.DrawSelf();
    }

    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.requestRender();
    }

    public void setConfig(Context context, String video_url) {
        this.context = context;
        videopath = video_url;
        this.setEGLContextClientVersion(2);
        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
