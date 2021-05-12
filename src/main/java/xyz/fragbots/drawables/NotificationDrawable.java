package xyz.fragbots.drawables;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationDrawable extends Drawable {
    private String text;
    public NotificationDrawable(String text){
        this.text = text;
    }

    @Override
    public void draw(Minecraft mc){
        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        drawCenteredString(mc.fontRendererObj, text, width-100, height/40, Integer.parseInt("FFAA00", 16));
    }
}
