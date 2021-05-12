package xyz.fragbots.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.fragbots.handlers.FragBotChatHandler;

@SideOnly(Side.CLIENT)
public class ServerDisconnectEvent {

    @SubscribeEvent
    public void onClientDisconnectionFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        FragBotChatHandler.getInstance().clear();
    }
}
