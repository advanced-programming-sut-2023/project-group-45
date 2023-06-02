package org.example.stronghold.model.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.stronghold.model.Unit;

class UnitTemplateTest {

    TemplateDatabase templateDatabase = new TemplateDatabase();

    @BeforeEach
    void loadTemplateDatabase() throws IOException {
        templateDatabase.updateFromPath(new File("./data"));
    }

    @Test
    void testInitializedFields() {
        UnitTemplate unitTemplate = templateDatabase.getUnitTemplates().get("Archer");
        Unit unit = unitTemplate.getBuilder().build();
        assertEquals(unit.getMaxHitPoints(), unit.getHitPoints());
    }

}