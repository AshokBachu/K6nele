package ee.ioc.phon.android.speak.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * TODO: read these from a flat file delivered by a file picker intent
 */
public class UtteranceRewriter {

    private final boolean mIsRewrite;

    // Rewrites applied the final result
    private static final Map<Pattern, String> REWRITES;

    // TODO: Rewrites applied to the whole text
    private static final Set<Triple> COMMANDS;

    static {
        Map<Pattern, String> rewrites = new HashMap<>();
        rewrites.put(Pattern.compile("[Nn]aeru ?näo ?sümbol"), ":-)");
        rewrites.put(Pattern.compile("[Rr]õõmsa ?näo ?sümbol"), ":-)");
        rewrites.put(Pattern.compile("[Nn]utu ?näo ?sümbol"), ":-(");
        rewrites.put(Pattern.compile("[Kk]urva ?näo ?sümbol"), ":-(");
        rewrites.put(Pattern.compile("[Vv]äga ?kurva ?näo ?sümbol"), ":'(");
        rewrites.put(Pattern.compile("[Ss]ulud algavad"), "(");
        rewrites.put(Pattern.compile("[Ss]ulud lõpevad"), ")");
        rewrites.put(Pattern.compile("[Ss]idekriips"), "-");
        rewrites.put(Pattern.compile("[Mm]õttekriips"), " - ");
        // TODO: provide the preferred position of the cursor
        rewrites.put(Pattern.compile("[Ee]-?kirja muster (1|üks)"), "Tere,\n\nKaarel");
        rewrites.put(Pattern.compile("[Ee]-?kirja muster (2|kaks)"), "Tere,\n\nParimat,\nKaarel");
        rewrites.put(Pattern.compile("Silla"), "Csilla");
        // Poor man's autopunctuation.
        // We optimize for precision by checking that the inserted comma is preceded by a word character,
        // thus it only works within the utterance.
        rewrites.put(Pattern.compile("(\\w) (aga|et|kuid|sest|siis|vaid)( |$)"), "$1, $2$3");
        // Relative clause pronouns
        rewrites.put(Pattern.compile("(\\w) (mi|ke)(s|da|lle)( |$)"), "$1, $2$3$4");
        rewrites.put(Pattern.compile("(\\w) (mille|kelle)(l|le|lt|s|sse|st|ni|na|ks|ga|ta)( |$)"), "$1, $2$3$4");
        REWRITES = Collections.unmodifiableMap(rewrites);
    }

    static class Triple {
        private Pattern mCommand;
        private String mIn;
        private String mOut;

        Triple(String command, String in, String out) {
            mCommand = Pattern.compile(command);
            mIn = in;
            mOut = out;
        }

        String matchCommand(String commandsAsString, CharSequence text) {
            Pattern pattern = Pattern.compile(mCommand.matcher(commandsAsString).replaceFirst(mIn));
            return pattern.matcher(text).replaceAll(mOut);
        }
    }

    static {
        Set<Triple> commands = new HashSet<>();
        commands.add(new Triple("[Kk]ustuta (.+)", "$1", ""));
        commands.add(new Triple("[Aa]senda (.+) fraasiga (.+)", "$1", "$2"));
        COMMANDS = Collections.unmodifiableSet(commands);
    }

    public UtteranceRewriter(boolean isRewrite) {
        mIsRewrite = isRewrite;

    }

    public CharSequence applyCommand(String commandsAsString, CharSequence text) {
        for (Triple triple : COMMANDS) {
            text = triple.matchCommand(commandsAsString, text);
        }
        return text;
    }

    private String rewrite(String str) {
        for (Map.Entry<Pattern, String> entry : REWRITES.entrySet()) {
            str = entry.getKey().matcher(str).replaceAll(entry.getValue());
        }
        return str;
    }


    public String rewrite(List<String> results) {
        if (results == null || results.size() < 1) {
            return "";
        }
        if (mIsRewrite) {
            return rewrite(results.get(0));
        }
        return results.get(0);
    }
}