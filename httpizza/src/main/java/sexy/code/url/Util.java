/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sexy.code.url;

import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Junk drawer of utility methods.
 */
final class Util {

    /**
     * A cheap and type-safe constant for the UTF-8 Charset.
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private Util() {
    }

    /**
     * Increments {@code pos} until {@code input[pos]} is not ASCII whitespace. Stops at {@code
     * limit}.
     */
    public static int skipLeadingAsciiWhitespace(String input, int pos, int limit) {
        for (int i = pos; i < limit; i++) {
            switch (input.charAt(i)) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    continue;
                default:
                    return i;
            }
        }
        return limit;
    }

    /**
     * Decrements {@code limit} until {@code input[limit - 1]} is not ASCII whitespace. Stops at
     * {@code pos}.
     */
    public static int skipTrailingAsciiWhitespace(String input, int pos, int limit) {
        for (int i = limit - 1; i >= pos; i--) {
            switch (input.charAt(i)) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    continue;
                default:
                    return i + 1;
            }
        }
        return pos;
    }

    /**
     * Returns the index of the first character in {@code input} that contains a character in {@code
     * delimiters}. Returns limit if there is no such character.
     */
    public static int delimiterOffset(String input, int pos, int limit, String delimiters) {
        for (int i = pos; i < limit; i++) {
            if (delimiters.indexOf(input.charAt(i)) != -1) return i;
        }
        return limit;
    }

    /**
     * Returns the index of the first character in {@code input} that is {@code delimiter}. Returns
     * limit if there is no such character.
     */
    public static int delimiterOffset(String input, int pos, int limit, char delimiter) {
        for (int i = pos; i < limit; i++) {
            if (input.charAt(i) == delimiter) return i;
        }
        return limit;
    }

    /**
     * Performs IDN ToASCII encoding and canonicalize the result to lowercase. e.g. This converts
     * {@code ☃.net} to {@code xn--n3h.net}, and {@code WwW.GoOgLe.cOm} to {@code www.google.com}.
     * {@code null} will be returned if the input cannot be ToASCII encoded or if the result
     * contains unsupported ASCII characters.
     */
    public static String domainToAscii(String input) {
        try {
            String result = IDN.toASCII(input).toLowerCase(Locale.US);
            if (result.isEmpty()) return null;

            // Confirm that the IDN ToASCII result doesn't contain any illegal characters.
            if (containsInvalidHostnameAsciiCodes(result)) {
                return null;
            }
            // TODO: implement all label limits.
            return result;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static boolean containsInvalidHostnameAsciiCodes(String hostnameAscii) {
        for (int i = 0; i < hostnameAscii.length(); i++) {
            char c = hostnameAscii.charAt(i);
            // The WHATWG Host parsing rules accepts some character codes which are invalid by
            // definition for OkHttp's host header checks (and the WHATWG Host syntax definition). Here
            // we rule out characters that would cause problems in host headers.
            if (c <= '\u001f' || c >= '\u007f') {
                return true;
            }
            // Check for the characters mentioned in the WHATWG Host parsing spec:
            // U+0000, U+0009, U+000A, U+000D, U+0020, "#", "%", "/", ":", "?", "@", "[", "\", and "]"
            // (excluding the characters covered above).
            if (" #%/:?@[\\]".indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    public static byte[] utf8Bytes(int codePoint) {
        try {
            return new String(new int[]{codePoint}, 0, 1).getBytes(UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding is not supported, something is very wrong here!");
        }
    }

    public static void writeUtf8CodePoint(ByteBuffer byteBuffer, int codePoint) {
        if (codePoint < 128) {
            byteBuffer.put((byte) codePoint);
        } else if (codePoint < 2048) {
            byteBuffer.put((byte) (codePoint >> 6 | 192));
            byteBuffer.put((byte) (codePoint & 63 | 128));
        } else if (codePoint < 65536) {
            if (codePoint >= '\ud800' && codePoint <= '\udfff') {
                throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(codePoint));
            }

            byteBuffer.put((byte) (codePoint >> 12 | 224));
            byteBuffer.put((byte) (codePoint >> 6 & 63 | 128));
            byteBuffer.put((byte) (codePoint & 63 | 128));
        } else {
            if (codePoint > 1114111) {
                throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(codePoint));
            }

            byteBuffer.put((byte) (codePoint >> 18 | 240));
            byteBuffer.put((byte) (codePoint >> 12 & 63 | 128));
            byteBuffer.put((byte) (codePoint >> 6 & 63 | 128));
            byteBuffer.put((byte) (codePoint & 63 | 128));
        }
    }

    public static String readUtf8String(ByteBuffer byteBuffer) {
        byteBuffer.flip();
        return StandardCharsets.UTF_8.decode(byteBuffer).toString();
    }
}
