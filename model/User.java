package model;

public class User {
    private final String username, nickname, email, slogan, securityQuestion, securityAnswer;
    private String password;

    private final Resources resources;

    public User(String username, String password, String nickname, String email, String slogan, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.slogan = slogan;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        resources = new Resources();
    }
}
