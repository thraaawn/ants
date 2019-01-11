package org.dave.ants.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.dave.ants.api.hill.IHillProperty;
import org.dave.ants.gui.ClientChamberGuiCache;
import org.dave.ants.util.serialization.FieldHandlers;

import java.util.Map;

public class ChamberDataMessageHandler implements IMessageHandler<ChamberDataMessage, ChamberDataMessage> {
    @Override
    public ChamberDataMessage onMessage(ChamberDataMessage message, MessageContext ctx) {
        ClientChamberGuiCache.chamberData = message.chamberData;
        ClientChamberGuiCache.chamberType = message.type;
        ClientChamberGuiCache.chamberPos = message.pos;

        Map<Class<? extends IHillProperty>, IHillProperty> properties = (Map<Class<? extends IHillProperty>, IHillProperty>) FieldHandlers.getNBTHandler(ClientChamberGuiCache.properties.getClass()).getLeft().read("properties", message.properties);
        Map<Class<? extends IHillProperty>, IHillProperty> calculated = (Map<Class<? extends IHillProperty>, IHillProperty>) FieldHandlers.getNBTHandler(ClientChamberGuiCache.properties.getClass()).getLeft().read("calculated", message.properties);
        ClientChamberGuiCache.properties.clear();
        ClientChamberGuiCache.properties.putAll(properties);
        ClientChamberGuiCache.properties.putAll(calculated);

        ClientChamberGuiCache.lastMessageReceived = System.currentTimeMillis();
        return null;
    }
}
