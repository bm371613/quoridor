package quoridor.gui.management;

import quoridor.ai.Bot;

public enum Player {
    HUMAN(null);

    private Bot bot;

    Player(Bot bot) {
        this.bot = bot;
    }

    public Bot getBot() {
        return bot;
    }
}
