package org.quurz.foomp.automata.localisation;

import java.util.Locale;
import java.util.ResourceBundle;

public class AutomataMessages {

    private static final ResourceBundle RESOURCE_BUNDLE
        = ResourceBundle.getBundle("AutomataMessages", Locale.of("en", "GB"));

    public static String emptyStateSet() {
        return RESOURCE_BUNDLE.getString("EMPTY_STATE_SET");
    }

    public static String emptyInputAlphabet() {
        return RESOURCE_BUNDLE.getString("EMPTY_INPUT_ALPHABET");
    }

    public static String emptyOutputAlphabet() {
        return RESOURCE_BUNDLE.getString("EMPTY_OUTPUT_ALPHABET");
    }

    public static String endStatesNotATrueSubsetOfStates() {
        return RESOURCE_BUNDLE.getString("END_STATES_TRUE_NO_SUBSET_OF_STATES");
    }

    public static String unknownInputToken(final Object inputToken) {
        return String.format(
            RESOURCE_BUNDLE.getString("UNKNOWN_INPUT_TOKEN"),
            inputToken
        );
    }

    public static String unknownState(final Object state) {
        return String.format(
            RESOURCE_BUNDLE.getString("UNKNOWN_STATE"),
            state
        );
    }

    public static String emptyTransitionMap() {
        return RESOURCE_BUNDLE.getString("EMPTY_TRANSITION_MAP");
    }

}
