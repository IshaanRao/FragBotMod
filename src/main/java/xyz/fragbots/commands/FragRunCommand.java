package xyz.fragbots.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import xyz.fragbots.FragBots;
import xyz.fragbots.api.FragBotsApi;
import xyz.fragbots.errors.ApiReturnError;
import xyz.fragbots.gsonobjs.BotGsonObj;
import xyz.fragbots.handlers.FragBotChatHandler;

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
        new Thread(()->{
            //Multithreading so client doesnt freeze on request
            EntityPlayer player = (EntityPlayer) sender;
            //Makes sure you havent already partied a bot
            //Check if there were any additional arguments
            if(args.length==1){
                if(args[0].equalsIgnoreCase("clearcache")){
                    FragBotChatHandler.getInstance().clear();
                    FragBotsApi.clearCache();
                    sender.addChatMessage(new ChatComponentText(FragBots.PREFIX+"Successfully cleared your cache!"));
                    return;
                }
                if(FragBotChatHandler.getInstance().isRunning()){
                    player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cYou have already partied a bot"));
                    return;
                }
                if(args[0].equalsIgnoreCase("whitelisted") || args[0].equalsIgnoreCase("wl")){
                    if(FragBotsApi.getWhitelistedIgn()==null|| FragBotsApi.getWhitelistedIgn().equals("")){
                        player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cYou do not have access to this bot!"));
                        return;
                    }
                    partyBot(FragBotsApi.getWhitelistedIgn());
                    return;
                }
                if(args[0].equalsIgnoreCase("active")||args[0].equalsIgnoreCase("ac")){
                    if(FragBotsApi.getActiveIgn()==null|| FragBotsApi.getActiveIgn().equals("")){
                        player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cYou do not have access to this bot!"));
                        return;
                    }
                    partyBot(FragBotsApi.getActiveIgn());
                    return;
                }
                if(args[0].equalsIgnoreCase("exclusive")||args[0].equalsIgnoreCase("ex")){
                    if(FragBotsApi.getExclusiveIgn()==null|| FragBotsApi.getExclusiveIgn().equals("")){
                        player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cYou do not have access to this bot!"));
                        return;
                    }
                    partyBot(FragBotsApi.getExclusiveIgn());
                    return;
                }
            }
            if(FragBotChatHandler.getInstance().isRunning()){
                player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§cYou have already partied a bot"));
                return;
            }
            player.addChatMessage(new ChatComponentText(FragBots.PREFIX+"Making requests to api to find all online fragbots"));
            List<BotGsonObj> botList;
            try {
                //Gets list of all current bots from our api checking for the ApiReturnError if our api is down
                botList = FragBotsApi.getBotList();
            } catch (ApiReturnError e) {
                player.addChatMessage(new ChatComponentText(FragBots.PREFIX + "§cThere was an error while trying to communicate with our api please contact prince"));
                return;
            }
            List<String> validBotNames = new ArrayList<>();
            //Sorts through all the bots to select all of the valid (online) ones
            for (BotGsonObj bot : botList) {
                if (bot.isOnline()) {
                    validBotNames.add(bot.getUsername());
                }

            }
            //Makes sure there aren't any online bots
            if (validBotNames.size() == 0) {
                player.addChatMessage(new ChatComponentText(FragBots.PREFIX + "§cNo available bots!"));
                return;
            }
            Random rand = new Random();
            //Chooses a random fragbot from array
            //TODO Work on way to track fragbot usage so it can party the least used bot in the last minute
            String randomBot = validBotNames.get(rand.nextInt(validBotNames.size()));
            partyBot(randomBot);
        }).start();
    }

    public void partyBot(String ign){
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§aPartying fragbot with ign: "+ign+"!"));
        FragBotChatHandler.getInstance().addBot(ign);
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/p "+ign);
    }

    @Override
    public String getCommandUsage(ICommandSender arg0) {
        return "/" + getCommandName();
    }
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return Arrays.asList("clearcache","whitelisted","wl","active","ac","exclusive","ex");
        }
        return null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}
