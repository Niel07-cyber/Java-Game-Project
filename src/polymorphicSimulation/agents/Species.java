package polymorphicSimulation.agents;

import polymorphicSimulation.style.ColorInConsole;

public enum Species {
    BOWSER(Alliance.EVIL, "B", "MB", ColorInConsole.YELLOW, MovementType.KING),
    KING_BOO(Alliance.EVIL, "K", "MK", ColorInConsole.PURPLE, MovementType.ROOK),
    LUIGI(Alliance.GOOD, "L", "ML", ColorInConsole.GREEN, MovementType.BISHOP),
    MARIO(Alliance.GOOD, "M", "MM", ColorInConsole.RED, MovementType.QUEEN);

    public enum MovementType {
        KING, ROOK, BISHOP, QUEEN
    }

    private final Alliance alliance;
    private final String symbol;
    private final String masterSymbol;
    private final String colorCode;
    private final MovementType movementType;

    Species(Alliance alliance, String symbol, String masterSymbol, String colorCode, MovementType movementType) {
        this.alliance = alliance;
        this.symbol = symbol;
        this.masterSymbol = masterSymbol;
        this.colorCode = colorCode;
        this.movementType = movementType;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getMasterSymbol() {
        return masterSymbol;
    }

    public String getColorCode() {
        return colorCode;
    }

    public MovementType getMovementType() {
        return movementType;
    }
}
