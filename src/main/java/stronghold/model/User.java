package stronghold.model;

import stronghold.context.HashedString;
import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;

@Data
public class User implements Serializable {

    @NonNull
    private String username;
    @NonNull
    private HashedString password;
    @NonNull
    private String nickname;
    @NonNull
    private String email;
    private String slogan;
    private String securityQuestion;
    private String securityAnswer;
}
