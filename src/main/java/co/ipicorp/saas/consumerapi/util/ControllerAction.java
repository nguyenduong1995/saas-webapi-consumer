/**
 * ControllerAction.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.util;

/**
 * ControllerAction. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class ControllerAction {

    public static final String APP_ERROR_ACTION = "/error";
    public static final String APP_COMMON_ACTION = "/common";
    public static final String APP_LOGIN_ACTION = "/login";
    public static final String APP_LOGOUT_ACTION = "/logout";

    /*
     * CUSTOMER ANON ACTION
     */
    public static final String APP_REGISTER_ACTION = "/register";
    public static final String APP_FORGOT_PASSWORD_ACTION = "/forgot-password";
    public static final String APP_RESET_PASSWORD_ACTION = "/reset-password";
    public static final String APP_RESET_PASSWORD_CHECK_KEY_ACTION = "/reset-password/check";
    public static final String APP_VERIFY_PHONE_ACTION = "/verify-phone";
    public static final String APP_VERIFY_OTP_ACTION = "/verify-otp";
    
    /*
     * Consumer Address action
     */
    public static final String APP_CONSUMER_ADDRESS_ACTION = "/addresses";
    public static final String APP_CONSUMER_ADDRESS_DETAIL_ACTION = "/addresses/{id}";
    public static final String APP_CONSUMER_ADDRESSS_SET_DEFAULT_ACTION = "/addresses/{id}/setDefault";
    
    /*
     * Order Sellout action
     */
    public static final String APP_PORTAL_ORDER_SELLOUT_BY_RETAILER_ACTION = "/orders/sellout/retailer";
    public static final String APP_PORTAL_ORDER_SELLOUT_BY_CONSUMER_ACTION = "/orders/sellout/consumer";
    public static final String APP_ORDER_SELLOUT_ACTION = "/orders/sellout";
    public static final String APP_ORDER_SELLOUT_DETAIL_REWARD_ACTION = "/orders/sellout/{id}/rewards";
    public static final String APP_ORDER_SELLOUT_SEARCH_ACTION = "/orders/sellout/search";
    
    /*
     * Consumer action on Retailer
     */
    public static final String APP_RETAILER_SEARCH_ACTION = "/retailers/search";
    public static final String APP_RETAILER_STOCKING_ACTION = "/retailers/stocking";
    public static final String APP_ACTION_ON_RETAILER_ACTION = "/actionOnRetailer";
    
    /*
     * PRODUCT VARIATION
     */
    public static final String APP_PRODUCT_VARIATION_GET_TOP = "/productVariations/top/{number}";
    public static final String APP_PRODUCT_VARIATION_SEARCH = "/productVariations/search";
    public static final String APP_PRODUCT_VARIATION_COUNT_IN_WAREHOUSE_ACTION = "/productVariations/{id}/countInWarehouse";
    
    /*
     * Consumer action
     */
    public static final String APP_CONSUMER_ACTION = "/consumer";
    
    /*
     * Promotion Action
     */
    public static final String APP_PROMOTION_ACTION = "/promotions";
    public static final String APP_PROMOTION_DETAIL_ACTION = "/promotions/{id}";
    public static final String APP_PROMOTION_CHECKCART_SELLOUT_ACTION = "/promotions/checkCartSellout";
    
}
