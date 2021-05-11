package xyz.fragbots;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import xyz.fragbots.api.FragBotsApi;
import xyz.fragbots.api.Request;
import xyz.fragbots.commands.FragRunCommand;
import xyz.fragbots.errors.ApiReturnError;

@Mod(modid = FragBots.MODID, version = FragBots.VERSION, clientSideOnly = true, name = "Frag Bots")
public class FragBots {
    public static final String MODID = "FragBots";
    public static final String VERSION = "1.0";
    public static final String PREFIX = "§6§lFragBots§8 »§a ";

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        registerCommands();
    }
    public void registerCommands(){
        ClientCommandHandler.instance.registerCommand(new FragRunCommand());
    }
}
