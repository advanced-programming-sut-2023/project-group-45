package org.example.stronghold.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.example.stronghold.context.HashedString;

@Data
@Builder(toBuilder = true)
public class User implements Serializable {

    @NonNull
    private String username;
    @NonNull
    @ToString.Exclude // avoid long logs
    private HashedString password;
    @NonNull
    private String nickname;
    @NonNull
    private String email;
    private String slogan;
    private String securityQuestion;
    private String securityAnswer;
    private int score;

    public Path getAvatar() {
        return new File("assets/avatars/" + username + ".jpg").toPath();
    }

    public FileHandle getAvatarFileHandle() {
        return Gdx.files.internal("avatars/" + username + ".jpg");
    }

}
