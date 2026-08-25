package puzzlegame.ui;

public class LoginJFrameTest {
    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        check(LoginJFrame.isLegalInput("player_01"),
                "letters, digits, and underscores should be legal");
        check(!LoginJFrame.isLegalInput(""), "empty input should be illegal");
        check(!LoginJFrame.isLegalInput("player!"), "punctuation should be illegal");
        check(LoginJFrame.isCaptchaCorrect("A1B2", "a1b2"),
                "captcha comparison should ignore letter case");
        check(!LoginJFrame.isCaptchaCorrect("A1B3", "A1B2"),
                "different captcha values should be rejected");

        String captcha = LoginJFrame.createVerificationCode();
        check(captcha.matches("[A-Z0-9]{4}"),
                "generated captcha should contain four uppercase letters or digits");
    }
}
