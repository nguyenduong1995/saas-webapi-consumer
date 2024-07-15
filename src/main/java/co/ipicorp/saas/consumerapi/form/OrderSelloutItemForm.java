/**
 * OrderSelloutItemForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import java.io.Serializable;

/**
 * OrderSelloutItemForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class OrderSelloutItemForm implements Serializable {

	private static final long serialVersionUID = -1241124773964142290L;

    private Integer productVariationId;
	
    private Integer amount;
    
	public Integer getProductVariationId() {
		return productVariationId;
	}

	public void setProductVariationId(Integer productVariationId) {
		this.productVariationId = productVariationId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
    public String toString() {
        return "OrderSelloutItemForm [productVariationId=" + productVariationId + ", amount=" + amount + "]";
    }
	
}
