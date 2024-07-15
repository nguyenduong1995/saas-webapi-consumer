/**
 * ConsumerUpdateForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ConsumerUpdateForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class ConsumerUpdateForm implements Serializable {
	
	private static final long serialVersionUID = 5870379514453344720L;

	@JsonProperty("fullname")
    private String fullname;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("mobile")
    private String mobile;
    
    @JsonProperty("image")
    private String image;

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
    public String toString() {
        return "ConsumerUpdateForm [fullname=" + fullname + ", email=" + email + ", mobile=" + mobile + ", image=" + image + "]";
    }
	
}
