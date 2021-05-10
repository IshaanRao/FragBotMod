package xyz.fragbots.Commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import xyz.fragbots.FragBots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class fragrun extends CommandBase {
    @Override
    public String getCommandName() {
        return "fragrun";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> list = new ArrayList<String>();
        list.add("fr");
        list.add("partybots");
        return list;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;
        if(botmanger.getInstance().isHasbot()){
            player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"You have partied a fragbot too recently."));
        }
        player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"Making requests to api to find all online fragbots"));
        JsonObject fragBots= Request.getResponse("https://api.fragbots.xyz/botslist/");
        if(fragBots==null){
            player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cThe api is not responding!"));
            return;
        }
        JsonArray bots = (JsonArray) fragBots.get("bots");
        List<String> validBots = new ArrayList<String>();
        for(JsonElement bot : bots){
            JsonObject obj = bot.getAsJsonObject();
            String uuid = null;
            for(Map.Entry<String, JsonElement> entry : obj.entrySet()){
                uuid = entry.getKey().toString();
            }
            if(uuid != null){
                if(obj.get(uuid).getAsJsonObject().get("online").getAsBoolean()){
                    if(!obj.get(uuid).getAsJsonObject().get("ign").getAsString().equals("Fekzy")) {
                        validBots.add(obj.get(uuid).getAsJsonObject().get("ign").getAsString());
                        continue;
                    }
                }
            }else{
                player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cSomething went wrong!"));
                return;
            }

        }
        if(validBots.size()==0){
            player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§aNo available bots!"));
            return;
        }
        Random rand = new Random();
        String randomBot = validBots.get(rand.nextInt(validBots.size()));
        botmanger.getInstance().addBot(randomBot);
        player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§aPartying fragbot with ign: "+randomBot+"!"));
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/p "+randomBot);
        return;
    }

    @Override
    public String getCommandUsage(ICommandSender arg0) {
        return "/" + getCommandName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}
