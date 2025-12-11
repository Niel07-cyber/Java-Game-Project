package polymorphicSimulation.agents;

public class Mario extends Agent {
    public static int count = 0;

    public Mario(int x, int y, int maxEnergy) {
        super(x, y, Species.MARIO, maxEnergy);
        count++;
    }
}
