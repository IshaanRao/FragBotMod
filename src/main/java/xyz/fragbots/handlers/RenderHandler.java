package xyz.fragbots.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.collection.parallel.ParIterableLike;
import xyz.fragbots.drawables.Drawable;
import xyz.fragbots.drawables.NotificationDrawable;

import java.util.ArrayList;
import java.util.List;

public class RenderHandler {
    private static RenderHandler instance;

    List<Drawable> thingsToShow = new ArrayList<>();

    public static RenderHandler getInstance() {
        if(instance == null){
            instance=new RenderHandler();
        }
        return instance;
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        try {
            for(Drawable gui : thingsToShow){
                gui.draw(Minecraft.getMinecraft());
            }
        }catch(Exception ignored){

        }
    }

    public void addItem(Drawable item, int time){
         this.thingsToShow.add(item);
         new Thread(()->{
             try {
                 Thread.sleep(time);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             this.thingsToShow.remove(item);
         }).start();
    }
}
