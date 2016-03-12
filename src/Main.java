/**
 * Main class
 * Test token ring protocol
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Main {

    private static Simulation s;

    private static final int EXPERIMENT_A_NUM_HOSTS = 10;
    private static final int EXPERIMENT_B_NUM_HOSTS = 25;
    private static final double[] EXPERIMENT_LAMBDAS = new double[] {
            0.01, 0.05, 0.1, 0.2, 0.3, 0.5, 0.6, 0.7, 0.8, 0.9
    };

    public static void main(String[] args) {

        s = new Simulation();

        System.out.println("**** EXPERIMENT A ****");
        experimentA();

        System.out.println("\n**** EXPERIMENT B ****");
        experimentB();
    }

    public static void experimentA() {

        for(int i = 0 ; i < EXPERIMENT_LAMBDAS.length; i++) {

            s.runSimulation(
                    EXPERIMENT_A_NUM_HOSTS,
                    EXPERIMENT_LAMBDAS[i],
                    Double.POSITIVE_INFINITY
            );
        }
    }

    public static void experimentB() {

        for(int i = 0 ; i < EXPERIMENT_LAMBDAS.length; i++) {

            s.runSimulation(
                    EXPERIMENT_B_NUM_HOSTS,
                    EXPERIMENT_LAMBDAS[i],
                    Double.POSITIVE_INFINITY
            );
        }
    }
}
