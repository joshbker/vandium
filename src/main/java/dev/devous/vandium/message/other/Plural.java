package dev.devous.vandium.message.other;

public class Plural {

    private Plural() {

    }

    /**
     * Make a word plural.
     *
     * @param string the word/phrase wanted to be plural
     * @return pluralised string
     */
    public static String makePlural(String string) {
        if (string.endsWith("s") || string.endsWith("z") || string.endsWith("x"))
            return string + "'";

        return string + "'s";
    }

}
