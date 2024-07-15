/**
 * PromotionController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import co.ipicorp.saas.consumerapi.dto.ProductVariationRewardDto;
import co.ipicorp.saas.consumerapi.form.ProductInCartForm;
import co.ipicorp.saas.consumerapi.form.PromotionSuggestionForm;
import co.ipicorp.saas.consumerapi.util.ControllerAction;
import co.ipicorp.saas.core.model.City;
import co.ipicorp.saas.core.service.CityService;
import co.ipicorp.saas.core.service.RegionService;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.SubjectType;
import co.ipicorp.saas.nrms.service.ProductCategoryService;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.PromotionConditionFormatService;
import co.ipicorp.saas.nrms.service.PromotionLimitationItemRewardProductService;
import co.ipicorp.saas.nrms.service.PromotionLimitationItemService;
import co.ipicorp.saas.nrms.service.PromotionLimitationService;
import co.ipicorp.saas.nrms.service.PromotionLocationService;
import co.ipicorp.saas.nrms.service.PromotionParticipantRetailerService;
import co.ipicorp.saas.nrms.service.PromotionProductGroupDetailService;
import co.ipicorp.saas.nrms.service.PromotionProductGroupService;
import co.ipicorp.saas.nrms.service.PromotionRewardFormatService;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.nrms.service.PromotionStateChangeHistoryService;
import co.ipicorp.saas.nrms.service.RetailerService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * PromotionController.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class PromotionController extends AbstractPromotionController {
    
    @Autowired
    private ErrorsKeyConverter errorsProcessor;
    
    @Autowired
    private PromotionService promotionService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private CityService cityService;

    @Autowired
    private PromotionLocationService promotionLocationService;

    @Autowired
    private PromotionParticipantRetailerService pprService;

    @Autowired
    private PromotionLimitationService promotionLimitationService;

    @Autowired
    private PromotionLimitationItemService pliService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private PromotionRewardFormatService promotionRewardFormatService;

    @Autowired
    private PromotionConditionFormatService promotionConditionFormatService;

    @Autowired
    private PromotionProductGroupDetailService promotionProductGroupDetailService;

    @Autowired
    private PromotionProductGroupService promotionProductGroupService;

    @Autowired
    private ProductVariationService productVariationService;

    @Autowired
    private ProductResourceService productResourceService;

    @Autowired
    private PromotionLimitationItemRewardProductService promotionLimitationItemRewardProductService;

    @Autowired
    private PromotionStateChangeHistoryService promotionStateChangeHistoryService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private RetailerService retailerService;
    
    @GetMapping(value = ControllerAction.APP_PROMOTION_ACTION)
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> getPromotionList(HttpServletRequest request, HttpServletResponse response, @RequestParam("retailerId") Integer retailerId) {
        AppControllerSupport support = new AppControllerSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                Retailer retailer = retailerService.getActivated(retailerId);
                if (retailer == null) {
                    getRpcResponse().addAttribute("promotions", new LinkedList<>());
                    return;
                }
                
                List<Promotion> promotions = getPromotionList(retailer, true);
                getRpcResponse().addAttribute("promotions", fetchPromotions(promotions));
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @GetMapping(value = ControllerAction.APP_PROMOTION_DETAIL_ACTION)
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> getPromotionDetail(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer promotionId) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                Promotion promotion = promotionService.get(promotionId);
                Map<String, Object> dto = fetchPromotion(promotion);
                getRpcResponse().addAttribute("promotion", dto);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @PostMapping(value = ControllerAction.APP_PROMOTION_CHECKCART_SELLOUT_ACTION)
    @ResponseBody
    @NoRequiredAuth
    public ResponseEntity<?> getPromotionSuggestionDetail(HttpServletRequest request, HttpServletResponse response,
            @RequestBody PromotionSuggestionForm promotionSuggestionForm) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                // find suggestion with promotions
                int retailerId = promotionSuggestionForm.getRetailerId() == null ? -1 : promotionSuggestionForm.getRetailerId();
                Retailer retailer = retailerService.getActivated(retailerId);
                if (retailer == null) {
                    getRpcResponse().addAttribute("promotions", new LinkedList<>());
                    return;
                }
                
                Map<Integer, Integer> productsInCartMap = promotionSuggestionForm.getProductsInCart().stream()
                        .collect(Collectors.toMap(ProductInCartForm::getProductVariationId, ProductInCartForm::getAmount));

                List<Promotion> promotions = getPromotionList(retailer, false);
                List<Integer> productVariationIds = productsInCartMap.keySet().stream().collect(Collectors.toList());
                List<ProductVariationRewardDto> rewards = new LinkedList<ProductVariationRewardDto>();

                getPromotionSuggestionWithProductVariations(promotions, productsInCartMap, productVariationIds, rewards);
                getPromotionSuggestionsOnBill(promotions, productsInCartMap, productVariationIds, rewards);

                double totalDiscount = rewards.stream().map(r -> r.getDiscount()).collect(Collectors.summingDouble(Double::new));
                List<Map<String, Object>> items = rewards.stream().flatMap(reward -> reward.getItems().stream()).collect(Collectors.toList());
                List<Map<String, Object>> result = filterItems(items);
                
                getRpcResponse().addAttribute("discount", totalDiscount);
                getRpcResponse().addAttribute("items", result);
                if (CollectionUtils.isEmpty(rewards)) {
                    getRpcResponse().addAttribute("message", "No promotion available.");
                } else {
                    getRpcResponse().addAttribute("message", "Have promotions");
                }
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @Override
    public CityService getCityService() {
        return this.cityService;
    }

    @Override
    public PromotionService getPromotionService() {
        return this.promotionService;
    }

    @Override
    public PromotionLimitationItemService getPromotionLimitationItemService() {
        return this.pliService;
    }

    @Override
    public ProductVariationService getProductVariationService() {
        return this.productVariationService;
    }

    @Override
    public PromotionProductGroupDetailService getPromotionProductGroupDetailService() {
        return this.promotionProductGroupDetailService;
    }

    @Override
    public     PromotionLimitationItemRewardProductService getPromotionLimitationItemRewardProductService() {
        return this.promotionLimitationItemRewardProductService;
    }

    @Override
    public ProductResourceService getProductResourceService() {
        return this.productResourceService;
    }
    
    @Override
    public RetailerService getRetailerService() {
        return this.retailerService;
    }
}
