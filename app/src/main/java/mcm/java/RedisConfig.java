package mcm.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisConfig {
    private static final String CONFIG_FILE = "redis.properties";
    private final Properties properties;
    
    public RedisConfig() {
        properties = new Properties();
        loadProperties();
    }
    
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE + " in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading Redis configuration", e);
        }
    }
    
    public String getHost() {
        return properties.getProperty("redis.host", "localhost");
    }
    
    public int getPort() {
        return Integer.parseInt(properties.getProperty("redis.port", "6379"));
    }
    
    public String getUsername() {
        return properties.getProperty("redis.username");
    }
    
    public String getPassword() {
        return properties.getProperty("redis.password");
    }
    
    public int getMaxTotal() {
        return Integer.parseInt(properties.getProperty("redis.pool.maxTotal", "10"));
    }
    
    public int getMaxIdle() {
        return Integer.parseInt(properties.getProperty("redis.pool.maxIdle", "5"));
    }
    
    public int getMinIdle() {
        return Integer.parseInt(properties.getProperty("redis.pool.minIdle", "1"));
    }
    
    public boolean getTestOnBorrow() {
        return Boolean.parseBoolean(properties.getProperty("redis.pool.testOnBorrow", "true"));
    }
    
    public boolean getTestOnReturn() {
        return Boolean.parseBoolean(properties.getProperty("redis.pool.testOnReturn", "true"));
    }
    
    public boolean getTestWhileIdle() {
        return Boolean.parseBoolean(properties.getProperty("redis.pool.testWhileIdle", "true"));
    }
}
