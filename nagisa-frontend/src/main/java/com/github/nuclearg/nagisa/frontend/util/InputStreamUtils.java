package com.github.nuclearg.nagisa.frontend.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class InputStreamUtils {
    private InputStreamUtils() {
    }

    public static byte[] read(InputStream is) throws IOException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int b;
            while ((b = is.read()) != -1)
                os.write(b);
            return os.toByteArray();
        } finally {
            is.close();
        }
    }

    public static String read(InputStream is, Charset encoding) throws IOException {
        return new String(read(is), encoding);
    }
}
