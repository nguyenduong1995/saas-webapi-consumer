/**
 * OrderSelloutSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * OrderSelloutSearchForm. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class OrderSelloutSearchForm extends LimittedForm {

    private static final long serialVersionUID = -307004501680385180L;

    private String fromDate; // dd/MM/yyyy

    private String toDate;

    private Integer customerId;

    private String orderCode;

    /**
     * get value of <b>fromDate</b>.
     * 
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Set value to <b>fromDate</b>.
     * 
     * @param fromDate
     *            the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * get value of <b>toDate</b>.
     * 
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * Set value to <b>toDate</b>.
     * 
     * @param toDate
     *            the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * get value of <b>customerId</b>.
     * 
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Set value to <b>customerId</b>.
     * 
     * @param customerId
     *            the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * get value of <b>orderCode</b>.
     * 
     * @return the orderCode
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * Set value to <b>orderCode</b>.
     * 
     * @param orderCode
     *            the orderCode to set
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Override
    public String toString() {
        return "OrderSelloutSearchForm [fromDate=" + fromDate + ", toDate=" + toDate + ", customerId=" + customerId + ", orderCode=" + orderCode + "]";
    }

}
