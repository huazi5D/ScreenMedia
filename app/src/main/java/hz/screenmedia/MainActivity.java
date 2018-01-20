package hz.screenmedia;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.view.TextureView;

public class MainActivity extends BaseActivity {

    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;

    private MyGLSurfaceView mGLSurfaceView;
    private TextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGLSurfaceView = findViewById(R.id.display);
        /*mGLSurfaceView.setOnSurfaceCreateListener(new MyGLSurfaceView.OnSurfaceCreateListener() {
            @Override
            public void onSurfaceCreate() {

                MyCamera myCamera = new MyCamera(mGLSurfaceView);

            }
        });*/

        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        Intent captureIntent= projectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, 1);

        /*mSurfaceTexture = new SurfaceTexture(5);
        mSurfaceTexture.setDefaultBufferSize(500, 500);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                Log.d("zhx", "onFrameAvailable: ");
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay(
                    "MainScreen",
                    500,
                    500,
                    mDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mGLSurfaceView.getSurface(),
                    null, null);
        }
    }
}
