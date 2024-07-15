package co.ipicorp.saas.consumerapi.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.consumerapi.form.OrderSelloutForm;
import co.ipicorp.saas.consumerapi.form.OrderSelloutItemForm;
import co.ipicorp.saas.consumerapi.util.ErrorCode;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.RetailerWarehouseTotalItem;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseTotalItemService;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class AmountItemRemainValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
    
    @Autowired
    private ProductVariationService productVariationService;
    
    @Autowired
    private RetailerWarehouseTotalItemService retailerWarehouseTotalItemService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		OrderSelloutForm orderSelloutForm = (OrderSelloutForm) form;
		return this.validateById(orderSelloutForm, errors);
	}
	
	private boolean validateById(OrderSelloutForm orderSelloutForm, Errors errors) {
		
		if ( orderSelloutForm.getItems() != null && orderSelloutForm.getItems().size() > 0 ) {
			
			for (OrderSelloutItemForm item : orderSelloutForm.getItems()) {
				ProductVariation productVariation = productVariationService.get(item.getProductVariationId());
				if ( productVariation == null || productVariation.getStatus() == Status.DELETED) {
					errors.reject(ErrorCode.APP_1905_PRODUCT_VARIATION_NOT_EXIST,
							new Object[] { "ProductVariation", item.getProductVariationId() },
							ErrorCode.APP_1905_PRODUCT_VARIATION_NOT_EXIST );
					
					return !errors.hasErrors();
				}
				
				if (productVariation.getStatus() == Status.INACTIVE) {
					errors.reject(ErrorCode.APP_1906_PRODUCT_VARIATION_INACTIVE,
							new Object[] { "ProductVariation", productVariation.getName() },
							ErrorCode.APP_1906_PRODUCT_VARIATION_INACTIVE );
				}
				
				RetailerWarehouseTotalItem retailerWarehouseTotalItem = this.retailerWarehouseTotalItemService
																			.getByRetailerWarehouseTotalItemInfo(orderSelloutForm.getRetailerId(),
																			 productVariation.getProductId(),
																			 productVariation.getId());
				if ( retailerWarehouseTotalItem == null ) {
					errors.reject(ErrorCode.APP_1903_RETAILER_WAREHOUSE_TOTAL_ITEM_NOT_EXIST,
							new Object[] { "RetailerWarehouseTotalItem", orderSelloutForm.getRetailerId(), productVariation.getProductId(), item.getProductVariationId() },
							ErrorCode.APP_1903_RETAILER_WAREHOUSE_TOTAL_ITEM_NOT_EXIST );
					
					return !errors.hasErrors();
				}
				
				Integer amount = retailerWarehouseTotalItem.getAmountAvailable() - item.getAmount();
				if ( amount < 0) {
					errors.reject(ErrorCode.APP_1904_RETAILER_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH,
							new Object[] { "Order Sellout Item", Math.abs(amount) },
								  ErrorCode.APP_1904_RETAILER_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH);
					
					return !errors.hasErrors();
				}
				
	    	}
		}

		return !errors.hasErrors();
	}
}
