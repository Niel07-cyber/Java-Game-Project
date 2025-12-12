package polymorphicSimulation.agents;

public abstract class Master extends LivingBeing {

    protected Master(int x, int y, Species species) {
        super(x, y, species);
    }

    public void move() {
    }

    public void reset() {
        this.knowledge.clear();
    }

    @Override
    public String getSymbol() {
        return species.getMasterSymbol();
    }

    @Override
    public String toString() {
        return "MASTER_" + species.name();
    }
}
