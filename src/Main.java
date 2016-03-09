public class Main {

    private static boolean isP3;

    private static Simulation s;

    private static double[] EXPERIMENT1_LAMBDAS = {
            0.1, 0.25, 0.4, 0.55, 0.65, 0.80, 0.90
    };

    private static double[] EXPERIMENT3_LAMBDAS = {
            0.2, 0.4, 0.6, 0.8, 0.9
    };

    private static int[] EXPERIMENT3_BUFFERS = {
            1, 20, 50
    };

    public static void main(String[] args) {

        isP3 = true;

        s = new Simulation();

        if(!isP3) {

            System.out.println("****** EXPERIMENT 1 ******");
            runExperiment1P1();

            System.out.println("****** EXPERIMENT 3 ******");
            runExperiment3P1();
        }
        else {

            // todo
        }
    }

    private static void runExperiment1P1() {

        for(int i = 0; i < EXPERIMENT1_LAMBDAS.length; i++) {

            s.runSimulation(1, EXPERIMENT1_LAMBDAS[i], 1, Double.POSITIVE_INFINITY);
        }
    }

    private static  void runExperiment3P1() {

        for(int i = 0; i < EXPERIMENT3_LAMBDAS.length; i++) {

            for(int j = 0; j < EXPERIMENT3_BUFFERS.length; j++) {

                s.runSimulation(1, EXPERIMENT3_LAMBDAS[i], 1, EXPERIMENT3_BUFFERS[j]);
            }
        }
    }
}
