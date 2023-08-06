package com.lingohammer.aws.auth.util;

import org.junit.jupiter.api.Assertions;

public class StringAssertions {

    private StringAssertions() {
    }

    public static void assertStringsEqualIgnoringLineEndings(String expected, String actual) {
        // Remove CR and LF characters from the expected and actual strings
        String expectedWithoutLineEndings = removeLineEndings(expected);
        String actualWithoutLineEndings = removeLineEndings(actual);

        // Assert that the modified strings are equal
        Assertions.assertEquals(expectedWithoutLineEndings, actualWithoutLineEndings);
    }

    private static String removeLineEndings(String input) {
        // Replace all CR and LF characters with an empty string
        return input.replaceAll("\\r|\\n", "");
    }
}
