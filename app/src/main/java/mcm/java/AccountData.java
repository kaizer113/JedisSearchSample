package mcm.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Represents account data that will be stored in Redis as JSON
 * and indexed by the search index
 */
public class AccountData {
    private String consumerId;
    private String accountNumber;
    private String last4SSN;
    private String dateOfBirth;
    private String lastName;
    private String platform;
    private String delinquencyDate;
    private String solDate;
    private String type;
    private String consumerState;
    private String consumerZip;
    private String accountType;
    private String recoveryFlag;
    private String isEngageAccount;
    
    // Default constructor
    public AccountData() {}
    
    // Constructor with all fields
    public AccountData(String consumerId, String accountNumber, String last4SSN, 
                      String dateOfBirth, String lastName, String platform,
                      String delinquencyDate, String solDate, String type,
                      String consumerState, String consumerZip, String accountType,
                      String recoveryFlag, String isEngageAccount) {
        this.consumerId = consumerId;
        this.accountNumber = accountNumber;
        this.last4SSN = last4SSN;
        this.dateOfBirth = dateOfBirth;
        this.lastName = lastName;
        this.platform = platform;
        this.delinquencyDate = delinquencyDate;
        this.solDate = solDate;
        this.type = type;
        this.consumerState = consumerState;
        this.consumerZip = consumerZip;
        this.accountType = accountType;
        this.recoveryFlag = recoveryFlag;
        this.isEngageAccount = isEngageAccount;
    }
    
    // Getters and setters
    public String getConsumerId() { return consumerId; }
    public void setConsumerId(String consumerId) { this.consumerId = consumerId; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getLast4SSN() { return last4SSN; }
    public void setLast4SSN(String last4SSN) { this.last4SSN = last4SSN; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    
    public String getDelinquencyDate() { return delinquencyDate; }
    public void setDelinquencyDate(String delinquencyDate) { this.delinquencyDate = delinquencyDate; }
    
    public String getSolDate() { return solDate; }
    public void setSolDate(String solDate) { this.solDate = solDate; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getConsumerState() { return consumerState; }
    public void setConsumerState(String consumerState) { this.consumerState = consumerState; }
    
    public String getConsumerZip() { return consumerZip; }
    public void setConsumerZip(String consumerZip) { this.consumerZip = consumerZip; }
    
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    
    public String getRecoveryFlag() { return recoveryFlag; }
    public void setRecoveryFlag(String recoveryFlag) { this.recoveryFlag = recoveryFlag; }
    
    public String getIsEngageAccount() { return isEngageAccount; }
    public void setIsEngageAccount(String isEngageAccount) { this.isEngageAccount = isEngageAccount; }
    
    /**
     * Converts this account data to JSON string
     */
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    
    /**
     * Creates AccountData from JSON string
     */
    public static AccountData fromJson(String json) {
        Gson gson = new Gson();

        // Handle the case where JSON is wrapped in a "map" object from Redis JSON operations
        if (json.contains("\"map\":")) {
            // Parse as a generic object first to extract the "map" field
            com.google.gson.JsonObject jsonObject = gson.fromJson(json, com.google.gson.JsonObject.class);
            if (jsonObject.has("map")) {
                com.google.gson.JsonObject mapObject = jsonObject.getAsJsonObject("map");
                return gson.fromJson(mapObject, AccountData.class);
            }
        }

        return gson.fromJson(json, AccountData.class);
    }
    
    /**
     * Generates the Redis key for this account
     */
    public String getRedisKey() {
        return "ssc-dev:accountNumber:" + accountNumber;
    }
    
    @Override
    public String toString() {
        return "AccountData{" +
                "consumerId='" + consumerId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", lastName='" + lastName + '\'' +
                ", platform='" + platform + '\'' +
                ", type='" + type + '\'' +
                ", consumerState='" + consumerState + '\'' +
                '}';
    }
}
