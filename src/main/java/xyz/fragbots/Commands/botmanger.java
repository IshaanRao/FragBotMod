package xyz.fragbots.Commands;

public class botmanger {
    private static botmanger instance;

    public static botmanger getInstance() {
        if(instance == null){
            instance = new botmanger();
        }
        return instance;
    }
    private boolean hasbot;
    private int stage;
    private String ign;
    public botmanger(){
        this.hasbot = false;
        this.stage = 9;
    }

    public int getStage() {
        return stage;
    }

    public boolean isHasbot() {
        return hasbot;
    }

    public void addStage(){
        this.stage++;
        if(this.stage==9){
            this.hasbot = false;
            this.stage = 0;
            this.ign = null;
        }
    }
    public String getIgn(){
        return this.ign;
    }
    public void addBot(String ign){
        this.stage=0;
        this.hasbot = true;
        this.ign = ign;
    }
}
