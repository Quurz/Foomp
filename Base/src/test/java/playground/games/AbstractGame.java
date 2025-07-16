package playground.games;

public abstract class AbstractGame
    implements Game {

    protected String name;
    protected ExecutableGame executableGame;

    protected AbstractGame(final String name,
                           final ExecutableGame executableGame) {
        this.name
            = name;
        this.executableGame
            = executableGame;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void play()
            throws Exception {
        this.executableGame.execute();
    }

}
