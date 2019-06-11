package net.gazeplay.games.creampie;

import javafx.geometry.Dimension2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import net.gazeplay.GameContext;
import net.gazeplay.GameLifeCycle;
import net.gazeplay.commons.gaze.devicemanager.GazeEvent;
import net.gazeplay.commons.utils.Portrait;
import net.gazeplay.commons.utils.RandomPositionGenerator;
import net.gazeplay.commons.utils.games.ImageLibrary;
import net.gazeplay.commons.utils.stats.Stats;

/**
 * Created by schwab on 12/08/2016.
 */
@Slf4j
public class CreamPie implements GameLifeCycle {

    private final GameContext gameContext;

    private final Stats stats;

    private final Rectangle blinkDetectionLayer;

    private final Hand hand;

    private final Target target;

    public CreamPie(GameContext gameContext, Stats stats) {
        super();
        this.gameContext = gameContext;
        this.stats = stats;

        final ImageLibrary imageLibrary = Portrait.createImageLibrary();
        final RandomPositionGenerator randomPositionGenerator = gameContext.getRandomPositionGenerator();

        hand = new Hand();
        target = new Target(randomPositionGenerator, hand, stats, gameContext, imageLibrary);
        Dimension2D dims = gameContext.getGamePanelDimensionProvider().getDimension2D();
        blinkDetectionLayer = new Rectangle(0, 0, dims.getWidth(), dims.getHeight());
        blinkDetectionLayer.setFill(Color.TRANSPARENT);

        blinkDetectionLayer.addEventFilter(GazeEvent.GAZE_EXITED, e -> {
            if(e.isBlinking()){
                stats.blink();
            }
        });

        this.gameContext.getGazeDeviceManager().addEventFilter(blinkDetectionLayer);

        gameContext.getChildren().add(target);
        gameContext.getChildren().add(hand);
        gameContext.getChildren().add(blinkDetectionLayer);
    }

    @Override
    public void launch() {
        hand.recomputePosition();

        gameContext.getRoot().widthProperty().addListener((obs, oldVal, newVal) -> {
            hand.recomputePosition();
        });
        gameContext.getRoot().heightProperty().addListener((obs, oldVal, newVal) -> {
            hand.recomputePosition();
        });

    }

    @Override
    public void dispose() {

    }
}
