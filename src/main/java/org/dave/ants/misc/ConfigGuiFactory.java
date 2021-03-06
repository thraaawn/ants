package org.dave.ants.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import org.dave.ants.Ants;
import org.dave.ants.config.ConfigurationHandler;

import java.util.Set;

public class ConfigGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ConfigScreen(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public class ConfigScreen extends GuiConfig {

        public ConfigScreen(GuiScreen parentScreen) {
            super(parentScreen, ConfigurationHandler.getConfigElements(), Ants.MODID, false, false, "Ants!");
        }
    }
}
