package org.dave.ants.api.gui.ants;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.dave.ants.Ants;
import org.dave.ants.api.chambers.IAntChamber;
import org.dave.ants.api.gui.widgets.WidgetButton;
import org.dave.ants.gui.GUIHelper;

public class WidgetBuyChamberButton extends WidgetButton {
    private IAntChamber chamber;

    private static String getTranslatedChamberName(IAntChamber chamber) {
        return I18n.format(String.format("tile.ants.hill_chamber.%s.name", chamber.getClass().getSimpleName().toLowerCase()));
    }

    public WidgetBuyChamberButton(IAntChamber chamber) {
        super(getTranslatedChamberName(chamber));
        this.chamber = chamber;

        this.setWidth(50);
        this.setHeight(40);
        this.addTooltipLine(chamber.description());
    }

    @Override
    protected void drawButtonContent(GuiScreen screen, FontRenderer renderer) {
        super.drawButtonContent(screen, renderer);

        screen.mc.getRenderItem().renderItemAndEffectIntoGUI(Ants.chamberTypes.createItemStackForChamberType(this.chamber.getClass(), 0), (width - 16) / 2, 3 );
    }

    @Override
    protected void drawString(GuiScreen screen, FontRenderer renderer) {
        int color = 0xEEEEEE;
        GUIHelper.drawSplitStringCentered(unlocalizedLabel, screen, 2, 18, width-4, color);
    }
}
