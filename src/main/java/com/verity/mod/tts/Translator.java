package com.verity.mod.tts;

import com.verity.mod.VerityVoiceMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translator {

    private static final Logger LOGGER = LogManager.getLogger(VerityVoiceMod.MOD_ID);

    /**
     * Returns Turkish translation of the text if it looks like English.
     * If already Turkish (contains Turkish chars), returns as-is.
     * Uses MyMemory free API — no key needed.
     */
    public static String toTurkishIfEnglish(String text) {
        if (looksLikeTurkish(text)) {
            return text;
        }
        try {
            String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String urlStr = "https://api.mymemory.translated.net/get?q=" + encoded + "&langpair=en|tr";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            conn.setRequestProperty("User-Agent", "VerityVoiceMod/1.0");

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                // Parse "translatedText":"..." from JSON without a library
                int start = body.indexOf("\"translatedText\":\"");
                if (start != -1) {
                    start += 18;
                    int end = body.indexOf("\"", start);
                    if (end != -1) {
                        String translated = body.substring(start, end);
                        // Unescape basic JSON escapes
                        translated = translated.replace("\\n", " ").replace("\\\"", "\"").replace("\\\\", "\\");
                        LOGGER.debug("[VerityVoice] Translated: {} -> {}", text, translated);
                        return translated;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warn("[VerityVoice] Translation failed: {}", e.getMessage());
        }
        // Fallback: return original
        return text;
    }

    /**
     * Heuristic: if text has any Turkish-specific characters it's likely Turkish.
     */
    private static boolean looksLikeTurkish(String text) {
        for (char c : text.toCharArray()) {
            if (c == 'ğ' || c == 'Ğ' || c == 'ı' || c == 'İ' ||
                c == 'ş' || c == 'Ş' || c == 'ç' || c == 'Ç' ||
                c == 'ö' || c == 'Ö' || c == 'ü' || c == 'Ü') {
                return true;
            }
        }
        return false;
    }
}
