package org.dave.ants.chambers;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.dave.ants.actions.ActionRegistry;
import org.dave.ants.actions.AddWorker;
import org.dave.ants.api.chambers.IChamberAction;
import org.dave.ants.api.gui.event.MouseClickEvent;
import org.dave.ants.api.gui.event.WidgetEventResult;
import org.dave.ants.api.gui.widgets.Widget;
import org.dave.ants.api.gui.widgets.WidgetButton;
import org.dave.ants.api.gui.widgets.WidgetPanel;
import org.dave.ants.api.gui.widgets.WidgetProgressBar;
import org.dave.ants.api.gui.widgets.composed.WidgetLabeledProgressBar;
import org.dave.ants.api.properties.stored.TotalAnts;
import org.dave.ants.api.serialization.Store;
import org.dave.ants.hills.HillData;
import org.dave.ants.util.SmartNumberFormatter;

import static org.dave.ants.gui.ClientChamberGuiCache.getPropertyValue;

public abstract class WorkableChamber extends UpgradeableChamber {
    @Store
    public int workers = 0;

    /**
     * How many workers this chamber can support. Must incorporate upgrade level,
     * if the chamber is indeed upgradable.
     *
     * @return maximum number of workers
     */
    public abstract int getMaxWorkers();

    /**
     * How many ants must be "sacrificed" to add another worker. Should increase
     * with the amount of workers already in this chamber.
     *
     * @param count How many workers should be bought.
     *
     * @return ant "price" for a single worker
     */
    public abstract double priceForWorkers(int count);

    private WidgetButton createWorkerButton(String label, int count) {
        WidgetButton addWorkerButton = new WidgetButton(label);
        addWorkerButton.setHeight(20);
        addWorkerButton.setY(24);
        addWorkerButton.addListener(MouseClickEvent.class, (event, widget) -> {
            ActionRegistry.fireChamberAction(new AddWorker(count));
            return WidgetEventResult.HANDLED;
        });

        double totalAnts = getPropertyValue(TotalAnts.class);
        double price = priceForWorkers(count);

        addWorkerButton.addTooltipLine(I18n.format("gui.ants.hill_chamber.infos.price", SmartNumberFormatter.formatNumber(price)));

        if(price > totalAnts) {
            addWorkerButton.setEnabled(false);
            addWorkerButton.addTooltipLine(I18n.format("gui.ants.hill_chamber.infos.need_more_ants"));
        }

        if(workers + count > getMaxWorkers()) {
            addWorkerButton.setEnabled(false);
            addWorkerButton.addTooltipLine(I18n.format("gui.ants.hill_chamber.infos.out_of_room"));
            if(upgrades < getMaxUpgrades()) {
                addWorkerButton.addTooltipLine(I18n.format("gui.ants.hill_chamber.infos.upgrade_first"));
            }
        }

        return addWorkerButton;
    }

    public Widget getWorkerBar() {
        WidgetPanel panel = new WidgetPanel();
        panel.setId("WorkerPanel");
        panel.setWidth(165);
        panel.setHeight(45);

        WidgetLabeledProgressBar workerBar = new WidgetLabeledProgressBar(I18n.format("gui.ants.workable_chamber.workers.label"), 0, getMaxWorkers(), (double)workers);
        workerBar.setWidth(165);
        workerBar.getProgressBar().setForegroundColor(0xFFDDAA11);
        workerBar.getProgressBar().setDisplayMode(WidgetProgressBar.EnumDisplayMode.VALUE_AND_PERCENTAGE);

        panel.add(workerBar);

        int xOffset = 0;
        WidgetButton plusOneButton = createWorkerButton("+1", 1);
        plusOneButton.setWidth(20);
        plusOneButton.setX(xOffset);
        panel.add(plusOneButton);
        xOffset += 24;

        WidgetButton plusFiveButton = createWorkerButton("+5", 5);
        plusFiveButton.setWidth(20);
        plusFiveButton.setX(xOffset);
        panel.add(plusFiveButton);
        xOffset += 24;

        WidgetButton plusTenPButton = createWorkerButton("+10", 10);
        plusTenPButton.setWidth(28);
        plusTenPButton.setX(xOffset);
        panel.add(plusTenPButton);
        xOffset += 32;

        WidgetButton plusFiftyPButton = createWorkerButton("x1.5", Math.max(1, (int)(workers * 0.5d)));
        plusFiftyPButton.setWidth(28);
        plusFiftyPButton.setX(xOffset);
        panel.add(plusFiftyPButton);
        xOffset += 32;

        WidgetButton doubleButton = createWorkerButton("x2", Math.max(1, workers));
        doubleButton.setWidth(21);
        doubleButton.setX(xOffset);
        panel.add(doubleButton);
        xOffset += 25;

        WidgetButton fillButton = createWorkerButton(I18n.format("gui.ants.workable_chamber.workers.fill"), getMaxWorkers()-workers);
        fillButton.setWidth(28);
        fillButton.setX(xOffset);
        panel.add(fillButton);

        panel.addTooltipLine(I18n.format("gui.ants.chamber.common.worker.tooltip"));
        return panel;
    }

    @Override
    public void onChamberAction(EntityPlayer player, IChamberAction action, HillData hillData) {
        super.onChamberAction(player, action, hillData);

        if(action instanceof AddWorker) {
            AddWorker addWorkerAction = (AddWorker)action;

            double price = priceForWorkers(addWorkerAction.count);
            if(price > hillData.getPropertyValue(TotalAnts.class)) {
                return;
            }

            if(workers + addWorkerAction.count > getMaxWorkers()) {
                return;
            }

            hillData.modifyPropertyValue(TotalAnts.class, ants -> ants - price);
            workers += addWorkerAction.count;
            this.markDirty();
            hillData.updateHillStatistics();
        }

    }
}
