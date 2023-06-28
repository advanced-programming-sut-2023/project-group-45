package org.example.stronghold.gui;

import com.badlogic.gdx.graphics.Texture;
import java.util.List;
import java.util.Random;
import lombok.Data;

@Data
public class Captcha {

    private final String answer;
    private final Texture texture;

    public static Captcha generate(AssetLoader assetLoader) {
        List<Captcha> allCaptcha = assetLoader.getAssets().entrySet().stream()
            .filter(e -> e.getKey().startsWith("captcha/"))
            .map(e -> new Captcha(getCaptchaAnswer(e.getKey()), (Texture) e.getValue()))
            .toList();
        return allCaptcha.get(new Random().nextInt(allCaptcha.size()));
    }

    public static String getCaptchaAnswer(String filename) {
        String prefix = "captcha/";
        return filename.substring(prefix.length(), prefix.length() + 4);
    }
}
