package hz.screenmedia;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018-01-18.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private SurfaceTexture mSurfaceTexture;
    private OnSurfaceCreateListener l;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new MyGLRenderer());
    }

    public void setOnSurfaceCreateListener(OnSurfaceCreateListener l) {
        this.l = l;
    }

    private class MyGLRenderer implements GLSurfaceView.Renderer {

        private String vertex = "attribute vec4 a_Position;\n" +
                "attribute vec2 a_Coordinate;\n" +
                "varying   vec2 v_Coordinate;\n\n" +
                "void main() {\n" +
                "   gl_Position = a_Position;\n" +
                "   v_Coordinate = a_Coordinate;\n" +
                "}";

        private String fragment = "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +
                "uniform samplerExternalOES a_Texture;\n" +
                "varying vec2 v_Coordinate;\n\n" +
                "void main() {\n" +
                "   gl_FragColor = texture2D(a_Texture, v_Coordinate);\n" +
                "}";

        private int mProgram;

        private float[] mVertexCoordinate = { -1, -1,-1, 1, 1, -1, 1, 1};
        private float[] mTextureCoordinate = { 0, 1,0, 0, 1, 1, 1, 0};

        private FloatBuffer mVertexBuffer;
        private FloatBuffer mTextureBuffer;

        private int mTextureId;

        private ShaderHandles mShaderHandles = new ShaderHandles();

        public MyGLRenderer() {
            mVertexBuffer = convertToFloatBuffer(mVertexCoordinate);
            mTextureBuffer = convertToFloatBuffer(mTextureCoordinate );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
            mProgram = ShaderUtils.createProgram(vertex, fragment);
            GLES20.glUseProgram(mProgram);

            mShaderHandles.aPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
            mShaderHandles.uColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Coordinate");

            mTextureId = createTexture();
            if (l != null)
                l.onSurfaceCreate();

            GLES20.glVertexAttribPointer(mShaderHandles.aPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
            GLES20.glVertexAttribPointer(mShaderHandles.uColorHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer);

            GLES20.glEnableVertexAttribArray(mShaderHandles.uColorHandle);
            GLES20.glEnableVertexAttribArray(mShaderHandles.aPositionHandle);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mSurfaceTexture.updateTexImage();
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCoordinate.length / 2);
        }

        public int createTexture() {
            int[] texture = new int[1];
            GLES20.glGenTextures(1, texture, 0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

            mSurfaceTexture = new SurfaceTexture(texture[0]);
            mSurfaceTexture.setDefaultBufferSize(500, 500);//必须设置 原因不明
            mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    Log.d("zhx", "onFrameAvailable: ");
                }
            });

            return texture[0];
        }

        private FloatBuffer convertToFloatBuffer(float[] buffer) {
            FloatBuffer fb = ByteBuffer.allocateDirect(buffer.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            fb.put(buffer);
            fb.position(0);
            return fb;
        }

    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    public Surface getSurface() {
        return new Surface(mSurfaceTexture);
    }

    public interface OnSurfaceCreateListener{
        void onSurfaceCreate();
    }

}
