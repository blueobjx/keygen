import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.KeyGenerator;

public class KeyGen {

    private static String encodeBytesToString(final byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String createKey() {
        try {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return encodeBytesToString(keyGenerator.generateKey().getEncoded());
        }
        catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static StringBuffer replaceAll(final String templateText, final Pattern pattern, final Function<Matcher, String> replacer) {
        final Matcher matcher = pattern.matcher(templateText);
        final StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(result, replacer.apply(matcher));
        }

        matcher.appendTail(result);
        return result;
    }

    private static void processFile(final String path) throws IOException {
        Files.lines(Paths.get(path)).forEach(it -> {
            System.out.println(replaceAll(it, Pattern.compile("\\{\\{INSERT_GENERATED_MSGKEY\\}\\}"), matcher -> createKey()));
        });
    }

    public static void main(final String[] args) throws IOException {
        if (args.length == 0) {
            System.out.printf("%s%n", createKey());
        }
        else {
            processFile(args[0]);
        }
    }

}
