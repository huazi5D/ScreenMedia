package hz.screenmedia;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Administrator on 2018-01-18.
 */

public class MyCamera {

    private Camera mCamera;

    public MyCamera(MyGLSurfaceView view) {
        try {
            mCamera = Camera.open();
            mCamera.setPreviewTexture(view.getSurfaceTexture());
            mCamera.startPreview();
            view.getSurfaceTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    Log.d("zhx", "onFrameAvailable: ");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
