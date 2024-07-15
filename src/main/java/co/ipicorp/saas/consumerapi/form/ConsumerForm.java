/**
 * ConsumerForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ConsumerForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class ConsumerForm implements Serializable {

	private static final long serialVersionUID = 757641660980497026L;

	@JsonProperty("consumerId")
	private Integer consumerId;
	
	@JsonProperty("fullname")
    private String fullname;

    @JsonProperty("gender")
    private Integer gender;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("image")
    private String image;
    
    @DateTimeFormat(iso = ISO.DATE)
    @JsonProperty("birthday")
    private LocalDate birthday;
    
    @JsonProperty("extraData")
    private HashMap<String, Object> extraData;
    
    public Integer getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Integer consumerId) {
		this.consumerId = consumerId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public HashMap<String, Object> getExtraData() {
		return extraData;
	}

	public void setExtraData(HashMap<String, Object> extraData) {
		this.extraData = extraData;
	}

	@Override
    public String toString() {
        return "ConsumerForm [consumerId=" + consumerId + ", fullname=" + fullname + ", gender=" + gender + ", email=" 
        		+ email + ", birthday=" + birthday + ", image=" + image + ", extraData=" + extraData + "]";
    }
	
}
