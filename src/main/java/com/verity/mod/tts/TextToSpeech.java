package com.verity.mod.tts;

import com.verity.mod.VerityVoiceMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TextToSpeech {

    private static final Logger LOGGER = LogManager.getLogger(VerityVoiceMod.MOD_ID);

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "VerityVoice-TTS");
        t.setDaemon(true);
        return t;
    });

    private static final AtomicBoolean speaking = new AtomicBoolean(false);
    private static final AtomicReference<Process> currentProcess = new AtomicReference<>(null);
    private static volatile boolean enabled = true;

    public static boolean toggle() {
        if (speaking.get()) {
            stop();
            enabled = false;
            return false;
        } else {
            enabled = !enabled;
            return enabled;
        }
    }

    public static void speak(String text) {
        if (!enabled) return;
        if (speaking.get()) stop();
        EXECUTOR.submit(() -> {
            speaking.set(true);
            try {
                String finalText = Translator.toTurkishIfEnglish(text);
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) speakWindows(finalText);
                else if (os.contains("mac")) speakMac(finalText);
                else speakLinux(finalText);
            } catch (Exception e) {
                String msg = e.getMessage();
                if (msg != null && !msg.contains("destroyed")) {
                    LOGGER.error("[VerityVoice] TTS error: {}", msg);
                }
            } finally {
                speaking.set(false);
                currentProcess.set(null);
            }
        });
    }

    public static void stop() {
        Process p = currentProcess.getAndSet(null);
        if (p != null && p.isAlive()) p.destroyForcibly();
        speaking.set(false);
    }

    public static boolean isSpeaking() { return speaking.get(); }
    public static boolean isEnabled()  { return enabled; }

    private static void speakWindows(String text) throws IOException, InterruptedException {
        String safe = text.replace("'", "''");
        String script =
            "Add-Type -AssemblyName System.Speech;" +
            "$s = New-Object System.Speech.Synthesis.SpeechSynthesizer;" +
            "$s.Rate = 5;" +
            "try{$s.SelectVoiceByHints([System.Speech.Synthesis.VoiceGender]::Female)}catch{};" +
            "$s.Speak('" + safe + "');";
        runProcess(List.of("powershell.exe", "-NoProfile", "-NonInteractive", "-Command", script));
    }

    private static void speakMac(String text) throws IOException, InterruptedException {
        runProcess(List.of("say", "-v", "Samantha", "-r", "230", text));
    }

    private static void speakLinux(String text) throws IOException, InterruptedException {
        runProcess(List.of("espeak-ng", "-p", "90", "-s", "175", text));
    }

    private static void runProcess(List<String> command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        currentProcess.set(p);
        p.waitFor();
    }

    public static void shutdown() {
        stop();
        EXECUTOR.shutdown();
    }
}
