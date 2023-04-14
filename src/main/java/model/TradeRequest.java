package model;

public record TradeRequest(String type, int amount, int price, String message) {
}
