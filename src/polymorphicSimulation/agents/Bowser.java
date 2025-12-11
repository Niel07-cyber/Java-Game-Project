package polymorphicSimulation.agents;

public class Bowser extends Agent {
    public static int count = 0;

    public Bowser(int x, int y, int maxEnergy) {
        super(x, y, Species.BOWSER, maxEnergy);
        count++;
    }
}
