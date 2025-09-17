package mcm.java;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RedisSearchManagerTest {
    
    @Test 
    void testAccountDataSerialization() {
        // Test AccountData JSON serialization/deserialization
        AccountData original = new AccountData(
            "CONS001", "ACC123456", "1234", "1990-01-15", "Smith",
            "WEB", "2023-06-01", "2023-07-01", "CREDIT",
            "CA", "90210", "PERSONAL", "Y", "N"
        );
        
        String json = original.toJson();
        assertNotNull(json);
        assertTrue(json.contains("CONS001"));
        assertTrue(json.contains("Smith"));
        
        AccountData deserialized = AccountData.fromJson(json);
        assertEquals(original.getConsumerId(), deserialized.getConsumerId());
        assertEquals(original.getAccountNumber(), deserialized.getAccountNumber());
        assertEquals(original.getLastName(), deserialized.getLastName());
        assertEquals(original.getPlatform(), deserialized.getPlatform());
    }
    
    @Test
    void testRedisKeyGeneration() {
        AccountData account = new AccountData();
        account.setAccountNumber("ACC123456");
        
        String expectedKey = "ssc-dev:accountNumber:ACC123456";
        assertEquals(expectedKey, account.getRedisKey());
    }
}
