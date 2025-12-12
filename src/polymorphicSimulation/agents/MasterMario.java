package polymorphicSimulation.agents;

public class MasterMario extends Master {
    private static MasterMario instance;

    private MasterMario(int x, int y) {
        super(x, y, Species.MARIO);
    }

    public static synchronized MasterMario getInstance(int x, int y) {
        if (instance == null) {
            instance = new MasterMario(x, y);
        }
        return instance;
    }

    public static synchronized MasterMario getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MasterMario not initialized!");
        }
        return instance;
    }
}
