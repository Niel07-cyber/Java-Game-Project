package polymorphicSimulation.agents;

public class KingBoo extends Agent {
    public static int count = 0;

    public KingBoo(int x, int y, int maxEnergy) {
        super(x, y, Species.KING_BOO, maxEnergy);
        count++;
    }
}
