package xyz.fragbots.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import xyz.fragbots.FragBots;
import xyz.fragbots.api.FragBotsApi;
import xyz.fragbots.errors.ApiReturnError;
import xyz.fragbots.gsonobjs.BotGsonObj;

import java.util.*;

/*

* Command that parties a random fragbot

 */

public class FragRunCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "fragrun";
    }

    @Override
    public List<String> getCommandAliases() {
        //Thanks byBackFish for showing me that this existed and I didnt have to create a new arraylist lol
        return Arrays.asList("fr","partbots");
    }
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;
        //TODO Check if player has already partied a bot
        player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"Making requests to api to find all online fragbots"));
        List<BotGsonObj> botList;
        try{
            //Gets list of all current bots from our api checking for the ApiReturnError if our api is down
            botList = FragBotsApi.getBotList();
        }catch(ApiReturnError e){
            player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cThere was an error while trying to communicate with our api please contact prince"));
            return;
        }
        List<String> validBotNames = new ArrayList<>();
        //Sorts through all the bots to select all of the valid (online) ones
        for(BotGsonObj bot : botList){
            if(bot.isOnline()){
                validBotNames.add(bot.getUsername());
            }

        }
        //Makes sure there aren't any online bots
        if(validBotNames.size()==0){
            player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cNo available bots!"));
            return;
        }
        Random rand = new Random();
        //Chooses a random fragbot from array
        //TODO Work on way to track fragbot usage so it can party the least used bot in the last minute
        String randomBot = validBotNames.get(rand.nextInt(validBotNames.size()));
        player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§aPartying fragbot with ign: "+randomBot+"!"));
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/p "+randomBot);
        //TODO setup manager to hide fragbot messages and detect when bot has been partied by the user
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
