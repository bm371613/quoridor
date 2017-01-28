package quoridor.analysis;

public final class Main {

    private static final Experiment[] EXPERIMENTS = {
            new Experiment(),
            Experiment.experiment1(),
    };


    private Main() {
    }

    public static void main(String[] args) {
        int turnTime = 2000;
        int maxTurnCount = 200;
        int verbosity = 1;

        String cmd = args[0];
        int experimentIx = Integer.parseInt(args[1]);
        Experiment experiment = EXPERIMENTS[experimentIx];

        if ("run".equals(cmd)) {
            int[] botIxs;
            botIxs = new int[args.length - 2];
            for (int i = 0; i < args.length - 2; ++i) {
                botIxs[i] = Integer.parseInt(args[2 + i]);
                if (verbosity >= 1) {
                    System.out.println("" + botIxs[i]
                            + " PLAYER " + experiment.getName(botIxs[i]));
                }
            }

            int winner;

            GameRunner runner = new GameRunner(turnTime, maxTurnCount,
                    verbosity);

            switch (botIxs.length) {
                case 2:
                    winner = runner.run(
                            experiment.getBot(botIxs[0]),
                            experiment.getBot(botIxs[1]));
                    break;
                case 4:
                    winner = runner.run(
                            experiment.getBot(botIxs[0]),
                            experiment.getBot(botIxs[1]),
                            experiment.getBot(botIxs[2]),
                            experiment.getBot(botIxs[3]));
                    break;
                default:
                    throw new RuntimeException("Unsupported number of players");
            }

            if (winner > -1) {
                System.out.println(
                        "WINNER " + winner + " " + botIxs[winner] + " "
                        + experiment.getName(botIxs[winner]));
            } else {
                System.out.println("DRAW");
            }
        } else if ("show".equals(cmd)) {
            for (int i = 0; i < experiment.getSize(); ++i) {
                System.out.println("" + i + " " + experiment.getName(i));
            }
        }
    }
}
