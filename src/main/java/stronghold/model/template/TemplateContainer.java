package stronghold.model.template;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Cleanup;
import lombok.Data;

@Data
public class TemplateContainer implements Serializable {

    private final Map<String, UnitTemplate> unitTemplates = new HashMap<>();

    public static <T> T fromFile(File file, Class<T> clazz) throws IOException {
        @Cleanup FileReader fileReader = new FileReader(file);
        return new Gson().fromJson(fileReader, clazz);
    }

    public static <T> T fromFile(String path, Class<T> clazz) throws IOException {
        return fromFile(new File(path), clazz);
    }

    public static void toFile(File file, Object obj) throws IOException {
        @Cleanup FileWriter fileWriter = new FileWriter(file);
        new Gson().toJson(obj, obj.getClass(), fileWriter);
    }

    public static void toFile(String path, Object obj) throws IOException {
        toFile(new File(path), obj);
    }

    public void updateFromPath(String path) throws IOException {
        populate(unitTemplates, path + "/units", UnitTemplate.class);
    }

    private <T> void populate(Map<String, T> templates, String path, Class<T> clazz)
            throws IOException {
        File folder = new File(path);
        for (File file : checkNotNull(folder.listFiles())) {
            String basename = getBasename(file.getName());
            T template = fromFile(file, clazz);
            templates.put(basename, template);
        }
    }

    private String getBasename(String filename) {
        int index = filename.indexOf('.');
        return index == -1 ? filename : filename.substring(0, index);
    }
}
