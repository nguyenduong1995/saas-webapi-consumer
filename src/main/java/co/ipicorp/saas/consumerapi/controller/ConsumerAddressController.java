/**
 * ConsumerAddressController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import co.ipicorp.saas.consumerapi.form.ConsumerAddressForm;
import co.ipicorp.saas.consumerapi.security.ConsumerSessionInfo;
import co.ipicorp.saas.consumerapi.util.Constants;
import co.ipicorp.saas.consumerapi.util.ControllerAction;
import co.ipicorp.saas.consumerapi.validator.ConsumerExistedValidator;
import co.ipicorp.saas.core.service.LocationService;
import co.ipicorp.saas.nrms.model.AddressType;
import co.ipicorp.saas.nrms.model.ConsumerAddress;
import co.ipicorp.saas.nrms.service.ConsumerAddressService;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.AppJsonSchema;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * ConsumerAddressController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
@SuppressWarnings("unchecked")
public class ConsumerAddressController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private ConsumerAddressService caService;
    
    @Autowired
    private LocationService locationService;

    @GetMapping(value = ControllerAction.APP_CONSUMER_ADDRESS_ACTION)
    @ResponseBody
    public ResponseEntity<?> listAllByConsumerId(HttpServletRequest request, HttpServletResponse response) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                return caService.getAllByConsumerId(info.getConsumer().getId());
            }

            @Override
            public String getAttributeName() {
                return "addresses";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                return DtoFetchingUtils.fetchConsumerAddresses((List<ConsumerAddress>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @PostMapping(value = ControllerAction.APP_CONSUMER_ADDRESS_ACTION)
	@Validation(schema = @AppJsonSchema("/schema/consumer_address_create.json"), validators = { ConsumerExistedValidator.class } )
	@Logged
	public ResponseEntity<?> createAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ConsumerAddressForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				ConsumerAddressController.this.doCreateConsumerAddress(request, form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successful");
				
			}
		};
			
		return support.doSupport(request, null, errors, errorsProcessor);
	}
    
    protected ConsumerAddress doCreateConsumerAddress(HttpServletRequest request, ConsumerAddressForm form, RpcResponse rpcResponse, BindingResult errors) {
        ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
    	ConsumerAddress consumerAddress = new ConsumerAddress();
    	consumerAddress.setConsumerId(info.getConsumer().getId());
    	consumerAddress.setName(form.getName());
    	consumerAddress.setAddressType(form.getAddressType());
    	consumerAddress.setMobile(form.getMobile());
    	consumerAddress.setAddress(form.getAddress());
    	consumerAddress.setCityId(form.getCityId());
    	consumerAddress.setDistrictId(form.getDistrictId());
    	consumerAddress.setWardId(form.getWardId());
    	consumerAddress.setUseAsDefault(form.getUseAsDefault());
    	consumerAddress.setStatus(Status.ACTIVE);
    	
    	String fullAddress = this.locationService.generateFullAddress(form.getAddress(), form.getWardId(), form.getDistrictId(), form.getCityId());
		consumerAddress.setFullAddress(fullAddress);
    	
		consumerAddress = this.caService.create(consumerAddress);

		if (Boolean.TRUE.equals(consumerAddress.getUseAsDefault())) {
		    this.caService.setDefault(info.getConsumer().getId(), consumerAddress.getId());
		}
		
		rpcResponse.addAttribute("address", consumerAddress);
		return consumerAddress;
	}

    @GetMapping(value = ControllerAction.APP_CONSUMER_ADDRESSS_SET_DEFAULT_ACTION)
    @Logged
    public ResponseEntity<?> setDefaultAddress(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("id") Integer addressId) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                boolean result = caService.setDefault(info.getConsumer().getId(), addressId);
                if (result) {
                    getRpcResponse().addAttribute("message", "successful");
                } else {
                    errors.reject("Thiết lập thất bại, Địa chỉ không tồn tại hoặc không thuộc về bạn.");
                }
            }
        };
            
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @DeleteMapping(value = ControllerAction.APP_CONSUMER_ADDRESS_DETAIL_ACTION)
    @Logged
    public ResponseEntity<?> deleteAddress(HttpServletRequest request, HttpServletResponse response,
            @PathVariable(value = "id", required = true) Integer addressId) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                ConsumerAddress address = caService.getActivated(addressId);
                if (address != null && address.getConsumerId().equals(info.getConsumer().getId())) {
                    boolean deleteDefault = address.getUseAsDefault();
                    if (deleteDefault) {
                        errors.reject("Xóa địa chỉ thất bại. Địa chỉ mặc định không được phép xóa");
                    } else {
                        address.setStatus(Status.DELETED);
                        address.setUseAsDefault(false);
                        address = caService.updatePartial(address);
                        getRpcResponse().addAttribute("message", "successful");
                    }
                } else {
                    errors.reject("Xóa địa chỉ thất bại. Địa chỉ không tồn tại hoặc không thuộc về bạn.");
                }
            }
        };
            
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @PostMapping(value = ControllerAction.APP_CONSUMER_ADDRESS_DETAIL_ACTION)
    @Logged
    public ResponseEntity<?> updateAddress(HttpServletRequest request, HttpServletResponse response,
            @PathVariable(value = "id", required = true) Integer addressId, @RequestBody ConsumerAddressForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                ConsumerAddress address = caService.getActivated(addressId);
                if (address != null && address.getConsumerId().equals(info.getConsumer().getId())) {
                    address.setName(form.getName());
                    address.setAddressType(form.getAddressType());
                    address.setMobile(form.getMobile());
                    address.setAddress(form.getAddress());
                    address.setCityId(form.getCityId());
                    address.setDistrictId(form.getDistrictId());
                    address.setWardId(form.getWardId());
                    address.setUseAsDefault(form.getUseAsDefault());
                    address.setStatus(Status.ACTIVE);
                    
                    String fullAddress = locationService.generateFullAddress(form.getAddress(), form.getWardId(), form.getDistrictId(), form.getCityId());
                    address.setFullAddress(fullAddress);
                    
                    address = caService.updatePartial(address);

                    if (Boolean.TRUE.equals(address.getUseAsDefault())) {
                        caService.setDefault(info.getConsumer().getId(), address.getId());
                    }
                    getRpcResponse().addAttribute("address", address);
                } else {
                    errors.reject("Thay đổi địa chỉ thất bại. Địa chỉ không tồn tại hoặc không thuộc về bạn.");
                }               
            }
        };
            
        return support.doSupport(request, null, errors, errorsProcessor);
    }
}
