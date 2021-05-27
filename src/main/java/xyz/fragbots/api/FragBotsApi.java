package xyz.fragbots.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import xyz.fragbots.Utils.HashUtils;
import xyz.fragbots.errors.ApiReturnError;
import xyz.fragbots.gsonobjs.BotGsonObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO Add functions to communicate with private backend for whitelisted bot

/*

* Function class for communicating with the backend

*/

public class FragBotsApi {
    private static String whitelistedIgn = null;
    private static String activeIgn = null;
    private static String exclusiveIgn = null;
    private static List<BotGsonObj> botListCache = null;
    //Gets the raw JsonObject from the backend feel free to use this in your own creations!
    public static JsonObject readBotsList() throws ApiReturnError {
        String resp = Request.getRequest("http://api.fragbots.xyz/v2/botslist");
        if(resp == null){
            throw new ApiReturnError("API returned null");
        }
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(resp,JsonObject.class);
        if(json == null){
            throw new ApiReturnError("Unable to parse json");
        }
        return json;
    }

    //Parses the json object into a class which allows me to easily get all of the information
    public static List<BotGsonObj> getBotList() throws ApiReturnError {
        if(botListCache != null){
            return botListCache;
        }
        JsonArray botJson = readBotsList().getAsJsonArray("bots");
        Gson gson = new Gson();
        List<BotGsonObj> botList = new ArrayList<>();
        for(JsonElement bot : botJson){
            botList.add(gson.fromJson(bot,BotGsonObj.class));
        }
        botListCache = botList;
        return botList;
    }

    //Clears cache
    public static void clearCache(){
        botListCache = null;
        whitelistedIgn = null;
        activeIgn = null;
        exclusiveIgn = null;
    }

    //Joins "fake server" using mojangs api so we can verify your indentity to see if you can use the private frag bots
    public static String joinServer(){

        Gson gson = new Gson();
        //Generate random server id hash to verify your identity
        String serverId = HashUtils.hash(Minecraft.getMinecraft().getSession().getPlayerID());
        String body = gson.toJson(new HashMap<>(new HashMap<String,String>(){{
            //Yes this is your session id but before you freak out this is not being saved and is being sent to MOJANGS API.
            put("accessToken", Minecraft.getMinecraft().getSession().getToken());
            put("selectedProfile", Minecraft.getMinecraft().getSession().getPlayerID()); //This is your uuid
            put("serverId", serverId); // Hash we generated earlier
        }}));
        Request.postRequest("https://sessionserver.mojang.com/session/minecraft/join",body);
        return serverId;
    }

    public static String getWhitelistedIgn() {
        if(whitelistedIgn!=null){
            return whitelistedIgn;
        }
        String serverId = joinServer();
        String username = Minecraft.getMinecraft().getSession().getUsername();
        //Check if mojang actually saw you joining the server
        JsonObject ignJson = Request.getRequestJson("http://api.fragbots.xyz/v2/authwhitelist?username="+username+"&serverId="+serverId);
        if(ignJson == null){
            whitelistedIgn = "";
        }else{
            if(ignJson.get("botusername")==null){
                whitelistedIgn = "";
            }else{
                whitelistedIgn = ignJson.get("botusername").getAsString();
            }
        }
        return whitelistedIgn;
    }
    public static String getActiveIgn(){
        if(activeIgn!=null){
            return activeIgn;
        }
        String serverId = joinServer();
        String username = Minecraft.getMinecraft().getSession().getUsername();
        //Check if mojang actually saw you joining the server
        JsonObject ignJson = Request.getRequestJson("http://api.fragbots.xyz/v2/authactive?username="+username+"&serverId="+serverId);
        if(ignJson == null){
            activeIgn = "";
        }else{
            if(ignJson.get("botusername")==null){
                activeIgn = "";
            }else{
                activeIgn = ignJson.get("botusername").getAsString();
            }
        }
        return activeIgn;
    }
    public static String getExclusiveIgn(){
        if(exclusiveIgn!=null){
            return exclusiveIgn;
        }
        String serverId = joinServer();
        String username = Minecraft.getMinecraft().getSession().getUsername();
        //Check if mojang actually saw you joining the server
        JsonObject ignJson = Request.getRequestJson("http://api.fragbots.xyz/v2/authexclusive?username="+username+"&serverId="+serverId);
        if(ignJson == null){
            exclusiveIgn = "";
        }else{
            if(ignJson.get("botusername")==null){
                exclusiveIgn = "";
            }else{
                exclusiveIgn = ignJson.get("botusername").getAsString();
            }
        }
        return exclusiveIgn;
    }
}
