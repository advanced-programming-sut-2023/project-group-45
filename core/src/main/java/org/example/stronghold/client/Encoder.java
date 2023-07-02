package org.example.stronghold.client;

import org.example.stronghold.model.Building;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.TradeRequest;
import org.example.stronghold.model.Unit;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.BuildingTemplate;
import org.example.stronghold.model.template.GameMapTemplate;
import org.example.stronghold.model.template.UnitTemplate;

public class Encoder {

    public static Object encodeIntoId(Object obj) {
        if (obj instanceof Building x)
            return x.getId();
        if (obj instanceof Unit x)
            return x.getId();
        if (obj instanceof Player x)
            return x.getId();
        if (obj instanceof GameData x)
            return x.getId();
        if (obj instanceof TradeRequest x)
            return x.getId();
        if (obj instanceof User x)
            return x.getUsername();
        if (obj instanceof BuildingTemplate x)
            return x.getType();
        if (obj instanceof GameMapTemplate x)
            return x.getName();
        if (obj instanceof UnitTemplate x)
            return x.getType();
        return null;
    }

    public static Object encodeIntoIdOrDefault(Object obj) {
        Object res = encodeIntoId(obj);
        return res == null ? obj : res;
    }
}
