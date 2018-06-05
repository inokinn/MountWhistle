package net.inokinn.mountwhistle;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = MountWhistle.MODID, name = MountWhistle.NAME, version = MountWhistle.VERSION)
public class MountWhistle {
    public static final String MODID = "mountwhistle";
    public static final String NAME = "Mount Whistle";
    public static final String VERSION = "1.0.3";
    public static final String PAKETNAME = "MountWhistle";

    public static Item mountWhistle;

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
    	mountWhistle = new Whistle()
    			.setCreativeTab(CreativeTabs.TRANSPORTATION)
    			.setUnlocalizedName("mountwhistle")
    			.setRegistryName("mountwhistle")
    			.setMaxStackSize(1);
        event.getRegistry().register(mountWhistle);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(mountWhistle, 0, new ModelResourceLocation(new ResourceLocation("mountwhistle", "mountwhistle"), "inventory"));
    }
}
