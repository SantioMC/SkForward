package me.santio.skforward;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class Listeners {
    
    private String toRediSkript(String message) {
        // This isn't the best way to handle this, however for our needs it'll be alright
        return "{\"Type\":\"Skript\",\"Messages\":[\"" + message + "!\"],\"Date\":" + System.currentTimeMillis() + "}";
    }
    
    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        System.out.println("Player join");
        SkForward.pubSub.publish("proxy_player_join", toRediSkript(event.getPlayer().getUniqueId().toString()));
    }
    
    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        System.out.println("Player quit");
        SkForward.pubSub.publish("proxy_player_quit", toRediSkript(event.getPlayer().getUniqueId().toString()));
    }
    
}
