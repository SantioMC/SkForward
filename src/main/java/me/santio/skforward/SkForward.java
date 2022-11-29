package me.santio.skforward;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
    id = "SkForward",
    name = "SkForward",
    version = "1.0",
    description = "Forwards bungee events so skript-reflect can listen to them",
    url = "https://santio.me",
    authors = {"Santio71"}
)
public class SkForward {
    
    @Inject private Logger logger;
    
    
    
}
