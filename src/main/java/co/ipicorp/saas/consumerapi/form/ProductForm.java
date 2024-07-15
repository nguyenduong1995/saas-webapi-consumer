/**
 * ProductForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductForm. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
public class ProductForm implements Serializable {

    private static final long serialVersionUID = -6243492463915187507L;

    @JsonProperty("id")
    private Integer productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("brandId")
    private Integer brandId;

    @JsonProperty("categroyIds")
    private List<Integer> categroyIds = new ArrayList<Integer>();
    
    @JsonProperty("status")
    private Integer status;

    /**
     * get value of <b>productId</b>.
     * 
     * @return the productId
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * Set value to <b>productId</b>.
     * 
     * @param productId
     *            the productId to set
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * get value of <b>productName</b>.
     * 
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Set value to <b>productName</b>.
     * 
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * get value of <b>productCode</b>.
     * 
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Set value to <b>productCode</b>.
     * 
     * @param productCode
     *            the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * get value of <b>brandId</b>.
     * 
     * @return the brandId
     */
    public Integer getBrandId() {
        return brandId;
    }

    /**
     * Set value to <b>brandId</b>.
     * 
     * @param brandId
     *            the brandId to set
     */
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    /**
     * get value of <b>categroyIds</b>.
     * 
     * @return the categroyIds
     */
    public List<Integer> getCategroyIds() {
        return categroyIds;
    }

    /**
     * Set value to <b>categroyIds</b>.
     * 
     * @param categroyIds
     *            the categroyIds to set
     */
    public void setCategroyIds(List<Integer> categroyIds) {
        this.categroyIds = categroyIds;
    }
    
    /**
     * get value of <b>status</b>.
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Set value to <b>status</b>.
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductForm [productId=" + productId + ", productName=" + productName + ", productCode=" + productCode + ", brandId=" + brandId
                + ", categroyIds=" + categroyIds + ", status=" + status + "]";
    }

}
