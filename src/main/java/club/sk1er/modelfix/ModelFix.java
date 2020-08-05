package club.sk1er.modelfix;

import club.sk1er.modcore.ModCoreInstaller;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = ModelFix.MOD_ID,
        name = ModelFix.MOD_NAME,
        version = ModelFix.VERSION
)
public class ModelFix {
    public static final String MOD_ID = "modelfix";
    public static final String MOD_NAME = "ModelFix";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);
    }
}
