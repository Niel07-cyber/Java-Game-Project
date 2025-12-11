package polymorphicSimulation.agents;

import java.util.HashSet;
import java.util.Set;

public abstract class LivingBeing {
    protected int x;
    protected int y;
    protected Species species;
    protected Set<Message> knowledge;

    public LivingBeing(int x, int y, Species species) {
        this.x = x;
        this.y = y;
        this.species = species;
        this.knowledge = new HashSet<>();
    }

    public String getSymbol() {
        return species.getSymbol();
    }

    public Species getSpecies() {
        return species;
    }

    public Set<Message> getKnowledge() {
        return knowledge;
    }

    public void addMessage(Message m) {
        knowledge.add(m);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
