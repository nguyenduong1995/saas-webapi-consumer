/**
 * ProductResourceForm.java
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
 * ProductResourceForm. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
public class ProductResourceDeleteForm implements Serializable {

    private static final long serialVersionUID = -7375409240989228038L;

    @JsonProperty("productVariationId")
    private Integer productVariationId;

    @JsonProperty("productResourceIds")
    private List<Integer> productResourceIds = new ArrayList<Integer>();

    /**
     * get value of <b>productVariationId</b>.
     * 
     * @return the productVariationId
     */
    public Integer getProductVariationId() {
        return productVariationId;
    }

    /**
     * Set value to <b>productVariationId</b>.
     * 
     * @param productVariationId
     *            the productVariationId to set
     */
    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

    /**
     * get value of <b>productResourceIds</b>.
     * 
     * @return the productResourceIds
     */
    public List<Integer> getProductResourceIds() {
        return productResourceIds;
    }

    /**
     * Set value to <b>productResourceIds</b>.
     * 
     * @param productResourceIds
     *            the productResources to set
     */
    public void setProductResourceIds(List<Integer> productResourceIds) {
        this.productResourceIds = productResourceIds;
    }

    @Override
    public String toString() {
        return "ProductResourceDeleteForm [productVariationId=" + productVariationId + ", productResources=" + productResourceIds + "]";
    }

}
