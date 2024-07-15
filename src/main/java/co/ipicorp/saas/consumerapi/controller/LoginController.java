/**
 * LoginController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import co.ipicorp.saas.consumerapi.form.LoginForm;
import co.ipicorp.saas.consumerapi.security.ConsumerSessionInfo;
import co.ipicorp.saas.consumerapi.util.Constants;
import co.ipicorp.saas.consumerapi.util.ControllerAction;
import co.ipicorp.saas.consumerapi.util.ErrorCode;
import co.ipicorp.saas.nrms.model.Consumer;
import co.ipicorp.saas.nrms.model.ConsumerAddress;
import co.ipicorp.saas.nrms.model.ConsumerAuth;
import co.ipicorp.saas.nrms.model.ConsumerRetailerActionHistory;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.service.ConsumerAddressService;
import co.ipicorp.saas.nrms.service.ConsumerAuthService;
import co.ipicorp.saas.nrms.service.ConsumerLoginHistoryService;
import co.ipicorp.saas.nrms.service.ConsumerRetailerActionHistoryService;
import co.ipicorp.saas.nrms.service.ConsumerService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.AppJsonSchema;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.component.SystemConfiguration;
import grass.micro.apps.util.PropertiesConstants;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.component.SessionPool;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * LoginController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
public class LoginController {

    @Autowired
    private SessionPool<ConsumerSessionInfo> pool;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ConsumerAddressService consumerAddressService;
    
    @Autowired
    private RetailerService rtService;
    
    @Autowired
    private ConsumerAuthService consumerAuthService;

    @Autowired
    private ErrorsKeyConverter errorsProcessor;
    
    @Autowired
    private ConsumerRetailerActionHistoryService actionHistoryService;
    
    @Autowired
    private ConsumerLoginHistoryService loginHistoryService;

    @RequestMapping(value = ControllerAction.APP_LOGIN_ACTION, method = RequestMethod.POST)
    @ResponseBody
    @NoRequiredAuth
    @Validation(schema = @AppJsonSchema("/schema/login.json"))
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                processLogin(request, response, form, errors, getRpcResponse());
            }
        };

        return support.doSupport(request, response, errors, errorsProcessor);
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response, LoginForm loginForm, Errors errors, RpcResponse rpcResponse) {
        if (!errors.hasErrors()) {
            ConsumerAuth auth = LoginController.this.consumerAuthService.login(loginForm.getLoginType(), loginForm.getLoginName(), loginForm.getPassword());
            if (auth != null) {
                Consumer consumer = this.consumerService.get(auth.getConsumerId());
                this.saveSession(request, response, auth, consumer, loginForm, rpcResponse);
                rpcResponse.addAttribute("consumer", this.fetchConsumerInfo(consumer));
                this.getDefaultAddress(rpcResponse, consumer);
                this.loginHistoryService.track(consumer, auth.getMobile(), ConsumerAuth.LOGIN_TYPE_MOBILE);
            } else {
                errors.reject(ErrorCode.APP_1101_ACCOUNT_NOT_EXIST);
            }
        }
    }

    /**
     * @param rpcResponse
     * @param consumer
     */
    private void getDefaultAddress(RpcResponse rpcResponse, Consumer consumer) {
        List<ConsumerAddress> addresses = consumerAddressService.getAllByConsumerId(consumer.getId());
        ConsumerAddress defaultAddress = null; 
                
        if (CollectionUtils.isEmpty(addresses)) {
            addresses = new LinkedList<ConsumerAddress>();
        } else {
            List<ConsumerAddress> defaultAddresses = addresses.stream().filter(add -> add.getUseAsDefault() != null && add.getUseAsDefault()).collect(Collectors.toList());
            defaultAddress = (defaultAddresses.size() > 0) ? defaultAddresses.get(0) : addresses.get(0);
            if (defaultAddress.getUseAsDefault() == null || !defaultAddress.getUseAsDefault()) {
                defaultAddress.setUseAsDefault(true);
                this.consumerAddressService.updatePartial(defaultAddress);
            }
        }
        
        rpcResponse.addAttribute("address", defaultAddress);
    }
    
    /*
     * @param consumer {@link Consumer}
     * @return
     */
    private Map<String, Object> fetchConsumerInfo(Consumer consumer) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", consumer.getId());
        map.put("mobile", consumer.getMobile());
        map.put("name", consumer.getFullname());
        map.put("email", consumer.getEmail());
        map.put("extraData", consumer.getExtraData());
        
        String avatar = consumer.getImage();
        if (StringUtils.isNotBlank(avatar)) {
            avatar = ResourceUrlResolver.getInstance().resolveConsumerUrl(1, avatar);
        } else {
            avatar = "";
        }
        
        map.put("avatar", avatar);
        
        int maxView = SystemConfiguration.getInstance().getInt(Constants.MAX_RETAILER_VIEW_LIST_KEY,
                Constants.MAX_RETAILER_VIEW_LIST);
        
        map.put("maxRetailerViewList", maxView);
        
        List<ConsumerRetailerActionHistory> histories = 
                actionHistoryService.getByConsumerId(consumer.getId(), ConsumerRetailerActionHistory.ACTION_TYPE_ACCESS, maxView);
        
        if (CollectionUtils.isNotEmpty(histories)) {
            List<Integer> ids = histories.stream().map(p -> p.getRetailerId()).collect(Collectors.toList());
            List<Retailer> retailers = rtService.get(ids);
            Map<Integer, Retailer> retailerMap = retailers.stream().collect(Collectors.toMap(r -> r.getId(), r -> r));
            
            List<Map<String, Object>> list = ids.stream().map(id -> {
                Map<String, Object> rs = new LinkedHashMap<String, Object>();
                Retailer retailer = retailerMap.get(id);
                rs.put("id", id);
                rs.put("name", retailer.getName());
                rs.put("address", retailer.getFullAddress());
                return rs;
            }).collect(Collectors.toList());
            
            map.put("retailers", list);
        } else {
            map.put("retailers", new LinkedList<>());
        }
        
        return map;
    }

    /*
     * Build SessionInfo object from Logged-in User and it into Session.
     * 
     * @param request {@link HttpServletRequest}
     * 
     * @param loginUser {@link User}
     */
    private void saveSession(HttpServletRequest request, HttpServletResponse response, ConsumerAuth auth, Consumer consumer, LoginForm loginForm,
            RpcResponse result) {
        
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("loginName", auth.getLoginName());
        map.put("mobile", auth.getMobile());
        map.put("loginType", loginForm.getLoginType());
        
        String sessionId = request.getSession(true).getId();
        map.put("sessionId", sessionId);
        
        int timeout = SystemConfiguration.getInstance().getInt(PropertiesConstants.APP_SESSION_TIMEOUT_KEY, PropertiesConstants.APP_DEFAULT_SESSION_TIMEOUT);
        String branchName = SystemConfiguration.getInstance().getProperty(Constants.APP_DEFAULT_SITE_NAME_KEY);
        String token = SystemUtils.getInstance().createJWT("A_C_" + consumer.getId(), branchName, "EU" + consumer.getId(), map, timeout * 60 * 1000);
        result.addAttribute("token", token);
        ConsumerSessionInfo sessionInfo = new ConsumerSessionInfo();
        sessionInfo.setUsername(auth.getLoginName());
        sessionInfo.setToken(token);
        sessionInfo.setConsumer(consumer);

        this.pool.putSession(auth.getLoginName(), sessionInfo);
        RequestUtils.getInstance().setSessionInfo(request, sessionInfo, Constants.APP_SESSION_INFO_KEY);
    }
}
