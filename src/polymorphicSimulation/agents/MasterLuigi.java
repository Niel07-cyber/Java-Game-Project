package polymorphicSimulation.agents;

public class MasterLuigi extends Master {
    private static MasterLuigi instance;

    private MasterLuigi(int x, int y) {
        super(x, y, Species.LUIGI);
    }

    public static synchronized MasterLuigi getInstance(int x, int y) {
        if (instance == null) {
            instance = new MasterLuigi(x, y);
        }
        return instance;
    }

    public static synchronized MasterLuigi getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MasterLuigi not initialized!");
        }
        return instance;
    }
}
