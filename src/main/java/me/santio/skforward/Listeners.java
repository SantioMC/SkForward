package me.santio.skforward;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class Listeners {
    
    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        SkForward.pubSub.publish("proxy_player_join", event.getPlayer().getUniqueId().toString());
    }
    
    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        SkForward.pubSub.publish("proxy_player_quit", event.getPlayer().getUniqueId().toString());
    }
    
}
