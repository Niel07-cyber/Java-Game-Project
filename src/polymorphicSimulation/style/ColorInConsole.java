package polymorphicSimulation.style;

public class ColorInConsole {
    private static final boolean COLORS_ENABLED = true;

    public static final String RESET = COLORS_ENABLED ? "\u001B[0m" : "";
    public static final String RED = COLORS_ENABLED ? "\u001B[31m" : "";
    public static final String GREEN = COLORS_ENABLED ? "\u001B[32m" : "";
    public static final String YELLOW = COLORS_ENABLED ? "\u001B[33m" : "";
    public static final String PURPLE = COLORS_ENABLED ? "\u001B[35m" : "";
}
