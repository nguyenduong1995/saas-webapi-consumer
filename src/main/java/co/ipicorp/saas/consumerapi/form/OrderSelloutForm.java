/**
 * OrderSellinForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OrderSellinForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class OrderSelloutForm implements Serializable {

	private static final long serialVersionUID = -1241124773964142290L;

    private Integer retailerId;
	
    private Integer consumerId;
    
    private Integer consumerAddressId;
	
	@JsonProperty("items")
    private List<OrderSelloutItemForm> items;
	
    public Integer getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Integer retailerId) {
		this.retailerId = retailerId;
	}

	public Integer getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Integer consumerId) {
		this.consumerId = consumerId;
	}

	public Integer getConsumerAddressId() {
		return consumerAddressId;
	}

	public void setConsumerAddressId(Integer consumerAddressId) {
		this.consumerAddressId = consumerAddressId;
	}

	public List<OrderSelloutItemForm> getItems() {
		return items;
	}

	public void setItems(List<OrderSelloutItemForm> items) {
		this.items = items;
	}

	@Override
    public String toString() {
        return "OrderSelloutForm [retailerId=" + retailerId + "consumerId=" + consumerId + "consumerAddressId=" 
        		+ consumerAddressId + "items=" + items + "]";
    }
	
}
