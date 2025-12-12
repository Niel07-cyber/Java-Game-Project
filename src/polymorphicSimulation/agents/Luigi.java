package polymorphicSimulation.agents;

public class Luigi extends Agent {
    public static int count = 0;

    public Luigi(int x, int y, int maxEnergy) {
        super(x, y, Species.LUIGI, maxEnergy);
        count++;
    }
}
