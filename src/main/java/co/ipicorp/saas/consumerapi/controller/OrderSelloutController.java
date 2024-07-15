/**
 * OrderSelloutController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import co.ipicorp.saas.consumerapi.dto.ProductVariationRewardDto;
import co.ipicorp.saas.consumerapi.form.OrderSelloutForm;
import co.ipicorp.saas.consumerapi.form.OrderSelloutItemForm;
import co.ipicorp.saas.consumerapi.form.OrderSelloutSearchForm;
import co.ipicorp.saas.consumerapi.security.ConsumerSessionInfo;
import co.ipicorp.saas.consumerapi.util.Constants;
import co.ipicorp.saas.consumerapi.util.ControllerAction;
import co.ipicorp.saas.consumerapi.validator.AmountItemRemainValidator;
import co.ipicorp.saas.consumerapi.validator.ConsumerAddressExistedValidator;
import co.ipicorp.saas.core.service.CityService;
import co.ipicorp.saas.core.service.RegionService;
import co.ipicorp.saas.nrms.model.Consumer;
import co.ipicorp.saas.nrms.model.ConsumerAddress;
import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.OrderSelloutItem;
import co.ipicorp.saas.nrms.model.OrderSelloutPromotion;
import co.ipicorp.saas.nrms.model.OrderSelloutPromotionLimitation;
import co.ipicorp.saas.nrms.model.OrderSelloutPromotionLimitationDetailItem;
import co.ipicorp.saas.nrms.model.OrderSelloutPromotionLimitationDetailReward;
import co.ipicorp.saas.nrms.model.OrderSelloutStatus;
import co.ipicorp.saas.nrms.model.ProductResource;
import co.ipicorp.saas.nrms.model.ProductResourceType;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.dto.OrderSelloutSearchCondition;
import co.ipicorp.saas.nrms.service.ConsumerAddressService;
import co.ipicorp.saas.nrms.service.ConsumerService;
import co.ipicorp.saas.nrms.service.OrderSelloutItemService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionLimitationDetailItemService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionLimitationDetailRewardService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionLimitationService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.service.ProductCategoryService;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductService;
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
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.web.dto.OrderSelloutDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.exception.HttpBadRequestException;
import grass.micro.apps.web.util.RequestUtils;

/**
 * OrderSelloutController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
@SuppressWarnings({"unchecked", "unused"})
public class OrderSelloutController extends AbstractPromotionController {
    public static final String ORDER_SELLOUT_PREFIX = "SO";

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private OrderSelloutService orderSelloutService;

    @Autowired
    private OrderSelloutItemService orderSelloutItemService;

    @Autowired
    private UnitService unitService;
    
    @Autowired
    private RetailerService retailerService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ConsumerAddressService consumerAddressService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductVariationService productVariationService;

    @Autowired
    private ProductResourceService productResourceService;

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
    private PromotionLimitationItemRewardProductService promotionLimitationItemRewardProductService;

    @Autowired
    private PromotionStateChangeHistoryService promotionStateChangeHistoryService;
    
    @Autowired
    private OrderSelloutPromotionService osiPromotionService;;

    @Autowired
    private OrderSelloutPromotionLimitationService osiPromotionLimitationService;

    @Autowired
    private OrderSelloutPromotionLimitationDetailItemService osiPromotionLimitationDetailItemService;

    @Autowired
    private OrderSelloutPromotionLimitationDetailRewardService osiPromotionLimitationDetailRewardService;

    @GetMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLOUT_BY_RETAILER_ACTION)
    @NoRequiredAuth
    public ResponseEntity<?> listAllByRetailerId(HttpServletRequest request, HttpServletResponse response, @GetBody OrderSelloutForm form) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return orderSelloutService.getAllByRetailerId(form.getRetailerId());
            }

            @Override
            public String getAttributeName() {
                return "orderSellouts";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                DtoFetchingUtils.setOrderSelloutItemService(orderSelloutItemService);
                return DtoFetchingUtils.fetchOrderSellouts((List<OrderSellout>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLOUT_BY_CONSUMER_ACTION)
    @ResponseBody
    public ResponseEntity<?> listAllByConsumerId(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderSelloutForm form) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return orderSelloutService.getAllByConsumerId(form.getConsumerId());
            }

            @Override
            public String getAttributeName() {
                return "orderSellouts";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                DtoFetchingUtils.setOrderSelloutItemService(orderSelloutItemService);
                return DtoFetchingUtils.fetchOrderSellouts((List<OrderSellout>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @PostMapping(value = ControllerAction.APP_ORDER_SELLOUT_ACTION)
    @Validation(validators = { ConsumerAddressExistedValidator.class, AmountItemRemainValidator.class })
    @Logged
    public ResponseEntity<?> createOrderSellout(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderSelloutForm form,
            BindingResult errors) {

        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                form.setConsumerId(info.getConsumer().getId());
                
                Retailer retailer = retailerService.getActivated(form.getRetailerId());
                if (retailer == null) {
                    throw new HttpBadRequestException();
                }

                Map<Integer, Integer> productsInCartMap = form.getItems().stream()
                        .collect(Collectors.toMap(OrderSelloutItemForm::getProductVariationId, OrderSelloutItemForm::getAmount));
                
                List<Promotion> promotions = getPromotionList(retailer, false);

                List<Integer> productVariationIds = productsInCartMap.keySet().stream().collect(Collectors.toList());
                List<ProductVariationRewardDto> rewards = new LinkedList<ProductVariationRewardDto>();

                getPromotionSuggestionWithProductVariations(promotions, productsInCartMap, productVariationIds, rewards);
                getPromotionSuggestionsOnBill(promotions, productsInCartMap, productVariationIds, rewards);

                double totalDiscount = rewards.stream().map(r -> r.getDiscount()).collect(Collectors.summingDouble(Double::new));

                OrderSellout orderSellout = OrderSelloutController.this.doCreateOrderSellout(form, totalDiscount, getRpcResponse(), (BindingResult) errors);
                
                List<Map<String, Object>> items = rewards.stream().flatMap(reward -> reward.getItems().stream()).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(items)) {
                    List<Map<String, Object>> filteredItems = filterItems(items);
                    getRpcResponse().addAttribute("rewards", filteredItems);
                } else {
                    getRpcResponse().addAttribute("rewards", new LinkedList<>());
                }
                
                doLinkOrderSelloutPromotionRewards(orderSellout, rewards);
                
                DtoFetchingUtils.setRetailerService(retailerService);
                DtoFetchingUtils.setOrderSelloutItemService(orderSelloutItemService);
                DtoFetchingUtils.setOrderSelloutService(orderSelloutService);
                
                List<Map<String, Object>> orderItems = orderSelloutItemService.getItemsByOderSelloutId(orderSellout.getId());
                
                OrderSelloutDto result = DtoFetchingUtils.fetchOrderSellout(orderSellout);
                
                for (Map<String, Object> item : orderItems) {
                    String image = (String) item.get("image");
                    item.put("image", ResourceUrlResolver.getInstance().resolveProductUrl(1, image));
                }

                result.setSelloutItems(orderItems);
                getRpcResponse().addAttribute("orderSellout", result);
            }
        };

        return support.doSupport(request, null, errors, errorsProcessor);
    }

    protected void doLinkOrderSelloutPromotionRewards(OrderSellout order, List<ProductVariationRewardDto> rewards) {
        List<ProductVariationRewardDto> promotionRewardList = getPromotionRewardList(rewards);
        for (ProductVariationRewardDto promotionReward : promotionRewardList) {
            OrderSelloutPromotion osi = this.createOrderSelloutPromotion(order, promotionReward);
            List<ProductVariationRewardDto> promotionGroupRewardList = getPromotionGroupRewardList(promotionReward.getPromotionId(), rewards);
            this.createOrderSelloutPromotionLimitation(osi, order, promotionGroupRewardList);
        }
    }
    
    private List<ProductVariationRewardDto> getPromotionRewardList(List<ProductVariationRewardDto> rewards) {
        Map<Integer, ProductVariationRewardDto> result = new LinkedHashMap<>();

        for (ProductVariationRewardDto reward : rewards) {
            ProductVariationRewardDto dto = result.get(reward.getPromotionId());
            
            if (dto == null) {
                dto = new ProductVariationRewardDto();
                dto.setPromotion(reward.getPromotion());
                result.put(reward.getPromotionId(), dto);
            }

            this.summingReward(rewards, reward, dto);
            int amountInCart = dto.getItemsInGroup().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
            dto.setAmountInCart(amountInCart);
        }

        return new ArrayList<>(result.values());
    }

    /**
     * @param rewards
     * @param source
     * @param target
     */
    protected void summingReward(List<ProductVariationRewardDto> rewards, ProductVariationRewardDto source, ProductVariationRewardDto target) {
        target.setDiscount(target.getDiscount() + source.getDiscount());
        if (source.getItems() != null && !source.getItems().isEmpty()) {
            List<Map<String, Object>> items = new ArrayList<>();
            items.addAll(target.getItems());
            items.addAll(source.getItems());
            List<Map<String, Object>> filteredItems = this.filterItems(items);
            target.setItems(filteredItems);
        }

        if (source.getItemsInGroup() != null && !source.getItemsInGroup().isEmpty()) {
            List<Map<String, Object>> items = new ArrayList<>();
            items.addAll(target.getItemsInGroup());
            items.addAll(source.getItemsInGroup());
            List<Map<String, Object>> filteredItems = this.filterItems(items);
            target.setItemsInGroup(filteredItems);
        }
    }

    private List<ProductVariationRewardDto> getPromotionGroupRewardList(int promotionId, List<ProductVariationRewardDto> rewards) {
        Map<Integer, ProductVariationRewardDto> result = new LinkedHashMap<>();

        for (ProductVariationRewardDto reward : rewards) {
            if (reward.getPromotionId() != promotionId) {
                continue;
            }

            ProductVariationRewardDto dto = result.get(reward.getGroupId());
            if (dto == null) {
                dto = new ProductVariationRewardDto();
                dto.setPromotion(reward.getPromotion());
                dto.setGroupId(reward.getGroupId());
                dto.setPromotionLimitationItem(reward.getPromotionLimitationItem());
                result.put(reward.getGroupId(), dto);
            }

            this.summingReward(rewards, reward, dto);
        }

        return new ArrayList<>(result.values());
    }
    
    private OrderSelloutPromotion createOrderSelloutPromotion(OrderSellout order, ProductVariationRewardDto reward) {
        OrderSelloutPromotion osoPromotion = new OrderSelloutPromotion();
        osoPromotion.setOrderId(order.getId());
        osoPromotion.setOrderCode(order.getOrderCode());
        osoPromotion.setPromotionId(reward.getPromotionId());
        osoPromotion.setConsumerId(order.getConsumerId());
        osoPromotion.setRetailerId(order.getRetailerId());
        osoPromotion.setStatus(Status.ACTIVE);

        osoPromotion.setOrderCost(order.getOrderCost());
        osoPromotion.setDiscount(reward.getDiscount());
        
        int productOnPromotionAmount = 0;
        double productOnPromotionCost = 0.0;
        if (CollectionUtils.isNotEmpty(reward.getItemsInGroup())) {
            productOnPromotionAmount = reward.getItemsInGroup().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
            productOnPromotionCost = reward.getItemsInGroup().stream().map(item -> (Double) item.get("itemValue")).reduce(0.0, (a, b) -> a + b);
        }

        osoPromotion.setProductOnPromotionAmount(productOnPromotionAmount);
        osoPromotion.setProductOnPromotionCost(productOnPromotionCost);

        int rewardAmount = 0;
        double rewardValue = 0.0;
        if (CollectionUtils.isNotEmpty(reward.getItems())) {
            rewardAmount = reward.getItems().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
            rewardValue = reward.getItems().stream().map(item -> (Double) item.get("itemValue")).reduce(0.0, (a, b) -> a + b);
        }

        osoPromotion.setRewardAmount(rewardAmount);
        osoPromotion.setRewardValue(rewardValue);

        // (double) reward.getItems().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b)
        osoPromotion = osiPromotionService.create(osoPromotion);
        return osoPromotion;
    }

    private void createOrderSelloutPromotionLimitation(OrderSelloutPromotion osi, OrderSellout order, List<ProductVariationRewardDto> promotionGroupRewardList) {
        for (ProductVariationRewardDto reward : promotionGroupRewardList) {
            OrderSelloutPromotionLimitation osilPromotion = new OrderSelloutPromotionLimitation();
            osilPromotion.setOrderId(order.getId());
            osilPromotion.setOrderCode(order.getOrderCode());
            osilPromotion.setPromotionId(osi.getPromotionId());
            osilPromotion.setConsumerId(osi.getConsumerId());
            osilPromotion.setRetailerId(order.getRetailerId());
            osilPromotion.setLimitationId(reward.getPromotionLimitationItem().getLimitationId());
            osilPromotion.setLimitationItemId(reward.getPromotionLimitationItem().getId());
            osilPromotion.setProductGroupId(reward.getPromotionLimitationItem().getProductGroupId());
            osilPromotion.setStatus(Status.ACTIVE);

            osilPromotion.setDiscount(reward.getDiscount());
            int productOnPromotionAmount = 0;
            double productOnPromotionCost = 0.0;
            if (CollectionUtils.isNotEmpty(reward.getItemsInGroup())) {
                productOnPromotionAmount = reward.getItemsInGroup().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
                productOnPromotionCost = reward.getItemsInGroup().stream().map(item -> (Double) item.get("itemValue")).reduce(0.0, (a, b) -> a + b);
            }

            osilPromotion.setProductOnPromotionAmount(productOnPromotionAmount);
            osilPromotion.setProductOnPromotionCost(productOnPromotionCost);

            int rewardAmount = 0;
            double rewardValue = 0.0;
            if (CollectionUtils.isNotEmpty(reward.getItems())) {
                rewardAmount = reward.getItems().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
                rewardValue = reward.getItems().stream().map(item -> (Double) item.get("itemValue")).reduce(0.0, (a, b) -> a + b);
            }

            osilPromotion.setRewardAmount(rewardAmount);
            osilPromotion.setRewardValue(rewardValue);

            // (double) reward.getItems().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b)
            osilPromotion = osiPromotionLimitationService.create(osilPromotion);
            this.createOrderSelloutPromotionLimitationDetailItems(osilPromotion, order, reward);
            this.createOrderSelloutPromotionLimitationDetailRewards(osilPromotion, order, reward);
        }
    }

    private void createOrderSelloutPromotionLimitationDetailItems(OrderSelloutPromotionLimitation osilPromotion, OrderSellout order,
            ProductVariationRewardDto reward) {
        if (CollectionUtils.isNotEmpty(reward.getItemsInGroup())) {
            
            double groupDiscount = osilPromotion.getDiscount();
            double groupCost = osilPromotion.getProductOnPromotionCost();
            boolean isLast = false;
            double currentDiscount = 0;
            int index = 0;
            for (Map<String, Object> itemInGroup : reward.getItemsInGroup()) {
                isLast = (++index >= reward.getItemsInGroup().size());
                
                OrderSelloutPromotionLimitationDetailItem detail = new OrderSelloutPromotionLimitationDetailItem();
                detail.setOrderSelloutPromotionLimitationId(osilPromotion.getId());
                detail.setOrderId(osilPromotion.getOrderId());
                detail.setOrderCode(osilPromotion.getOrderCode());
                detail.setPromotionId(osilPromotion.getPromotionId());
                detail.setRetailerId(osilPromotion.getRetailerId());
                detail.setLimitationId(osilPromotion.getLimitationId());
                detail.setLimitationItemId(osilPromotion.getLimitationItemId());
                detail.setProductGroupId(osilPromotion.getProductGroupId());
                detail.setStatus(Status.ACTIVE);
                
                detail.setProductId((Integer) itemInGroup.get("productId"));
                detail.setProductVariationId((Integer) itemInGroup.get("productVariationId"));
                detail.setProductVariationName((String) itemInGroup.get("productVariationName"));
                detail.setSku((String) itemInGroup.get("sku"));
                
                detail.setUnitId((Integer) itemInGroup.get("unitId"));
                detail.setUnitAmount((Integer) itemInGroup.get("unitAmount"));
                detail.setPackingId((Integer) itemInGroup.get("packingId"));
                detail.setPackingAmount((Integer) itemInGroup.get("packingAmount"));
                detail.setPackingExchangeRatio((Integer) itemInGroup.get("packingExchangeRatio"));
                detail.setUnitPrice((Double) itemInGroup.get("price"));
                detail.setPackingPrice((Double) itemInGroup.get("packingPrice"));
                
                detail.setProductOnPromotionAmount(((Integer) itemInGroup.get("amount")));
                detail.setProductOnPromotionCost(((Double) itemInGroup.get("itemValue")));
                if (groupDiscount == 0) {
                    detail.setDiscount(0.0);
                } else {
                    double itemCost = (Double) itemInGroup.get("itemValue");
                    double discount = 0;
                    if (isLast) {
                        discount = groupDiscount - currentDiscount;
                    } else {
                        int percent = (int) Math.round(100 * (itemCost / groupCost));
                        discount = (int) (percent * groupDiscount / 100);
                        currentDiscount += discount;
                    }
                    
                    detail.setDiscount(discount);
                }
                
                detail = this.osiPromotionLimitationDetailItemService.create(detail);
            }
        }
    }

    private void createOrderSelloutPromotionLimitationDetailRewards(OrderSelloutPromotionLimitation osilPromotion, OrderSellout order,
            ProductVariationRewardDto reward) {

        if (CollectionUtils.isNotEmpty(reward.getItems())) {
            for (Map<String, Object> rewardItem : reward.getItems()) {
                OrderSelloutPromotionLimitationDetailReward detail = new OrderSelloutPromotionLimitationDetailReward();
                detail.setOrderSelloutPromotionLimitationId(osilPromotion.getId());
                detail.setOrderId(osilPromotion.getOrderId());
                detail.setOrderCode(osilPromotion.getOrderCode());
                detail.setPromotionId(osilPromotion.getPromotionId());
                detail.setRetailerId(osilPromotion.getRetailerId());
                detail.setLimitationId(osilPromotion.getLimitationId());
                detail.setLimitationItemId(osilPromotion.getLimitationItemId());
                detail.setProductGroupId(osilPromotion.getProductGroupId());
                detail.setStatus(Status.ACTIVE);
                
                detail.setRewardProductId((Integer) rewardItem.get("productId"));
                detail.setRewardProductVariationId((Integer) rewardItem.get("productVariationId"));
                detail.setRewardProductVariationName((String) rewardItem.get("productVariationName"));
                detail.setRewardSku((String) rewardItem.get("sku"));
                detail.setRewardValue(((Double) rewardItem.get("itemValue")));
                detail.setRewardAmount((Integer) rewardItem.get("amount"));
                
                detail.setUnitId((Integer) rewardItem.get("unitId"));
                detail.setUnitAmount((Integer) rewardItem.get("unitAmount"));
                detail.setPackingId((Integer) rewardItem.get("packingId"));
                detail.setPackingAmount((Integer) rewardItem.get("packingAmount"));
                detail.setPackingExchangeRatio((Integer) rewardItem.get("packingExchangeRatio"));
                detail.setUnitPrice((Double) rewardItem.get("price"));
                detail.setPackingPrice((Double) rewardItem.get("packingPrice"));
                
                detail = this.osiPromotionLimitationDetailRewardService.create(detail);
            }
        }
    }

    protected OrderSellout doCreateOrderSellout(OrderSelloutForm form, double totalDiscount, RpcResponse rpcResponse, BindingResult errors) {
        Retailer retailer = this.retailerService.get(form.getRetailerId());
        Consumer consumer = this.consumerService.get(form.getConsumerId());
        ConsumerAddress consAddress = this.consumerAddressService.get(form.getConsumerAddressId());

        OrderSellout orderSellout = new OrderSellout();
        orderSellout.setOrderCode(UUID.randomUUID().toString());
        orderSellout.setRetailerId(form.getRetailerId());
        orderSellout.setRetailerCode(retailer.getRetailerCode());
        orderSellout.setConsumerId(form.getConsumerId());
        orderSellout.setConsumerName(consumer.getFullname());
        orderSellout.setMobile(consumer.getMobile());
        orderSellout.setConsumerAddressId(form.getConsumerAddressId());
        orderSellout.setConsumerAddressText(consAddress.getFullAddress());
        orderSellout.setOrderDate(LocalDateTime.now());
        orderSellout.setOrderStatus(OrderSelloutStatus.NEW.toString());
        orderSellout.setPresentProductImage(getPresentImage(form.getItems()));
        orderSellout.setOrderCost(0.0);
        orderSellout.setShippingFee(0.0);
        orderSellout.setPromotionRedeem(totalDiscount);
        orderSellout.setVatFee(0.0);
        orderSellout.setFinalCost(0.0);
        orderSellout.setExtraData(new LinkedHashMap<String, Object>());
        this.orderSelloutService.create(orderSellout);

        double totalCost = 0.0;
        List<OrderSelloutItem> orderSelloutItems = this.doCreateSelloutItem(form.getItems(), orderSellout);
        if (orderSelloutItems.size() > 0) {
            this.orderSelloutItemService.saveAll(orderSelloutItems);

            for (OrderSelloutItem item : orderSelloutItems) {
                totalCost += item.getTotalCost();
            }

            orderSellout.setOrderCost(totalCost);
            orderSellout.setFinalCost(totalCost - totalDiscount);
        }

        String orderCode = DtoFetchingUtils.generateCode(ORDER_SELLOUT_PREFIX, retailer.getRetailerCode(), retailer.getId(), 8);
        orderSellout.setOrderCode(orderCode);
        this.orderSelloutService.updatePartial(orderSellout);

        return orderSellout;
    }

    private String getPresentImage(List<OrderSelloutItemForm> items) {
        if (CollectionUtils.isEmpty(items)) {
            return "";
        }

        for (OrderSelloutItemForm item : items) {
            List<ProductResource> resources = this.productResourceService.getAllByProductVariationId(item.getProductVariationId(), ProductResourceType.IMAGE);
            if (resources.size() > 0) {
                return resources.get(0).getUri();
            }
        }

        return "";
    }

    private List<OrderSelloutItem> doCreateSelloutItem(List<OrderSelloutItemForm> items, OrderSellout orderSellout) {
        List<OrderSelloutItem> orderSelloutItems = new ArrayList<OrderSelloutItem>();
        if (items == null || items.size() == 0) {
            return orderSelloutItems;
        }

        for (OrderSelloutItemForm item : items) {
            ProductVariation productVariation = this.productVariationService.get(item.getProductVariationId());
            if (productVariation == null)
                continue;
            orderSelloutItems.add(mappingItem(orderSellout, productVariation, item));

        }

        return orderSelloutItems;
    }

    private OrderSelloutItem mappingItem(OrderSellout orderSellout, ProductVariation productVariation, OrderSelloutItemForm item) {
        OrderSelloutItem orderSelloutItem = new OrderSelloutItem();
        orderSelloutItem.setProductId(productVariation.getProductId());
        orderSelloutItem.setProductVariationId(productVariation.getId());
        orderSelloutItem.setSku(productVariation.getSku());
        orderSelloutItem.setSelloutOrderId(orderSellout.getId());
        orderSelloutItem.setSelloutOrderCode(orderSellout.getOrderCode());
        orderSelloutItem.setUnitId(productVariation.getUnitId());
        orderSelloutItem.setUnitPrice(productVariation.getPrice());
        orderSelloutItem.setUnitAmount(item.getAmount());
        orderSelloutItem.setPackingId(productVariation.getPackingId());
        orderSelloutItem.setPackingExchangeRatio(productVariation.getPackingExchangeRatio());
        orderSelloutItem.setPackingPrice(productVariation.getPackingPrice());
        orderSelloutItem.setPackingAmount(2);
        orderSelloutItem.setTotalAmount(item.getAmount());
        orderSelloutItem.setTotalCost(item.getAmount() * productVariation.getPrice());
        orderSelloutItem.setStatus(Status.ACTIVE);

        return orderSelloutItem;
    }

    @GetMapping(value = ControllerAction.APP_ORDER_SELLOUT_SEARCH_ACTION)
    @ResponseBody
    @Logged
    public ResponseEntity<?> search(HttpServletRequest request, HttpServletResponse response, @GetBody OrderSelloutSearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                OrderSelloutSearchCondition condition = createSearchCondition(form, info.getConsumer().getId());

                long count = orderSelloutService.count(condition);
                getRpcResponse().addAttribute("count", count);

                if (count > form.getSegment()) {
                    List<OrderSellout> orders = orderSelloutService.search(condition);
                    DtoFetchingUtils.setOrderSelloutItemService(orderSelloutItemService);
                    DtoFetchingUtils.setProductService(productService);
                    DtoFetchingUtils.setProductVariationService(productVariationService);
                    DtoFetchingUtils.setUnitService(unitService);
                    DtoFetchingUtils.setRetailerService(retailerService);
                    List<OrderSelloutDto> dtos = DtoFetchingUtils.fetchOrderSellouts(orders);
                    for (OrderSelloutDto dto : dtos) {
                        Integer orderId = dto.getId();
                        List<Map<String, Object>> orderItems = orderSelloutItemService.getItemsByOderSelloutId(orderId);
                        for (Map<String, Object> item : orderItems) {
                            String image = (String) item.get("image");
                            item.put("image", ResourceUrlResolver.getInstance().resolveProductUrl(1, image));
                        }
                        dto.setSelloutItems(orderItems);
                    }
                    
                    getRpcResponse().addAttribute("orders", dtos);
                }
            }
            
            private OrderSelloutSearchCondition createSearchCondition(OrderSelloutSearchForm form, Integer consumerId) {
                LocalDate toDate = LocalDate.now();
                LocalDate fromDate = LocalDate.now().minusDays(15);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                if (form.getFromDate() != null && form.getToDate() != null) {
                    try {
                        fromDate = LocalDate.parse(form.getFromDate(), formatter);
                        toDate = LocalDate.parse(form.getToDate(), formatter);
                    } catch (Exception ex) {
                        // do nothing
                    }
                }

                OrderSelloutSearchCondition condition = new OrderSelloutSearchCondition();
                condition.setRetailerId(null);
                condition.setConsumerId(consumerId);
                
                condition.setEnableCreatedDate(true);
                condition.setFromDate(fromDate);
                condition.setToDate(toDate);
                
                condition.setLimitSearch(true);
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                
                condition.setOrderCodeEnabled(StringUtils.isNotBlank(form.getOrderCode()));
                condition.setOrderCode(form.getOrderCode());
                
                return condition;
            }
        };
        
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @GetMapping(value = ControllerAction.APP_ORDER_SELLOUT_DETAIL_REWARD_ACTION)
    @Logged
    public ResponseEntity<?> getOrderRewards(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer orderId) {

        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                getOrderRewards(request, orderId, errors, getRpcResponse());
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    
    protected void getOrderRewards(HttpServletRequest request, Integer orderId, Errors errors, RpcResponse rpcResponse) {
        List<OrderSelloutPromotionLimitationDetailReward> rewards = osiPromotionLimitationDetailRewardService.getByOrderId(orderId);
        
        List<Map<String, Object>> result = new LinkedList<Map<String,Object>>();
        if (CollectionUtils.isNotEmpty(rewards)) {
            List<Integer> pvIds = rewards.stream().map(p -> p.getRewardProductVariationId()).collect(Collectors.toList());
            Map<Integer, String> urls = this.getProductResourceService().getResourceUrlOfProductVariations(pvIds, ProductResourceType.IMAGE);
            
            for (OrderSelloutPromotionLimitationDetailReward reward : rewards) {
                result.add(fetchOrderSelloutPromotionLimitationDetailReward(reward, urls));
            }
        }
        
        rpcResponse.addAttribute("rewards", result);
    }


    private Map<String, Object> fetchOrderSelloutPromotionLimitationDetailReward(OrderSelloutPromotionLimitationDetailReward reward
            , Map<Integer, String> urls) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("productId", reward.getRewardProductId());
        result.put("productVariationId", reward.getRewardProductVariationId());
        result.put("productVariationName", reward.getRewardProductVariationName());
        result.put("amount", reward.getRewardAmount());
        result.put("unitAmount", reward.getUnitAmount());
        result.put("packingAmount", reward.getPackingAmount());
        result.put("packingExchangeRatio", reward.getPackingExchangeRatio());
        result.put("unitId", reward.getUnitId());
        result.put("packingId", reward.getPackingId());
        result.put("price", reward.getUnitPrice());
        result.put("packingPrice", reward.getPackingPrice());
        double itemValue = reward.getUnitPrice() * reward.getUnitAmount() + reward.getPackingPrice() * reward.getPackingAmount();
        result.put("itemValue", itemValue);
        result.put("image", ResourceUrlResolver.getInstance().resolveProductUrl(1, urls.get(reward.getRewardProductVariationId())));
        
        return result;
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
