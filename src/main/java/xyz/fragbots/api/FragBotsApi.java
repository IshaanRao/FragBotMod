package xyz.fragbots.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import xyz.fragbots.errors.ApiReturnError;
import xyz.fragbots.gsonobjs.BotGsonObj;

import java.util.ArrayList;
import java.util.List;

//TODO Add functions to communicate with private backend for whitelisted bot

/*

* Function class for communicating with the backend

*/

public class FragBotsApi {

    //Gets the raw JsonObject from the backend feel free to use this in your own creations!
    public static JsonObject readBotsList() throws ApiReturnError {
        String resp = Request.getRequest("https://api.fragbots.xyz/v2/botslist");
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
        JsonArray botJson = readBotsList().getAsJsonArray("bots");
        Gson gson = new Gson();
        List<BotGsonObj> botList = new ArrayList<>();
        for(JsonElement bot : botJson){
            botList.add(gson.fromJson(bot,BotGsonObj.class));
        }
        return botList;
    }
}
