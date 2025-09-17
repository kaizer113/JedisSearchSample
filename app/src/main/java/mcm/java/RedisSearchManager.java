package mcm.java;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.FTCreateParams;
import redis.clients.jedis.search.IndexDataType;
import redis.clients.jedis.search.schemafields.SchemaField;
import redis.clients.jedis.search.schemafields.TextField;
import redis.clients.jedis.search.schemafields.TagField;

public class RedisSearchManager {
    private static final String INDEX_NAME = "idx:accountStr";
    private static final String KEY_PREFIX = "ssc-dev:accountNumber:";
    
    /**
     * Checks if the search index exists and creates it if it doesn't
     * @param jedis Redis connection (regular Jedis)
     * @return true if index was created, false if it already existed
     */
    public static boolean ensureSearchIndexExists(Jedis jedis) {
        // Create UnifiedJedis from the existing Jedis connection info
        RedisConfig config = new RedisConfig();
        try (UnifiedJedis unifiedJedis = new UnifiedJedis("redis://" + config.getUsername() + ":" +
                                                         config.getPassword() + "@" + config.getHost() +
                                                         ":" + config.getPort())) {
            return ensureSearchIndexExistsInternal(unifiedJedis);
        }
    }

    /**
     * Internal method that works with UnifiedJedis
     * @param jedis UnifiedJedis connection
     * @return true if index was created, false if it already existed
     */
    private static boolean ensureSearchIndexExistsInternal(UnifiedJedis jedis) {
        try {
            // Check if index exists by trying to get info about it
            jedis.ftInfo(INDEX_NAME);
            System.out.println("Search index '" + INDEX_NAME + "' already exists");
            return false;
        } catch (JedisDataException e) {
            // Index doesn't exist if we get an error, so create it
            if (e.getMessage().contains("Unknown index name")) {
                return createSearchIndexInternal(jedis);
            } else {
                throw new RuntimeException("Error checking search index: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Creates the search index with the specified schema
     * @param jedis Redis connection
     * @return true if index was created successfully
     */
    private static boolean createSearchIndexInternal(UnifiedJedis jedis) {
        try {
            System.out.println("Creating search index '" + INDEX_NAME + "'...");

            // Define the schema fields using Jedis schema field classes
            SchemaField[] schema = {
                TextField.of("$.consumerId").as("consumerId"),
                TextField.of("$.accountNumber").as("accountNumber"),
                TextField.of("$.last4SSN").as("last4SSN"),
                TextField.of("$.dateOfBirth").as("dateOfBirth"),
                TextField.of("$.lastName").as("lastName"),
                TagField.of("$.platform").as("platform"),
                TextField.of("$.delinquencyDate").as("delinquencyDate"),
                TextField.of("$.solDate").as("solDate"),
                TagField.of("$.type").as("type"),
                TagField.of("$.consumerState").as("consumerState"),
                TextField.of("$.consumerZip").as("consumerZip"),
                TagField.of("$.accountType").as("accountType"),
                TagField.of("$.recoveryFlag").as("recoveryFlag"),
                TagField.of("$.isEngageAccount").as("isEngageAccount")
            };

            // Create the index using Jedis ftCreate method
            String result = jedis.ftCreate(INDEX_NAME,
                FTCreateParams.createParams()
                    .on(IndexDataType.JSON)
                    .addPrefix(KEY_PREFIX),
                schema
            );

            System.out.println("Search index '" + INDEX_NAME + "' created successfully! Result: " + result);
            // Wait a moment for indexing to complete
            Thread.sleep(1000);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Error creating search index: " + e.getMessage(), e);
        }
    }
    
    /**
     * Drops the search index (useful for testing or rebuilding)
     * @param jedis Redis connection
     */
    public static void dropSearchIndex(UnifiedJedis jedis) {
        try {
            String result = jedis.ftDropIndex(INDEX_NAME);
            System.out.println("Search index '" + INDEX_NAME + "' dropped successfully! Result: " + result);
        } catch (JedisDataException e) {
            if (e.getMessage().contains("Unknown index name")) {
                System.out.println("Search index '" + INDEX_NAME + "' does not exist");
            } else {
                throw new RuntimeException("Error dropping search index: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Gets information about the search index
     * @param jedis Redis connection
     * @return index information as string
     */
    public static String getIndexInfo(UnifiedJedis jedis) {
        try {
            Object result = jedis.ftInfo(INDEX_NAME);
            return result.toString();
        } catch (JedisDataException e) {
            if (e.getMessage().contains("Unknown index name")) {
                return "Index '" + INDEX_NAME + "' does not exist";
            } else {
                throw new RuntimeException("Error getting search index info: " + e.getMessage(), e);
            }
        }
    }
}
