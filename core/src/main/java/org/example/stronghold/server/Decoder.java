package org.example.stronghold.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static void decodeOperatorRequest(Map<String, Object> req) {
        decodeSingle(req, "user", "User");
        decodeSingle(req, "map", "GameMapTemplate");
        decodeList(req, "users", "User");
        decodeList(req, "units", "Unit");
        decodeSingle(req, "game", "GameData");
        decodeSingle(req, "player", "Player");
        decodeBuilding(req);
        decodeSingle(req, "target", "Player");
        decodeSingle(req, "request", "TradeRequest");
    }

    private static void decodeSingle(Map<String, Object> req, String key, String type) {
        Object id = req.get(key);
        if (id == null)
            return;
        Object obj = decodeWithId(type, id);
        if (obj == null) {
            req.remove(key);
            return;
        }
        req.put(key, obj);
    }

    private static void decodeList(Map<String, Object> req, String key, String type) {
        if (!req.containsKey(key))
            return;
        if (!(req.get(key) instanceof List rawList))
            return;
        List list = new ArrayList<>(rawList);
        req.put(key, list);
        for (int i = 0; i < list.size(); i++) {
            Object obj = decodeWithId(type, list.get(i));
            if (obj == null) {
                list.remove(i);
                i--;
            } else {
                list.set(i, obj);
            }
        }
    }

    private static void decodeBuilding(Map<String, Object> req) {
        if (!req.containsKey("building"))
            return;
        if (!(req.get("building") instanceof String))
            decodeSingle(req, "building", "Building");
    }
}
