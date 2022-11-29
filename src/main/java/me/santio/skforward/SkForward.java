package me.santio.skforward;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
    id = "skforward",
    name = "SkForward",
    version = "1.0",
    description = "Forwards bungee events so skript-reflect can listen to them",
    url = "https://santio.me",
    authors = {"Santio71"}
)
public class SkForward {
    
    private Path dataFolder;
    private RedisClient client;
    
    public static RedisPubSubAsyncCommands<String, String> pubSub;
    
    @Inject
    public SkForward(ProxyServer server, Logger logger, @DataDirectory Path folder) {
        dataFolder = folder;
        
        Toml config = loadConfig(folder, "config");
        if (config == null) {
            logger.error("Failed to load config!");
            return;
        }
        
        // Connect to Redis
        client = RedisClient.create(RedisURI.Builder.redis(config.getString("redis.uri"))
            .withPassword(config.getString("redis.password").toCharArray())
            .build());
        
        if (!client.connect().sync().ping().equals("PONG")) {
            logger.error("Failed to connect to Redis!");
            return;
        }
        
        StatefulRedisPubSubConnection<String, String> connection = client.connectPubSub();
        pubSub = connection.async();
       
        logger.info("Successfully connected to redis!");
        server.getEventManager().register(this, new Listeners());
    }
    
    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        pubSub.quit();
        client.close();
    }
    
    @SuppressWarnings({"ResultOfMethodCallIgnored", "SameParameterValue"})
    private Toml loadConfig(Path path, String config) {
        File file = new File(path.toFile(), config + ".toml");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        
        if (!file.exists()) {
            
            try (InputStream input = getClass().getResourceAsStream("/" + file.getName())) {
                
                if (input != null) Files.copy(input, file.toPath());
                else file.createNewFile();
                
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }
        
        return new Toml().read(file);
    }
    
}
