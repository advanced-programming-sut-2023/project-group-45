package stronghold.view;

import org.junit.jupiter.api.Test;
import stronghold.view.sections.AuthMenu;

public class PasswordTest {

    @Test
    void testIsPasswordWeak(){
        assert !AuthMenu.isPasswordWeak("Ab1@23");
        assert !AuthMenu.isPasswordWeak(".y1X..zxad.");

        assert AuthMenu.isPasswordWeak("Ab1@2");
        assert AuthMenu.isPasswordWeak("AB1@23");
        assert AuthMenu.isPasswordWeak("ab1@23");
        assert AuthMenu.isPasswordWeak("abc@AB");
        assert AuthMenu.isPasswordWeak("ABCABC");
        assert AuthMenu.isPasswordWeak("AB1cde");
    }

    @Test
    void  testGeneratePassword(){
        String password = AuthMenu.generatePassword();
        assert !AuthMenu.isPasswordWeak(password);
    }
}
