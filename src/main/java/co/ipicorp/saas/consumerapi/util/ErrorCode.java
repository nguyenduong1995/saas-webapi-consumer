/**
 * ErrorCode.java
 * @copyright  Copyright © 2020 Micro App
 * @author     hieumicro
 * @package    co.ipicorp.saas.ms.identity.util
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.util;

/**
 * ErrorCode.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class ErrorCode {
    public static final String APP_1000_SYSTEM_ERROR = "1000";
    public static final String APP_1001_REQUIRED = "1001";
    
    public static final String APP_1101_ACCOUNT_NOT_EXIST = "1101";
    
    public static final String APP_1201_RETAILER_NOT_EXIST = "1201";
    
    public static final String APP_1301_CONSUMER_NOT_EXIST = "1301";
    
    //Product Validation Errors
    public static final String APP_1401_FIELD_NOT_EXIST = "1401";
    public static final String APP_1402_FILED_IS_EXISTED = "1402";
    public static final String APP_1403_FIELD_NOT_BELONG_TO_OBJECT = "1403";
    public static final String APP_1404_FIELD_CAN_NOT_BE_NULL = "1404";
    
    public static final String APP_1501_CONSUMER_ADDRESS_NOT_EXIST = "1501";
    
    public static final String APP_1601_WAREHOUSE_NOT_EXIST = "1601";
    
    public static final String APP_1701_FROM_DATE_NO_REASONABLE = "1701";
    
    public static final String APP_1801_ORDER_SELLIN_NOT_EXIST = "1801";
    
    public static final String APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST = "1901";
    
    public static final String APP_1902_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH= "1902";
    
    public static final String APP_1903_RETAILER_WAREHOUSE_TOTAL_ITEM_NOT_EXIST = "1903";
    
    public static final String APP_1904_RETAILER_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH= "1904";
    
    public static final String APP_1905_PRODUCT_VARIATION_NOT_EXIST = "1905";
    
    public static final String APP_1906_PRODUCT_VARIATION_INACTIVE = "1906";
    
    public static final String APP_2056_OLD_PASSWORD_NOT_MATCH = "2056";
    
    public static final String APP_2201_NEW_PASSWORD_IS_SAME_CURRENT_PASSWORD = "2201";
    
    public static final String APP_2202_NEW_PASSWORD_DIFFERENT_CONFIRM_PASSWORD = "2202";
    
    private ErrorCode() {}
}
