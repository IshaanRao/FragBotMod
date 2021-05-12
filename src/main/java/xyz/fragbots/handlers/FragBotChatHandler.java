package xyz.fragbots.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.fragbots.FragBots;
import xyz.fragbots.drawables.NotificationDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FragBotChatHandler {
    private List<Pattern> deleteQueue;
    private boolean deleteDisband=false;
    private String currentBot;
    private boolean running;
    private static FragBotChatHandler instance;
    public static FragBotChatHandler getInstance() {
        if(instance == null) instance = new FragBotChatHandler();
        return instance;
    }

    public FragBotChatHandler(){
        this.deleteQueue = new ArrayList<>();
    }

    //Event gets triggered whenever something is added to the players chat
    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        if(deleteQueue.isEmpty()){
            return;
        }
        String message = event.message.getUnformattedText();
        //Checks if the message is the next thing in the queue of items to be deleted
        if(deleteQueue.get(0).matcher(message).matches()){
            event.setCanceled(true);
            ChatEventType type = getType(deleteQueue.get(0));
            //Check if it is a special event and if it is send a custom message to inform the user
            if(type == ChatEventType.PARTY){
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(FragBots.PREFIX+currentBot+" has been partied!"));
            }else if(type == ChatEventType.JOIN){
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(FragBots.PREFIX+currentBot+" has joined the party!"));
                if(FragBots.getConfigHandler().isShowAlerts()) {
                    RenderHandler.getInstance().addItem(new NotificationDrawable("§a§l" + currentBot + " has joined the party!"), 3000);
                }
            }else if(type == ChatEventType.LEAVE){
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(FragBots.PREFIX+currentBot+" left the party!"));
            }
            //Remove the message from delete queue since we processed it
            deleteQueue.remove(0);

            //If there is nothing left then handle it
            if(deleteQueue.isEmpty()){
                this.running = false;
                this.currentBot = "";
            }
        }else if(running && message.contains("discord.gg/fragbots") && message.contains(currentBot)){
            event.setCanceled(true);
            Minecraft.getMinecraft().thePlayer.addChatMessage(ForgeHooks.newChatWithLinks(FragBots.PREFIX + "Join our discord at:§9 https://discord.gg/fragbots"));
        }
    }

    public void clear() {
        deleteQueue.clear();
        this.running = false;
        this.currentBot = "";
    }

    public boolean isRunning() {
        return running;
    }

    public void addBot(String botName){
        if(!running){
            //Party Join Regex
            this.deleteQueue.add(Pattern.compile("-----------------------------"));
            this.deleteQueue.add(Pattern.compile(".* "+botName+" to the party! They have 60 seconds to accept."));
            this.deleteQueue.add(Pattern.compile("-----------------------------"));

            //Bot Join Regex
            this.deleteQueue.add(Pattern.compile("-----------------------------"));
            this.deleteQueue.add(Pattern.compile(".*"+botName+" joined the party."));
            this.deleteQueue.add(Pattern.compile("-----------------------------"));

            //Bot Leave Regex
            this.deleteQueue.add(Pattern.compile("-----------------------------"));
            this.deleteQueue.add(Pattern.compile(".*"+botName+" has left the party."));
            this.deleteQueue.add(Pattern.compile("-----------------------------"));
            this.currentBot=botName;
            this.running=true;
        }
    }

    public ChatEventType getType(Pattern pattern){
        if (pattern.pattern().equals(".* " + currentBot + " to the party! They have 60 seconds to accept.")){
            return ChatEventType.PARTY;
        }else if(pattern.pattern().equals(".*"+ currentBot + " joined the party.")){
            return ChatEventType.JOIN;
        }else if(pattern.pattern().equals(".*"+ currentBot +" has left the party.")){
            return ChatEventType.LEAVE;
        }
        return null;

    }
    private enum ChatEventType{
        PARTY,JOIN,LEAVE,DISBAND
    }
}
