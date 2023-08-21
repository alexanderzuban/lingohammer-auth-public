package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lingohammer.aws.auth.process.login.LoginRequest;
import com.lingohammer.aws.auth.process.register.RegistrationConfirmRequest;
import com.lingohammer.aws.auth.process.register.RegistrationRequest;
import com.lingohammer.aws.auth.process.restore.RestorePasswordConfirmRequest;
import com.lingohammer.aws.auth.process.restore.RestorePasswordRequest;
import com.lingohammer.aws.auth.util.ObjectMapperHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMapperHelperTest {

    @ParameterizedTest
    @MethodSource("parametersForTestModelsWithEmail")
    void testModelsWithEmail(Object target, Function<Object, String> modelFieldGetter) throws JsonProcessingException {
        var valueBefore = modelFieldGetter.apply(target);

        //check value has uppercase characters
        assertNotEquals(valueBefore, valueBefore.toLowerCase());

        var serialized = ObjectMapperHelper.getObjectMapper().writeValueAsString(target);
        assertTrue(serialized.contains(valueBefore));

        var loaded = ObjectMapperHelper.getObjectMapper().readValue(serialized, target.getClass());
        var valueAfter = modelFieldGetter.apply(loaded);

        assertEquals(valueBefore.toLowerCase(), valueAfter);
    }

    private static Stream<Arguments> parametersForTestModelsWithEmail() {
        return Stream.of(
                //wrong password
                Arguments.of(new RegistrationConfirmRequest("UserEmail@email.com", null, null), (Function<Object, String>) (e) -> ((RegistrationConfirmRequest) e).getEmail()),
                Arguments.of(new LoginRequest("UserEmail@email.com", null), (Function<Object, String>) (e) -> ((LoginRequest) e).getEmail()),
                Arguments.of(new RegistrationRequest("UserEmail@email.com", null, null, null), (Function<Object, String>) (e) -> ((RegistrationRequest) e).getEmail()),
                Arguments.of(new RestorePasswordConfirmRequest("UserEmail@email.com", null, null), (Function<Object, String>) (e) -> ((RestorePasswordConfirmRequest) e).getEmail()),
                Arguments.of(new RestorePasswordRequest("UserEmail@email.com"), (Function<Object, String>) (e) -> ((RestorePasswordRequest) e).getEmail())
        );
    }
}
