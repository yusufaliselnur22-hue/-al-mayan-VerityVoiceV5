package com.verity.mod;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final String KEY_CATEGORY = "key.category.verityvoice";
    public static final String KEY_READ_VERITY = "key.verityvoice.read";

    public static KeyMapping READ_VERITY_KEY;

    public static void register(IEventBus modEventBus) {
        READ_VERITY_KEY = new KeyMapping(
                KEY_READ_VERITY,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                KEY_CATEGORY
        );
        modEventBus.addListener(KeyBindings::onRegisterKeyMappings);
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(READ_VERITY_KEY);
    }
}
