package net.examplemod.neoforge;

import net.examplemod.ExampleMod;
import net.neoforged.fml.common.Mod;

@Mod(ExampleMod.MOD_ID)
public class ExampleModForge {
    public ExampleModForge() {
        ExampleMod.init();
    }
}
