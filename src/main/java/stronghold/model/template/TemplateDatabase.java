package stronghold.model.template;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Cleanup;
import lombok.Data;

@Data
public class TemplateDatabase {

    private final Map<String, UnitTemplate> unitTemplates = new HashMap<>();
    private final Map<String, BuildingTemplate> buildingTemplates = new HashMap<>();

    private static <T> T fromFile(File file, Class<T> clazz) throws IOException {
        @Cleanup FileReader fileReader = new FileReader(file);
        return new Gson().fromJson(fileReader, clazz);
    }

    private static void toFile(File file, Object obj) throws IOException {
        @Cleanup FileWriter fileWriter = new FileWriter(file);
        new Gson().toJson(obj, obj.getClass(), fileWriter);
    }

    public void updateFromPath(File root) throws IOException {
        populate(unitTemplates, root, "units", UnitTemplate.class);
        populate(buildingTemplates, root, "buildings", BuildingTemplate.class);
    }

    public void saveToPath(File root) throws IOException {
        depopulate(unitTemplates, root, "units");
        depopulate(buildingTemplates, root, "buildings");
    }

    private <T> void populate(Map<String, T> templates, File root, String subPath, Class<T> clazz)
            throws IOException {
        File path = new File(root, subPath);
        if (!path.exists()) // same as empty directory
            return;
        for (File file : checkNotNull(path.listFiles())) {
            String templateName = file.getName().replace(".json", "");
            T template = fromFile(file, clazz);
            templates.put(templateName, template);
        }
    }

    private <T> void depopulate(Map<String, T> templates, File root, String subpath)
            throws IOException {
        File path = new File(root, subpath);
        for (String templateName : templates.keySet()) {
            File file = new File(path, templateName + ".json");
            file.getParentFile().mkdirs();
            toFile(file, templates.get(templateName));
        }
    }
}
