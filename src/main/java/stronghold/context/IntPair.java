package stronghold.context;

public record IntPair(int x, int y) {
    public IntPair add(int dx, int dy) {
        return new IntPair(x + dx, y + dy);
    }
}
