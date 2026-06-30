package com.verity.mod;

import com.verity.mod.tts.TextToSpeech;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.InputEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EventBusSubscriber(modid = VerityVoiceMod.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final Pattern VERITY_PATTERN = Pattern.compile(
            "^(?:\\[.*?\\]\\s*)?<\\s*Verity\\s*>\\s*(.+)$"
    );

    private static String lastVerityMessage = null;

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        String raw = event.getMessage().getString();
        Matcher m = VERITY_PATTERN.matcher(raw.trim());
        if (m.matches()) {
            lastVerityMessage = m.group(1).trim();
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;

        if (KeyBindings.READ_VERITY_KEY.consumeClick()) {
            if (TextToSpeech.isSpeaking()) {
                // Şu an konuşuyor → durdur
                TextToSpeech.stop();
                mc.gui.getChat().addMessage(
                        Component.literal("§b[Verity Voice] §cDurduruldu.")
                );
            } else if (!TextToSpeech.isEnabled()) {
                // Kapalıydı → tekrar aç
                TextToSpeech.toggle();
                mc.gui.getChat().addMessage(
                        Component.literal("§b[Verity Voice] §aAçıldı.")
                );
            } else {
                // Normal durum: Verity mesajı varsa oku
                if (lastVerityMessage != null && !lastVerityMessage.isEmpty()) {
                    mc.gui.getChat().addMessage(
                            Component.literal("§b[Verity Voice] §fOkunuyor (çeviriliyor...): §e" + lastVerityMessage)
                    );
                    TextToSpeech.speak(lastVerityMessage);
                } else {
                    mc.gui.getChat().addMessage(
                            Component.literal("§b[Verity Voice] §cSohbette henüz Verity mesajı yok.")
                    );
                }
            }
        }
    }
}
