package com.verity.mod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(VerityVoiceMod.MOD_ID)
public class VerityVoiceMod {

    public static final String MOD_ID = "verityvoice";

    public VerityVoiceMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::clientSetup);
        KeyBindings.register(modEventBus);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        // Client setup complete
    }
}
