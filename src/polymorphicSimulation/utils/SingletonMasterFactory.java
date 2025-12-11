package polymorphicSimulation.utils;

import polymorphicSimulation.agents.*;

public class SingletonMasterFactory {

    public static Agent createAgent(Species species, int x, int y, int maxEnergy) {
        switch (species) {
            case BOWSER:
                return new Bowser(x, y, maxEnergy);
            case KING_BOO:
                return new KingBoo(x, y, maxEnergy);
            case LUIGI:
                return new Luigi(x, y, maxEnergy);
            case MARIO:
                return new Mario(x, y, maxEnergy);
            default:
                throw new IllegalArgumentException("Unknown species: " + species);
        }
    }

    public static Master getMaster(Species species, int x, int y) {
        switch (species) {
            case BOWSER:
                return MasterBowser.getInstance(x, y);
            case KING_BOO:
                return MasterKingBoo.getInstance(x, y);
            case LUIGI:
                return MasterLuigi.getInstance(x, y);
            case MARIO:
                return MasterMario.getInstance(x, y);
            default:
                throw new IllegalArgumentException("Unknown Master species");
        }
    }

    public static Master getMaster(Species species) {
        switch (species) {
            case BOWSER:
                return MasterBowser.getInstance();
            case KING_BOO:
                return MasterKingBoo.getInstance();
            case LUIGI:
                return MasterLuigi.getInstance();
            case MARIO:
                return MasterMario.getInstance();
            default:
                throw new IllegalArgumentException("Unknown Master species");
        }
    }
}
