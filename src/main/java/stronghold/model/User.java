package stronghold.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import stronghold.context.HashedString;

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
}
