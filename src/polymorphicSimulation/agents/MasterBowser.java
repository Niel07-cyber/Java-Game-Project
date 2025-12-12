package polymorphicSimulation.agents;

public class MasterBowser extends Master {
    private static MasterBowser instance;

    private MasterBowser(int x, int y) {
        super(x, y, Species.BOWSER);
    }

    public static synchronized MasterBowser getInstance(int x, int y) {
        if (instance == null) {
            instance = new MasterBowser(x, y);
        }
        return instance;
    }

    public static synchronized MasterBowser getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MasterBowser not initialized!");
        }
        return instance;
    }
}
