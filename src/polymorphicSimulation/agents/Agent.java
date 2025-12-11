package src.polymorphicSimulation.agents;

import src.polymorphicSimulation.utils.MonteCarloRNG;
import src.polymorphicSimulation.utils.Direction;
import src.polymorphicSimulation.environment.Map;
import src.polymorphicSimulation.environment.Obstacle;

public class Agent extends LivingBeing {
    protected int energyPoints;
    protected int maxEnergy;
    protected Direction lastDirection;

    public Agent(int x, int y, Species species, int maxEnergy) {
        super(x, y, species);
        this.maxEnergy = maxEnergy;
        this.energyPoints = maxEnergy;
        this.lastDirection = MonteCarloRNG.getItem(Direction.VALUES);
    }

    public int getEnergy() {
        return energyPoints;
    }

    public void move(Map map) {
        // Can't move if we're dead
        if (energyPoints <= 0) {
            System.out.println(this + " ran out of energy and turned into an obstacle.");
            map.removeAgent(this);
            return;
        }

        // Heal up if we made it home
        if (map.isInSafeZone(this)) {
            energyPoints = Math.min(maxEnergy, energyPoints + 5);
        } else {
            int targetX = x;
            int targetY = y;
            Direction moveDir = null;

            double epRatio = (double) energyPoints / maxEnergy;

            // Explore freely unless energy is critical (< 20%)
            if (epRatio >= 0.20) {
                moveDir = MonteCarloRNG.getItem(Direction.VALUES);
            } else {
                moveDir = map.getDirectionToSafeZone(this);
            }

            targetX += moveDir.dx;
            targetY += moveDir.dy;

            // Hit a wall or enemy safe zone? Stay put, burn energy.
            if (!map.isValid(targetX, targetY) || map.isRestrictedSafeZone(targetX, targetY, this.species)) {
                consumeEnergy(map);
                lastDirection = moveDir;
                return;
            }

            Object target = map.getEntityAt(targetX, targetY);

            if (target == null) {
                map.moveAgent(this, targetX, targetY);
                consumeEnergy(map);
                lastDirection = moveDir;
            } else if (target instanceof Obstacle) {
                consumeEnergy(map);
                lastDirection = moveDir;
            } else if (target instanceof LivingBeing) {
                interact((LivingBeing) target);
                consumeEnergy(map);
            }
        }

        // Always check if Master is nearby to offload data
        scanForMaster(map);
    }

    private void scanForMaster(Map map) {
        for (Direction d : Direction.VALUES) {
            int tx = x + d.dx;
            int ty = y + d.dy;
            if (map.isValid(tx, ty)) {
                Object obj = map.getEntityAt(tx, ty);
                if (obj instanceof Master) {
                    Master master = (Master) obj;
                    if (master.getSpecies() == this.species) {
                        shareKnowledge(master);
                        // Download Master's knowledge too (union)
                        master.getKnowledge().forEach(this::addMessage);
                    }
                }
            }
        }
    }

    private void consumeEnergy(Map map) {
        if (!map.isInSafeZone(this)) {
            energyPoints -= 2;
        }
    }

    protected void interact(LivingBeing other) {
        String reset = "\u001B[0m";
        System.out.println(this.species.getColorCode() + this + reset + " interacts with "
                + other.getSpecies().getColorCode() + other + reset);

        if (other.getSpecies() == this.species) {
            // Friend: Swap everything
            shareKnowledge(other);
            other.getKnowledge().forEach(this::addMessage);
        } else if (other.getSpecies().getAlliance() == this.species.getAlliance()) {
            // Ally: Swap a few random stories
            shareRandomMessages(other);
        } else {
            // Enemy: Fight!
            fight(other);
        }
    }

    private void shareKnowledge(LivingBeing other) {
        for (Message m : this.knowledge) {
            other.addMessage(m);
        }
    }

    private void shareRandomMessages(LivingBeing other) {
        for (int i = 0; i < 3; i++) {
            if (!this.knowledge.isEmpty()) {
                other.addMessage(MonteCarloRNG.getItem(this.knowledge.toArray(new Message[0])));
            }
            if (!other.getKnowledge().isEmpty()) {
                this.addMessage(MonteCarloRNG.getItem(other.getKnowledge().toArray(new Message[0])));
            }
        }
    }

    private void fight(LivingBeing other) {
        boolean iWin = MonteCarloRNG.chance(0.5);
        if (iWin) {
            stealMessage(other, this);
        } else {
            stealMessage(this, other);
        }
    }

    private void stealMessage(LivingBeing loser, LivingBeing winner) {
        if (loser.getKnowledge().isEmpty())
            return;

        int stealCount = Math.max(1, loser.getKnowledge().size() / 2);

        for (int i = 0; i < stealCount; i++) {
            if (loser.getKnowledge().isEmpty())
                break;

            Message stolen = MonteCarloRNG.getItem(loser.getKnowledge().toArray(new Message[0]));
            if (stolen != null) {
                winner.addMessage(stolen);
                loser.getKnowledge().remove(stolen);
                String reset = "\u001B[0m";
                System.out.println(winner.getSpecies().getColorCode() + winner + reset + " stole " + stolen + " from "
                        + loser.getSpecies().getColorCode() + loser + reset);
            }
        }
    }

    @Override
    public String toString() {
        return species.name() + "-" + Integer.toHexString(hashCode()).substring(0, 4);
    }
}
