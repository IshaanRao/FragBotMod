package xyz.fragbots.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import xyz.fragbots.FragBots;

import java.util.Arrays;
import java.util.List;

public class FragRunSettingsCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "fragrunsettings";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        //Thanks byBackFish for showing me that this existed and I didnt have to create a new arraylist lol
        return Arrays.asList("frsettings", "frsetting");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            StringBuilder message = new StringBuilder();
            message.append("§6-----FragRun-----\n");
            message.append("§9ShowAlerts§8 » ").append((FragBots.getConfigHandler().isShowAlerts()) ? "§aEnabled" : "§cDisabled").append("\n");
            message.append("§6-----------------");
            sender.addChatMessage(new ChatComponentText(message.toString()));
            return;
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("setshowalerts")){
                boolean setTo = Boolean.parseBoolean(args[1]);
                FragBots.getConfigHandler().setShowAlerts(setTo);
                sender.addChatMessage(new ChatComponentText(FragBots.PREFIX+"§7Setting ShowAlerts is now "+((FragBots.getConfigHandler().isShowAlerts()) ? "§aEnabled" : "§cDisabled")));
                return;
            }
        }
        sender.addChatMessage(new ChatComponentText(FragBots.PREFIX+"Incorrect Usage"));
    }
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return Arrays.asList("setshowalerts");
        }else if(args.length==2){
            if(args[0].equalsIgnoreCase("setshowalerts")){
                return Arrays.asList("true","false");
            }
        }
        return null;
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}