package org.example.stronghold.server;

import org.example.stronghold.model.Building;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.TradeRequest;
import org.example.stronghold.model.Unit;
import org.example.stronghold.operator.Operators;

public class Decoder {

    public static Object decodeWithLongId(String type, Long id) {
        return switch (type) {
            case "Building" -> Building.OBJECTS.get(id);
            case "Unit" -> Unit.OBJECTS.get(id);
            case "Player" -> Player.OBJECTS.get(id);
            case "GameData" -> GameData.OBJECTS.get(id);
            case "TradeRequest" -> TradeRequest.OBJECTS.get(id);
            default -> null;
        };
    }

    public static Object decodeWithStringId(String type, String id) {
        return switch (type) {
            case "User" -> Operators.database.getUserFromUsername(id);
            case "BuildingTemplate" -> Operators.templateDatabase.getBuildingTemplates().get(id);
            case "GameMapTemplate" -> Operators.templateDatabase.getGameMapTemplates().get(id);
            case "UnitTemplate" -> Operators.templateDatabase.getUnitTemplates().get(id);
            default -> null;
        };
    }

    public static Object decodeWithId(String type, Object id) {
        if (id instanceof Long longId)
            return decodeWithLongId(type, longId);
        if (id instanceof String stringId)
            return decodeWithStringId(type, stringId);
        return null;
    }

}
