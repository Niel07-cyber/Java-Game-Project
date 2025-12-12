package polymorphicSimulation.agents;

public class MasterKingBoo extends Master {
    private static MasterKingBoo instance;

    private MasterKingBoo(int x, int y) {
        super(x, y, Species.KING_BOO);
    }

    public static synchronized MasterKingBoo getInstance(int x, int y) {
        if (instance == null) {
            instance = new MasterKingBoo(x, y);
        }
        return instance;
    }

    public static synchronized MasterKingBoo getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MasterKingBoo not initialized!");
        }
        return instance;
    }
}
