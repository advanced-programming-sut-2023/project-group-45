package model;

public class Game {

    private final Player[] players;
    private final int playerCount;
    private int turn, playerTurn;

    public Game(Player[] players) {
        this.players = players;
        turn = 1;
        playerTurn = 0;
        playerCount = players.length;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getPlayerCount() {
        return playerCount;
    }
}
