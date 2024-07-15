/**
 * ConsumerAddressForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import co.ipicorp.saas.nrms.model.AddressType;

import java.io.Serializable;

/**
 * ConsumerAddressForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class ConsumerAddressForm implements Serializable {

	private static final long serialVersionUID = 8506472493544780290L;

    private String name;

    private AddressType addressType;
    
    private String mobile;
    
    private String address;
    
    private Integer wardId;
    
    private Integer cityId;
    
    private Integer districtId;
    
    private Boolean useAsDefault;
	
	public String getName() {
		return name;
	}
	
	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	
	public Boolean getUseAsDefault() {
		return useAsDefault;
	}

	public void setUseAsDefault(Boolean useAsDefault) {
		this.useAsDefault = useAsDefault;
	}

	@Override
    public String toString() {
        return "ConsumerAddressForm [name=" + name + ", addressType=" + addressType + ", mobile=" + mobile + ", address=" + address + ", wardId=" + wardId
                + ", cityId=" + cityId + ", districtId=" + districtId + ", useAsDefault=" + useAsDefault + "]";
    }
	
}
