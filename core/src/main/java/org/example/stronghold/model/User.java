package org.example.stronghold.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.Serializable;
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

    public FileHandle getAvatar() {
        return Gdx.files.internal("avatars/" + username + ".jpg");
    }

}
