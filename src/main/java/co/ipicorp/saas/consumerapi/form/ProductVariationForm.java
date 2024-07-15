/**
 * ProductForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import co.ipicorp.saas.nrms.web.form.ProductVariationAttributeForm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import grass.micro.apps.model.base.Status;

/**
 * ProductForm. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
public class ProductVariationForm implements Serializable {

    private static final long serialVersionUID = -6517159294045041692L;
    
    @JsonProperty("id")
    private Integer productVariationId;

    @JsonProperty("productId")
    private Integer productId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private Integer status = Status.ACTIVE.getValue();

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("attributes")
    private List<ProductVariationAttributeForm> attributes = new ArrayList<ProductVariationAttributeForm>();;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("virtualPrice")
    private Double virtualPrice;

    @JsonProperty("incomePrice")
    private Double incomePrice;

    @JsonProperty("packingPrice")
    private Double packingPrice;

    @JsonProperty("unitId")
    private Integer unitId;

    @JsonProperty("packingId")
    private Integer packingId;

    @JsonProperty("minOrder")
    private Integer minOrder;

    @JsonProperty("orderInc")
    private Integer orderInc;

    @JsonProperty("packingExchangeRatio")
    private Integer packingExchangeRatio;

    /**
     * get value of <b>productVariationId</b>.
     * @return the productVariationId
     */
    public Integer getProductVariationId() {
        return productVariationId;
    }

    /**
     * Set value to <b>productVariationId</b>.
     * @param productVariationId the productVariationId to set
     */
    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

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
     * get value of <b>name</b>.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set value to <b>name</b>.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get value of <b>description</b>.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set value to <b>description</b>.
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get value of <b>status</b>.
     * 
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Set value to <b>status</b>.
     * 
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * get value of <b>sku</b>.
     * 
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * Set value to <b>sku</b>.
     * 
     * @param sku
     *            the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * get value of <b>attributes</b>.
     * 
     * @return the attributes
     */
    public List<ProductVariationAttributeForm> getAttributes() {
        return attributes;
    }

    /**
     * Set value to <b>attributes</b>.
     * 
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<ProductVariationAttributeForm> attributes) {
        this.attributes = attributes;
    }

    /**
     * get value of <b>price</b>.
     * 
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Set value to <b>price</b>.
     * 
     * @param price
     *            the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * get value of <b>virtualPrice</b>.
     * 
     * @return the virtualPrice
     */
    public Double getVirtualPrice() {
        return virtualPrice;
    }

    /**
     * Set value to <b>virtualPrice</b>.
     * 
     * @param virtualPrice
     *            the virtualPrice to set
     */
    public void setVirtualPrice(Double virtualPrice) {
        this.virtualPrice = virtualPrice;
    }

    /**
     * get value of <b>incomePrice</b>.
     * 
     * @return the incomePrice
     */
    public Double getIncomePrice() {
        return incomePrice;
    }

    /**
     * Set value to <b>incomePrice</b>.
     * 
     * @param incomePrice
     *            the incomePrice to set
     */
    public void setIncomePrice(Double incomePrice) {
        this.incomePrice = incomePrice;
    }

    /**
     * get value of <b>packingPrice</b>.
     * 
     * @return the packingPrice
     */
    public Double getPackingPrice() {
        return packingPrice;
    }

    /**
     * Set value to <b>packingPrice</b>.
     * 
     * @param packingPrice
     *            the packingPrice to set
     */
    public void setPackingPrice(Double packingPrice) {
        this.packingPrice = packingPrice;
    }

    /**
     * get value of <b>unitId</b>.
     * 
     * @return the unitId
     */
    public Integer getUnitId() {
        return unitId;
    }

    /**
     * Set value to <b>unitId</b>.
     * 
     * @param unitId
     *            the unitId to set
     */
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    /**
     * get value of <b>packingId</b>.
     * 
     * @return the packingId
     */
    public Integer getPackingId() {
        return packingId;
    }

    /**
     * Set value to <b>packingId</b>.
     * 
     * @param packingId
     *            the packingId to set
     */
    public void setPackingId(Integer packingId) {
        this.packingId = packingId;
    }

    /**
     * get value of <b>minOrder</b>.
     * 
     * @return the minOrder
     */
    public Integer getMinOrder() {
        return minOrder;
    }

    /**
     * Set value to <b>minOrder</b>.
     * 
     * @param minOrder
     *            the minOrder to set
     */
    public void setMinOrder(Integer minOrder) {
        this.minOrder = minOrder;
    }

    /**
     * get value of <b>orderInc</b>.
     * 
     * @return the orderInc
     */
    public Integer getOrderInc() {
        return orderInc;
    }

    /**
     * Set value to <b>orderInc</b>.
     * 
     * @param orderInc
     *            the orderInc to set
     */
    public void setOrderInc(Integer orderInc) {
        this.orderInc = orderInc;
    }

    /**
     * get value of <b>packingExchangeRatio</b>.
     * 
     * @return the packingExchangeRatio
     */
    public Integer getPackingExchangeRatio() {
        return packingExchangeRatio;
    }

    /**
     * Set value to <b>packingExchangeRatio</b>.
     * 
     * @param packingExchangeRatio
     *            the packingExchangeRatio to set
     */
    public void setPackingExchangeRatio(Integer packingExchangeRatio) {
        this.packingExchangeRatio = packingExchangeRatio;
    }

    @Override
    public String toString() {
        return "ProductVariationForm [productVariationId=" + productVariationId + ", productId=" + productId + ", name=" + name + ", description=" + description
                + ", status=" + status + ", sku=" + sku + ", attributes=" + attributes + ", price=" + price + ", virtualPrice=" + virtualPrice
                + ", incomePrice=" + incomePrice + ", packingPrice=" + packingPrice + ", unitId=" + unitId + ", packingId=" + packingId + ", minOrder="
                + minOrder + ", orderInc=" + orderInc + ", packingExchangeRatio=" + packingExchangeRatio + "]";
    }

}
