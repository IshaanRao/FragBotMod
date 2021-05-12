package xyz.fragbots;


import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.fragbots.commands.FragRunCommand;
import xyz.fragbots.commands.FragRunSettingsCommand;
import xyz.fragbots.events.ServerDisconnectEvent;
import xyz.fragbots.handlers.ConfigHandler;
import xyz.fragbots.handlers.FragBotChatHandler;
import xyz.fragbots.handlers.RenderHandler;

import java.io.File;

@Mod(modid = FragBots.MODID, version = FragBots.VERSION, clientSideOnly = true, name = "Frag Bots")
public class FragBots {
    public static final String MODID = "FragBots";
    public static final String VERSION = "1.0";
    public static final String PREFIX = "§6§lFragBots§8 »§a ";
    private static ConfigHandler configHandler;
    private File configFile;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        configHandler = new ConfigHandler(configFile);
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        registerCommands();
        registerEvents();
    }
    public void registerCommands(){
        ClientCommandHandler.instance.registerCommand(new FragRunCommand());
        ClientCommandHandler.instance.registerCommand(new FragRunSettingsCommand());
    }
    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(FragBotChatHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(RenderHandler.getInstance());
        FMLCommonHandler.instance().bus().register(new ServerDisconnectEvent());
    }
    public static ConfigHandler getConfigHandler(){
        return configHandler;
    }
}
