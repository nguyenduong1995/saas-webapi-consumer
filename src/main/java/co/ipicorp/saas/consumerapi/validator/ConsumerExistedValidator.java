package co.ipicorp.saas.consumerapi.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.consumerapi.form.ConsumerAddressForm;
import co.ipicorp.saas.consumerapi.form.ConsumerForm;
import co.ipicorp.saas.consumerapi.form.ConsumerTelephoneUpdateForm;
import co.ipicorp.saas.consumerapi.form.OrderSelloutForm;
import co.ipicorp.saas.consumerapi.util.ErrorCode;
import co.ipicorp.saas.nrms.model.Consumer;
import co.ipicorp.saas.nrms.service.ConsumerService;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ConsumerExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof ConsumerAddressForm || form instanceof OrderSelloutForm 
				|| form instanceof ConsumerTelephoneUpdateForm || form instanceof ConsumerForm;
	}
	
	@Autowired
	private ConsumerService consumerService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		Integer customerId = null;
		if (form instanceof OrderSelloutForm) {
			customerId = ((OrderSelloutForm) form).getConsumerId();
		} else if (form instanceof ConsumerTelephoneUpdateForm) {
			customerId = ((ConsumerTelephoneUpdateForm) form).getConsumerId();
		} else if (form instanceof ConsumerForm) {
			customerId = ((ConsumerForm) form).getConsumerId();
		}

		return this.validateById(customerId, errors);
	}
	
	private boolean validateById(Integer customerId, Errors errors) {
		if ( customerId != null ) {
			Consumer consumer = this.consumerService.getActivated(customerId);
			if ( consumer == null ) {
				errors.reject(ErrorCode.APP_1301_CONSUMER_NOT_EXIST,
						new Object[] { "CustomerId", customerId },
						ErrorCode.APP_1301_CONSUMER_NOT_EXIST);
			}
		}
		
		return !errors.hasErrors();
	}
}
