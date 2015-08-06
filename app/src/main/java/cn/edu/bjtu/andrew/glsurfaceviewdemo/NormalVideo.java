package cn.edu.bjtu.andrew.glsurfaceviewdemo;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by AnrewZJ on 2015/7/24.
 */
public class NormalVideo {

    private float[] mVertexData = {
        // X,    Y,     Z,
        -1.0f,  -1.0f,  0,
         1.0f,  -1.0f,  0,
        -1.0f,	 1.0f,  0,
         1.0f,   1.0f,  0,
    };

    private float[] mTexCoorData = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexCoorBuffer;
    private int mProgram;
    private int maPositionHandle;
    private int maTexCoorHandle;
    private int muMVPMatrixHandle;
    private float[] mMVPMatrix = new float[16];
    private int mTextureID;

    private static NormalVideo sNormalVideo;

    public static NormalVideo getInstance(Context context){
        if (sNormalVideo == null){
            synchronized (NormalVideo.class){
                if (sNormalVideo == null){
                    sNormalVideo = new NormalVideo(context);
                }
            }
        }
        return sNormalVideo;
    }

    public NormalVideo(Context context){
        initVertexData();
        initShader(context);
    }

    private void initVertexData() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(mVertexData.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为int型缓冲
        mVertexBuffer.put(mVertexData);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置

        ByteBuffer llbb = ByteBuffer.allocateDirect(mTexCoorData.length * 4);
        llbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer=llbb.asFloatBuffer();
        mTexCoorBuffer.put(mTexCoorData);
        mTexCoorBuffer.position(0);
    }

    private void initShader(Context context) {
        String mVertexShader = ShaderUtil.loadFromAssetsFile("vertexShader.sh", context);
        String mFragmentShader = ShaderUtil.loadFromAssetsFile("fragShader.sh", context);
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        if (mProgram == 0) {
            return;
        }
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maTexCoorHandle = GLES20.glGetAttribLocation(mProgram,"aTexCoor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
    }

    public void DrawSelf(){
        GLES20.glUseProgram(mProgram);

        //绑定到纹理0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);

        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,false, 3*4,mVertexBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);

        GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,false, 2*4,mTexCoorBuffer);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);

        Matrix.setIdentityM(mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix,0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
    
    public int getTextureID() {
        return mTextureID;
    }
}
