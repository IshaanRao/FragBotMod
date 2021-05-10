package xyz.fragbots;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.fragbots.Commands.botmanger;
import xyz.fragbots.Commands.fragrun;

@Mod(modid = FragBots.MODID, version = FragBots.VERSION, clientSideOnly = true, name = "Frag Bots")
public class FragBots {
    public static final String MODID = "FragBots";
    public static final String VERSION = "1.0";
    public static final String PREFIX = "§6§lFragBots§8 »§a ";
    boolean hasJustLeft = false;
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new fragrun());
        System.out.println("Fragbots has started up");
    }
    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event) {
        if(botmanger.getInstance().isHasbot()){
            String msg = event.message.getUnformattedText();
            if(event.message.getUnformattedText().contains("discord.gg/fragbots")) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX + "Join our discord at: §9discord.gg/fragbots"));
                event.setCanceled(true);
                return;
            }
            if(msg.contains("-----------------------------")){
                if(botmanger.getInstance().getStage()==0){
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX+"Successfully partied "+botmanger.getInstance().getIgn()+"!"));
                }
                botmanger.getInstance().addStage();
                event.setCanceled(true);
            }else if(msg.contains("to the party!")&&msg.contains(botmanger.getInstance().getIgn())){
                botmanger.getInstance().addStage();
                event.setCanceled(true);
            }else if(msg.contains("joined the party")&&msg.contains(botmanger.getInstance().getIgn())){
                botmanger.getInstance().addStage();
                event.setCanceled(true);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX+botmanger.getInstance().getIgn()+" has joined the party!"));
            } else if(msg.contains("left the party")&&msg.contains(botmanger.getInstance().getIgn())){
                botmanger.getInstance().addStage();
                event.setCanceled(true);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX+botmanger.getInstance().getIgn()+" has left the party!"));
                hasJustLeft = true;
                new Thread(()->{
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        hasJustLeft = false;
                    }
                    hasJustLeft = false;
                }).start();
            }else if(msg.contains("has expired")&&msg.contains(botmanger.getInstance().getIgn())){
                botmanger.getInstance().addStage();
                event.setCanceled(true);
            }
        }else if(hasJustLeft){
            String msg = event.message.getUnformattedText();
            if(msg.contains("-----------------------------")){
                event.setCanceled(true);
            }else if(msg.contains("The party was disbanded because all invites expired and the party was empty")){
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX+"The party has been disbanded"));
                event.setCanceled(true);
            }
        }
    }
    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
    }
}
