package polymorphicSimulation.environment;

import polymorphicSimulation.agents.LivingBeing;
import polymorphicSimulation.agents.Species;
import polymorphicSimulation.utils.MonteCarloRNG;
import polymorphicSimulation.utils.Direction;
import polymorphicSimulation.style.ColorInConsole;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private final int width;
    private final int height;
    private final Object[][] grid; // LivingBeing or Obstacle
    private final List<LivingBeing> agents;

    private static final int SAFE_ZONE_SIZE = 5;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Object[width][height];
        this.agents = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addAgent(LivingBeing agent) {
        if (isValid(agent.getX(), agent.getY()) && isFree(agent.getX(), agent.getY())) {
            grid[agent.getX()][agent.getY()] = agent;
            agents.add(agent);
        } else {
            System.err.println("Could not place agent at " + agent.getX() + "," + agent.getY());
        }
    }

    public void addObstacle(int x, int y) {
        if (isValid(x, y) && isFree(x, y)) {
            grid[x][y] = new Obstacle();
        }
    }

    public boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isFree(int x, int y) {
        return grid[x][y] == null;
    }

    public Object getEntityAt(int x, int y) {
        if (!isValid(x, y))
            return null;
        return grid[x][y];
    }

    public void moveAgent(LivingBeing agent, int newX, int newY) {
        grid[agent.getX()][agent.getY()] = null;
        agent.setPosition(newX, newY);
        grid[newX][newY] = agent;
    }

    public void removeAgent(LivingBeing agent) {
        grid[agent.getX()][agent.getY()] = null;
        grid[agent.getX()][agent.getY()] = new Obstacle();
        agents.remove(agent);
    }

    public boolean isInSafeZone(LivingBeing agent) {
        return isInSafeZone(agent.getX(), agent.getY(), agent.getSpecies());
    }

    public boolean isInSafeZone(int x, int y, Species species) {
        switch (species) {
            case BOWSER:
                return x < SAFE_ZONE_SIZE && y < SAFE_ZONE_SIZE;
            case KING_BOO:
                return x >= width - SAFE_ZONE_SIZE && y < SAFE_ZONE_SIZE;
            case LUIGI:
                return x < SAFE_ZONE_SIZE && y >= height - SAFE_ZONE_SIZE;
            case MARIO:
                return x >= width - SAFE_ZONE_SIZE && y >= height - SAFE_ZONE_SIZE;
            default:
                return false;
        }
    }

    public boolean isRestrictedSafeZone(int x, int y, Species mySpecies) {
        for (Species s : Species.values()) {
            if (s != mySpecies && isInSafeZone(x, y, s)) {
                return true;
            }
        }
        return false;
    }

    public Direction getDirectionToSafeZone(LivingBeing agent) {
        int targetX = 0, targetY = 0;
        switch (agent.getSpecies()) {
            case BOWSER:
                targetX = 0;
                targetY = 0;
                break;
            case KING_BOO:
                targetX = width - 1;
                targetY = 0;
                break;
            case LUIGI:
                targetX = 0;
                targetY = height - 1;
                break;
            case MARIO:
                targetX = width - 1;
                targetY = height - 1;
                break;
        }

        int dx = Integer.compare(targetX, agent.getX());
        int dy = Integer.compare(targetY, agent.getY());

        for (Direction d : Direction.values()) {
            if (d.dx == dx && d.dy == dy)
                return d;
        }
        return MonteCarloRNG.getItem(Direction.values());
    }

    public List<LivingBeing> getAgents() {
        return agents;
    }

    public int getSurroundingTilesCount(int x, int y) {
        if (!isValid(x, y))
            return 0;

        boolean isLeft = (x == 0);
        boolean isRight = (x == width - 1);
        boolean isTop = (y == 0);
        boolean isBottom = (y == height - 1);

        if ((isLeft || isRight) && (isTop || isBottom)) {
            return 3; // Corner
        } else if (isLeft || isRight || isTop || isBottom) {
            return 5; // Border
        } else {
            return 8; // Center
        }
    }

    public void display() {
        final String RESET = ColorInConsole.RESET;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Object obj = grid[x][y];
                if (obj == null) {
                    System.out.print(".");
                } else if (obj instanceof Obstacle) {
                    System.out.print("#");
                } else if (obj instanceof LivingBeing) {
                    LivingBeing lb = (LivingBeing) obj;
                    System.out.print(lb.getSpecies().getColorCode() + lb.getSymbol() + RESET);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
