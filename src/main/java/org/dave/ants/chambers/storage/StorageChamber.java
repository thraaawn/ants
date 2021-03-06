package org.dave.ants.chambers.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import org.dave.ants.Ants;
import org.dave.ants.api.chambers.AntChamber;
import org.dave.ants.api.gui.widgets.WidgetPanel;
import org.dave.ants.api.gui.ants.WidgetStatsTable;
import org.dave.ants.api.properties.calculated.FoodCapacity;
import org.dave.ants.chambers.UpgradeableChamber;
import org.dave.ants.config.GeneralAntHillConfig;
import org.dave.ants.hills.HillData;
import org.dave.ants.util.SmartNumberFormatter;

import java.util.List;

@AntChamber
public class StorageChamber extends UpgradeableChamber {
    public static final String CONFIG_CATEGORY = "Storage Chamber";
    public static double priceToBuy = 10.0d;

    // Upgrade-Options
    public static int maxUpgrades = 100;
    public static double baseUpgradePrice = 10.0d;
    public static double upgradePriceMultiplier = 1.09d;

    // Tier-Scaling options
    public static double baseFoodCrates = 1000;


    @Override
    public String description() {
        return I18n.format("gui.ants.chamber.storage_chamber.description");
    }

    @Override
    public boolean shouldListInStore() {
        return true;
    }


    @Override
    public void applyHillModification(HillData data, int chamberTier) {
        double baseValue = baseFoodCrates * GeneralAntHillConfig.defaultTierIncomeRate.get(chamberTier);
        double totalBeds = baseValue * Math.pow(GeneralAntHillConfig.defaultUpgradeMultiplier, upgrades);

        data.modifyPropertyValue(FoodCapacity.class, fc -> fc + totalBeds);
    }


    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public WidgetPanel createGuiPanel(int tier) {
        WidgetPanel storagePanel = new WidgetPanel();
        storagePanel.add(getUpgradeBar());

        WidgetStatsTable statsTable = new WidgetStatsTable();
        statsTable.setX(1);
        statsTable.setY(25);


        double baseValue = baseFoodCrates * GeneralAntHillConfig.defaultTierIncomeRate.get(tier);
        double totalCrates = baseValue * Math.pow(GeneralAntHillConfig.defaultUpgradeMultiplier, upgrades);
        double percentOfTotal = totalCrates / Ants.clientHillData.getPropertyValue(FoodCapacity.class);

        statsTable.addStatistic(
                I18n.format("gui.ants.chamber.storage_chamber.space"),
                SmartNumberFormatter.formatNumber(totalCrates)
        );

        statsTable.addStatistic(
                I18n.format("gui.ants.chamber.common.stats.percent_of_total"),
                String.format("%.1f%%", percentOfTotal * 100)
        );

        storagePanel.add(statsTable);

        return storagePanel;
    }


    @Override
    public List<IBlockState> getTierList() {
        return GeneralAntHillConfig.defaultTierList;
    }

    @Override
    public double tierCost(int tier, IBlockState state) {
        return GeneralAntHillConfig.defaultTierCost.get(tier) * 2;
    }


    @Override
    public double getMaxUpgrades() {
        return maxUpgrades;
    }

    @Override
    public double getBaseUpgradePrice() {
        return baseUpgradePrice;
    }

    @Override
    public double getUpgradePriceMultiplier() {
        return upgradePriceMultiplier;
    }


    @Override
    public int antFillingColorTint() {
        return 0xd9ac85;
    }
}
