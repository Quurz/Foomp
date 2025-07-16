package playground;

import playground.games.Game;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class Playground {

    public static final Logger LOGGER
        = getLogger(Playground.class);

    public static void main(final String... args) {
        final var playground
            = new Playground();
        try {
            LOGGER.info(">>>>>> Let's play");
            playground.playGames();
            LOGGER.info(">>>>>> Playtime is over");
        } catch (final Exception exception) {
            LOGGER.error(">>>>>> Ouch!" , exception);
        }
    }

    private void playGames(Game... games) {
        for (final var game : games) {
            try {
                LOGGER.info(">>>> Let's start the {}", game.getName());
                game.play();
                LOGGER.info(">>>> We finished the {}", game.getName());
            } catch (final Exception exception) {
                LOGGER.error(">>>> The {} is broken... =(", game.getName(), exception);
            }
        }
    }

}
