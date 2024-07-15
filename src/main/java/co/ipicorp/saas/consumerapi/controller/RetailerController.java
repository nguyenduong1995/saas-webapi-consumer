/**
 * RetailerController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.consumerapi.form.RetailerSearchForm;
import co.ipicorp.saas.consumerapi.util.ControllerAction;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.dto.RetailerSearchCondition;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.RetailerService;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.form.validator.LimittedForm;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * RetailerController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
@Api(tags = "Retailer APIs", description = "API to search, get detail retailer")
public class RetailerController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private RetailerService rtService;
    
    @Autowired
    private ProductVariationService pvService;

    @GetMapping(value = ControllerAction.APP_RETAILER_SEARCH_ACTION)
    @NoRequiredAuth
    public ResponseEntity<?> search(HttpServletRequest request, HttpServletResponse response, @GetBody RetailerSearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                RetailerSearchCondition condition = this.createSearchCondition(form);

                long count = rtService.count(condition);
                getRpcResponse().addAttribute("count", count);

                List<Retailer> retailers = new LinkedList<>();
                if (count > form.getSegment()) {
                    retailers = rtService.search(condition);
                }

                List<Map<String, Object>> list = new LinkedList<Map<String,Object>>();
                if (CollectionUtils.isNotEmpty(retailers)) {
                    list = retailers.stream().map(r -> {
                        Map<String, Object> rs = new LinkedHashMap<String, Object>();
                        rs.put("id", r.getId());
                        rs.put("name", r.getName());
                        rs.put("address", r.getFullAddress());
                        return rs;
                    }).collect(Collectors.toList());
                }

                getRpcResponse().addAttribute("retailers", list);
            }

            /**
             * @param form
             * @return
             */
            private RetailerSearchCondition createSearchCondition(RetailerSearchForm form) {
                RetailerSearchCondition condition = new RetailerSearchCondition();
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                condition.setLimitSearch(true);
                condition.setStatus(Status.ACTIVE.getValue());
                condition.setCityId(null);
                condition.setKeyword(form.getKeyword());
                condition.setEnableCreatedDate(false);
                return condition;
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @GetMapping(value = ControllerAction.APP_RETAILER_STOCKING_ACTION)
    @NoRequiredAuth
    public ResponseEntity<?> getRetailers(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam("productVariationId") Integer productVariationId ) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                ProductVariation productVariation = pvService.get(productVariationId);
                List<Retailer> retailers = rtService.getRetailerStocking(productVariation.getProductId(), productVariationId);
                List<Map<String, Object>> list = new LinkedList<Map<String,Object>>();
                if (CollectionUtils.isNotEmpty(retailers)) {
                    list = retailers.stream().map(r -> {
                        Map<String, Object> rs = new LinkedHashMap<String, Object>();
                        rs.put("id", r.getId());
                        rs.put("name", r.getName());
                        rs.put("address", r.getFullAddress());
                        return rs;
                    }).collect(Collectors.toList());
                }
                
                getRpcResponse().addAttribute("count", retailers.size());
                getRpcResponse().addAttribute("retailers", list);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
}
