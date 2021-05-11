package xyz.fragbots.gsonobjs;

public class BotGsonObj {
    private String uuid;
    private String username;
    private boolean online;
    public String getUsername() {
        return username;
    }
    public String getUuid() {
        return uuid;
    }
    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return "BotGsonObj [uuid="+uuid+", username="+username+", online="+online+"]";
    }
}
