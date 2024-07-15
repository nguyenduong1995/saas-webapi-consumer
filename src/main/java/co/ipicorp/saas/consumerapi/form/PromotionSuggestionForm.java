/**
 * PromotionSuggestionForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * PromotionSuggestionForm. <<< Detail note.
 * 
 * @author thuy nguyen
 * @access public
 */
public class PromotionSuggestionForm implements Serializable {

    private static final long serialVersionUID = -1182475772114949030L;

    private Integer retailerId = -1;

    @JsonProperty("productsInCart")
    private List<ProductInCartForm> productsInCart;

    /**
     * get value of <b>productsInCart</b>.
     * 
     * @return the productsInCart
     */
    public List<ProductInCartForm> getProductsInCart() {
        return productsInCart;
    }

    /**
     * Set value to <b>productsInCart</b>.
     * 
     * @param productsInCart
     *            the productsInCart to set
     */
    public void setProductsInCart(List<ProductInCartForm> productsInCart) {
        this.productsInCart = productsInCart;
    }

    /**
     * get value of <b>retailerId</b>.
     * 
     * @return the retailerId
     */
    public Integer getRetailerId() {
        return retailerId;
    }

    /**
     * Set value to <b>retailerId</b>.
     * 
     * @param retailerId
     *            the retailerId to set
     */
    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    @Override
    public String toString() {
        return "PromotionSuggestionForm [retailerId=" + retailerId + ", productsInCart=" + productsInCart + "]";
    }

}
