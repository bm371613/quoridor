package quoridor.analysis;

public final class Main {

    private static final Experiment[] EXPERIMENTS = {
            new Experiment(),
            Experiment.experiment1(),
    };


    private Main() {
    }

    public static void main(String[] args) {
        int turnTime = 5000;
        int maxTurnCount = 200;

        String cmd = args[0];
        int experimentIx = Integer.parseInt(args[1]);
        Experiment experiment = EXPERIMENTS[experimentIx];

        if ("run".equals(cmd)) {
            int[] boxIxs;
            boxIxs = new int[args.length - 2];
            for (int i = 0; i < args.length - 2; ++i) {
                boxIxs[i] = Integer.parseInt(args[2 + i]);
            }

            int winner;

            GameRunner runner = new GameRunner(turnTime, maxTurnCount, 0);

            switch (boxIxs.length) {
                case 2:
                    winner = runner.run(
                            experiment.getBot(0),
                            experiment.getBot(1));
                    break;
                case 4:
                    winner = runner.run(
                            experiment.getBot(0),
                            experiment.getBot(1),
                            experiment.getBot(2),
                            experiment.getBot(3));
                    break;
                default:
                    throw new RuntimeException("Unsupported number of players");
            }

            System.out.println(winner);
        } else if ("show".equals(cmd)) {
            for (int i = 0; i < experiment.getSize(); ++i) {
                System.out.println("" + i + " " + experiment.getName(i));
            }
        }
    }
}
