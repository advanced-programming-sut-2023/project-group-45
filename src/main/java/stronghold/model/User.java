package stronghold.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;

@Data
public class User implements Serializable {

    @NonNull
    private String username;
    @NonNull
    private String hashedPassword;
    @NonNull
    private String nickname;
    @NonNull
    private String email;
    private String slogan;
    private String securityQuestion;
    private String securityAnswer;
}
