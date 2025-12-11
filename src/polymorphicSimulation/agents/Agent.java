package polymorphicSimulation.agents;

import polymorphicSimulation.utils.MonteCarloRNG;
import polymorphicSimulation.utils.Direction;
import polymorphicSimulation.environment.Map;
import polymorphicSimulation.environment.Obstacle;

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

    private void moveKing(Map map) {
        moveSingleStep(map, MonteCarloRNG.getItem(Direction.VALUES));
    }

    private void moveRook(Map map) {
        Direction[] dirs = { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };
        moveMultiStep(map, MonteCarloRNG.getItem(dirs));
    }

    private void moveBishop(Map map) {
        Direction[] dirs = { Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST };
        moveMultiStep(map, MonteCarloRNG.getItem(dirs));
    }

    private void moveQueen(Map map) {
        moveMultiStep(map, MonteCarloRNG.getItem(Direction.VALUES));
    }

    private void moveMultiStep(Map map, Direction d) {
        int steps = MonteCarloRNG.getInt(1, 3);
        for (int i = 0; i < steps; i++) {
            if (!attemptMove(map, d)) {
                break; // Stop if blocked
            }
        }
    }

    private void moveSingleStep(Map map, Direction d) {
        attemptMove(map, d);
    }

    private boolean attemptMove(Map map, Direction d) {
        int targetX = x + d.dx;
        int targetY = y + d.dy;

        // Hit a wall or enemy safe zone?
        if (!map.isValid(targetX, targetY) || map.isRestrictedSafeZone(targetX, targetY, this.species)) {
            consumeEnergy(map);
            lastDirection = d;
            return false;
        }

        Object target = map.getEntityAt(targetX, targetY);

        if (target == null) {
            map.moveAgent(this, targetX, targetY);
            consumeEnergy(map);
            lastDirection = d;
            return true;
        } else if (target instanceof Obstacle) {
            consumeEnergy(map);
            lastDirection = d;
            return false;
        } else if (target instanceof LivingBeing) {
            interact((LivingBeing) target);
            consumeEnergy(map);
            return false;
        }
        return false;
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
            double epRatio = (double) energyPoints / maxEnergy;

            // Explore freely unless energy is critical (< 20%)
            if (epRatio >= 0.20) {
                switch (species.getMovementType()) {
                    case KING:
                        moveKing(map);
                        break;
                    case ROOK:
                        moveRook(map);
                        break;
                    case BISHOP:
                        moveBishop(map);
                        break;
                    case QUEEN:
                        moveQueen(map);
                        break;
                }
            } else {
                Direction moveDir = map.getDirectionToSafeZone(this);
                moveSingleStep(map, moveDir);
            }
        }

        // Always check if Master is nearby to offload data
        scanForMaster(map);

        // Check for death (EP <= 0)
        if (energyPoints <= 0) {
            die(map);
        }
    }

    private void die(Map map) {
        // "becomes an obstacle and therefore loses all his messages"
        this.knowledge.clear();
        map.removeAgent(this);
        System.out.println(this.species.getColorCode() + this.toString() + " ran out of energy and became an Obstacle!"
                + polymorphicSimulation.style.ColorInConsole.RESET);
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
        String reset = polymorphicSimulation.style.ColorInConsole.RESET;
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

    private enum RPS {
        ROCK, PAPER, SCISSORS
    }

    private void fight(LivingBeing other) {
        String reset = polymorphicSimulation.style.ColorInConsole.RESET;
        boolean winnerFound = false;
        LivingBeing winner = null;
        LivingBeing loser = null;

        while (!winnerFound) {
            RPS myChoice = MonteCarloRNG.getItem(RPS.values());
            RPS otherChoice = MonteCarloRNG.getItem(RPS.values());

            System.out.println("  > FIGHT: " + this + " chose " + myChoice + " vs " + other + " chose " + otherChoice);

            if (myChoice == otherChoice) {
                System.out.println("  > It's a TIE! Re-throwing...");
            } else if ((myChoice == RPS.ROCK && otherChoice == RPS.SCISSORS) ||
                    (myChoice == RPS.PAPER && otherChoice == RPS.ROCK) ||
                    (myChoice == RPS.SCISSORS && otherChoice == RPS.PAPER)) {
                winner = this;
                loser = other;
                winnerFound = true;
            } else {
                winner = other;
                loser = this;
                winnerFound = true;
            }
        }

        System.out.println("  > Winner: " + winner.getSpecies().getColorCode() + winner + reset);
        stealMessage(loser, winner);
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
                String reset = polymorphicSimulation.style.ColorInConsole.RESET;
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
