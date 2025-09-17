# Sample Jedis Java Redis Application

This Java application demonstrates Redis connectivity with RediSearch functionality for account data management.

## Features

- **Redis Connection**: Secure connection to Redis Cloud with authentication
- **Search Index Management**: Automatic creation and management of RediSearch indexes using Jedis built-in API
- **Account Data Storage**: JSON-based storage using proper `jedis.jsonSet()` and `jedis.jsonGet()` methods
- **Full-Text Search**: Comprehensive search functionality using `jedis.ftSearch()` with field-specific returns
- **Modern Jedis API**: Uses `JedisPooled` for optimal connection pooling with full JSON support

## Search Index Schema

The application automatically creates a search index `idx:accountStr` with the following schema:

```
FT.CREATE idx:accountStr ON JSON
 PREFIX 1 ssc-dev:accountNumber:
 SCHEMA
    $.consumerId as consumerId TEXT
    $.accountNumber as accountNumber TEXT
    $.last4SSN as last4SSN TEXT
    $.dateOfBirth as dateOfBirth TEXT
    $.lastName as lastName TEXT
    $.platform as platform TAG
    $.delinquencyDate as delinquencyDate TEXT
    $.solDate as solDate TEXT
    $.type as type TAG
    $.consumerState as consumerState TAG
    $.consumerZip as consumerZip TEXT
    $.accountType as accountType TAG
    $.recoveryFlag as recoveryFlag TAG
    $.isEngageAccount as isEngageAccount TAG
```

## Configuration

1. Copy `app/src/main/resources/redis.properties.template` to `app/src/main/resources/redis.properties`
2. Update the configuration with your Redis connection details:

```properties
redis.host=your-redis-host.com
redis.port=6379
redis.username=your-username
redis.password=your-password
```

## Running the Application

```bash
./gradlew run
```

The application will:
1. Connect to Redis
2. Check if the search index exists
3. Create the search index if it doesn't exist
4. Store sample account data
5. Demonstrate comprehensive search functionality using `jedis.ftSearch()`

## Search Functionality

The application demonstrates various search patterns using Jedis built-in search methods:

### Search Examples Demonstrated
- **Text field search**: `@lastName:Smith @last4SSN:1234`
- **TAG field search**: `@consumerState:{CA}` (note the curly braces for TAG fields)
- **Platform search**: `@platform:{WEB}`
- **Multiple criteria**: `@type:{CREDIT} @consumerState:{CA}`
- **Field-specific returns**: Returns only specified fields like `accountNumber` and `consumerId`

### Manual Redis Commands (for reference)
```bash
# Search by last name and SSN with specific field returns
FT.SEARCH idx:accountStr '@lastName:Smith @last4SSN:1234' RETURN 2 accountNumber consumerId

# Search by platform (TAG field)
FT.SEARCH idx:accountStr '@platform:{WEB}' RETURN 3 accountNumber consumerId lastName

# Search by state (TAG field)
FT.SEARCH idx:accountStr '@consumerState:{CA}' RETURN 3 accountNumber lastName platform

# Complex query - credit accounts in California
FT.SEARCH idx:accountStr '@type:{CREDIT} @consumerState:{CA}' RETURN 2 accountNumber consumerId
```

## Project Structure

- `App.java` - Main application entry point
- `RedisConfig.java` - Configuration management
- `RedisSearchManager.java` - Search index management
- `AccountData.java` - Account data model with JSON serialization
- `redis.properties` - Redis connection configuration (not in git)
- `redis.properties.template` - Configuration template

## Testing

Run tests with:

```bash
./gradlew test
```

## Dependencies

- **Jedis 5.2.0** - Redis Java client
- **Gson** - JSON serialization (via Guava dependency)
- **JUnit 5** - Testing framework
