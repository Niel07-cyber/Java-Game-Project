package src.polymorphicSimulation.agents;

import src.polymorphicSimulation.style.ColorInConsole;

public enum Species {
    BOWSER(Alliance.EVIL, "B", "MB", ColorInConsole.YELLOW),
    KING_BOO(Alliance.EVIL, "K", "MK", ColorInConsole.PURPLE),
    LUIGI(Alliance.GOOD, "L", "ML", ColorInConsole.GREEN),
    MARIO(Alliance.GOOD, "M", "MM", ColorInConsole.RED);

    private final Alliance alliance;
    private final String symbol;
    private final String masterSymbol;
    private final String colorCode;

    Species(Alliance alliance, String symbol, String masterSymbol, String colorCode) {
        this.alliance = alliance;
        this.symbol = symbol;
        this.masterSymbol = masterSymbol;
        this.colorCode = colorCode;
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
}
