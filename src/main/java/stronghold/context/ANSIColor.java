package stronghold.context;

public enum ANSIColor {
    BLACK("\033[30m", "\033[40m", "\033[100m", "\033[90m"),
    RED("\033[31m", "\033[41m", "\033[101m", "\033[91m"),
    GREEN("\033[32m", "\033[42m", "\033[102m", "\033[92m"),
    YELLOW("\033[33m", "\033[43m", "\033[103m", "\033[93m"),
    BLUE("\033[34m", "\033[44m", "\033[104m", "\033[94m"),
    PURPLE("\033[35m", "\033[45m", "\033[105m", "\033[95m"),
    CYAN("\033[36m", "\033[46m", "\033[106m", "\033[96m"),
    WHITE("\033[37m", "\033[47m", "\033[107m", "\033[97m"),
    ;

    public static final String RESET = "\033[0m";

    private final String foreground;
    private final String background;
    private final String brightForeground;
    private final String brightBackground;

    ANSIColor(String foreground, String background, String brightBackground,
            String brightForeground) {
        this.foreground = foreground;
        this.brightForeground = brightForeground;
        this.background = background;
        this.brightBackground = brightBackground;
    }

    public String foreground() {
        return foreground;
    }

    public String background() {
        return background;
    }

    public String brightForeground() {
        return brightForeground;
    }

    public String brightBackground() {
        return brightBackground;
    }
}
