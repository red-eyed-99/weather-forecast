package utils;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.params.provider.Arguments;
import java.util.List;
import java.util.stream.Stream;

import static dto.auth.SignUpUserDTO.Fields.PASSWORD;
import static dto.auth.SignUpUserDTO.Fields.REPEAT_PASSWORD;
import static dto.auth.SignUpUserDTO.Fields.USERNAME;
import static utils.ModelAttributeUtil.PASSWORD_ERROR;
import static utils.ModelAttributeUtil.REPEAT_PASSWORD_ERROR;
import static utils.ModelAttributeUtil.USERNAME_ERROR;

@UtilityClass
public class SignUpTestDataProvider {

    public static Stream<Arguments> getSignUpInvalidData() {
        return Stream.of(
                Arguments.of(
                        "user", "123", "456",
                        List.of(USERNAME, PASSWORD, REPEAT_PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR, REPEAT_PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "user name", "12 345", "456",
                        List.of(USERNAME, PASSWORD, REPEAT_PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR, REPEAT_PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "userГname", "12Г345", "456",
                        List.of(USERNAME, PASSWORD, REPEAT_PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR, REPEAT_PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "", "", "",
                        List.of(USERNAME, PASSWORD, REPEAT_PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR, REPEAT_PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "moreThanTwentyCharacters", "moreThanTwentyCharacters", "moreThanTwentyCharacters",
                        List.of(USERNAME, PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "correct1_", "pass", "password",
                        List.of(PASSWORD, REPEAT_PASSWORD).toArray(new String[0]),
                        List.of(PASSWORD_ERROR, REPEAT_PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "use1r)", "correct", "correct",
                        List.of(USERNAME).toArray(new String[0]),
                        List.of(USERNAME_ERROR).toArray(new String[0])),

                Arguments.of(
                        "correct", "correct", "incorrect",
                        List.of(REPEAT_PASSWORD).toArray(new String[0]),
                        List.of(REPEAT_PASSWORD_ERROR).toArray(new String[0]))
        );
    }
}
