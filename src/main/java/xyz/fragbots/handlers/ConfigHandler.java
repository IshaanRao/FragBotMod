package xyz.fragbots.handlers;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ConfigHandler {
    private Configuration config = null;
    public ConfigHandler(File config) {
        this.config = new Configuration(config);
        this.config.load();
        if(!this.config.hasCategory("general-settings")){
            this.config.addCustomCategoryComment("general-settings","Settings for the mod");
        }
        ConfigCategory cat = this.config.getCategory("general-settings");
        if(!cat.containsKey("showalerts")){
            cat.put("showalerts",new Property("showalerts","true", Property.Type.BOOLEAN));
        }
        this.config.save();
    }
    public void setShowAlerts(boolean showAlerts){
        ConfigCategory cat = this.config.getCategory("general-settings");
        cat.put("showalerts",new Property("showalerts",String.valueOf(showAlerts), Property.Type.BOOLEAN));
        this.config.save();
    }
    public boolean isShowAlerts() {
        ConfigCategory cat = this.config.getCategory("general-settings");
        return cat.get("showalerts").getBoolean();
    }
}
