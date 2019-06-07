package net.gazeplay.commons.gaze.devicemanager;

import com.theeyetribe.clientsdk.GazeManager;
import com.theeyetribe.clientsdk.IGazeListener;
import com.theeyetribe.clientsdk.data.GazeData;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;
import net.gazeplay.commons.utils.games.Utils;

/**
 * Created by schwab on 16/08/2016.
 */
@Slf4j
class EyeTribeGazeDeviceManager extends AbstractGazeDeviceManager implements IGazeListener {

    private GazeManager gazeManager;

    public EyeTribeGazeDeviceManager() {
        super();
    }

    @Override
    public void init() {
        gazeManager = GazeManager.getInstance();
        gazeManager.activate();
        gazeManager.addGazeListener(this);
    }

    @Override
    public void destroy() {
        gazeManager.removeGazeListener(this);
    }

    @Override
    public void onGazeUpdate(GazeData gazeData) {
        Point2D point = new Point2D(gazeData.rawCoordinates.x, gazeData.rawCoordinates.y);
        super.onGazeUpdate(point, gazeData.state == gazeData.STATE_TRACKING_FAIL);
    }
}
