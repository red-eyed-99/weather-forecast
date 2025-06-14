package utils;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.params.provider.Arguments;
import java.util.List;
import java.util.stream.Stream;

import static dto.SignUpUserDTO.Fields.PASSWORD;
import static dto.SignUpUserDTO.Fields.USERNAME;
import static utils.ModelAttributeUtil.PASSWORD_ERROR;
import static utils.ModelAttributeUtil.USERNAME_ERROR;

@UtilityClass
public class SignInTestDataProvider {

    public static Stream<Arguments> getSignInInvalidData() {
        return Stream.of(
                Arguments.of(
                        "user", "123",
                        List.of(USERNAME, PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "user name", "12 345",
                        List.of(USERNAME, PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "userГname", "12Г345",
                        List.of(USERNAME, PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "", "",
                        List.of(USERNAME, PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "moreThanTwentyCharacters", "moreThanTwentyCharacters",
                        List.of(USERNAME, PASSWORD).toArray(new String[0]),
                        List.of(USERNAME_ERROR, PASSWORD_ERROR).toArray(new String[0])),

                Arguments.of(
                        "use1r)", "correct",
                        List.of(USERNAME).toArray(new String[0]),
                        List.of(USERNAME_ERROR).toArray(new String[0])),

                Arguments.of(
                        "correct", "pass  word",
                        List.of(PASSWORD).toArray(new String[0]),
                        List.of(PASSWORD_ERROR).toArray(new String[0]))
        );
    }
}
