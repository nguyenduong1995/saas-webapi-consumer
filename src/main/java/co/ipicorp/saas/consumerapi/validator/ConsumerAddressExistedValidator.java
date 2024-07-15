package co.ipicorp.saas.consumerapi.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.consumerapi.form.OrderSelloutForm;
import co.ipicorp.saas.consumerapi.util.ErrorCode;
import co.ipicorp.saas.nrms.model.ConsumerAddress;
import co.ipicorp.saas.nrms.service.ConsumerAddressService;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ConsumerAddressExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof OrderSelloutForm;
	}
	
	@Autowired
    private ConsumerAddressService consumerAddressService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		Integer consumerAddressId = null;
		if (form instanceof OrderSelloutForm) {
			consumerAddressId = ((OrderSelloutForm) form).getConsumerAddressId();
		}
		
		return this.validateById(consumerAddressId, errors);
	}
	
	private boolean validateById(Integer consumerAddressId, Errors errors) {
		
		if ( consumerAddressId != null ) {
			ConsumerAddress consumerAddress = this.consumerAddressService.getActivated(consumerAddressId);
			if ( consumerAddress == null ) {
				errors.reject(ErrorCode.APP_1501_CONSUMER_ADDRESS_NOT_EXIST,
						new Object[] { "ConsumerAddressId", consumerAddressId },
						ErrorCode.APP_1501_CONSUMER_ADDRESS_NOT_EXIST);
			}
		}
		
		return !errors.hasErrors();
	}
}
