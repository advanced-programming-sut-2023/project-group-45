package org.example.stronghold.cli;

import static org.junit.jupiter.api.Assertions.*;

import org.example.stronghold.cli.sections.AuthMenu;
import org.junit.jupiter.api.Test;

public class PasswordTest {

    @Test
    void testIsPasswordWeak() {
        assertFalse(AuthMenu.isPasswordWeak("Ab1@23"));
        assertFalse(AuthMenu.isPasswordWeak(".y1X..zxad."));

        assertTrue(AuthMenu.isPasswordWeak("Ab1@2"));
        assertTrue(AuthMenu.isPasswordWeak("ab1@23"));
        assertTrue(AuthMenu.isPasswordWeak("abc@AB"));
        assertTrue(AuthMenu.isPasswordWeak("ABCABC"));
        assertTrue(AuthMenu.isPasswordWeak("AB1cde"));
    }

    @Test
    void testGeneratePassword() {
        String password = AuthMenu.generatePassword();
        assertFalse(AuthMenu.isPasswordWeak(password));
    }
}
