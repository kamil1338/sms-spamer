package com.google.kamil1338.smsspamer.utils;

/**
 * <p>
 *     Created by Kamil on 2016-07-06.
 *
 *     Operacje na znakach.
 * </p>
 */
public class CharactersUtil {

    private static final String polishCharacters = "ąćęłńóśźż";
    private static final String asciCharacters = "acelnoszz";

    /**
     * <p>
     *     Zamiana polskich znaków na ASCII.
     *
     *     @param polishText Tekst, który może zawierać polskie znaki.
     *     @return Zwraca tekst bez polskich znaków.
     * </p>
     * */
    public static String transformToAscii(String polishText) {
        char[] iterableCharacters = polishText.toCharArray();
        for (int i = 0; i < iterableCharacters.length; i++) {
            for (int j = 0; j < polishCharacters.length(); j++) {
                if (iterableCharacters[i] == polishCharacters.charAt(j)) {
                    iterableCharacters[i] = asciCharacters.charAt(j);
                }
            }
        }
        return String.valueOf(iterableCharacters);
    }
}
