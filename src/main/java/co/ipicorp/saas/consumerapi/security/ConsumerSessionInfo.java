/**
O * CustomerSessionInfo.java
 * @copyright  Copyright © 2020 Duy Vo
 * @author     duyvk
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.security;

import co.ipicorp.saas.nrms.model.Consumer;

import grass.micro.apps.web.security.SessionInfo;

/**
 * ConsumerSessionInfo. <<< Detail note.
 * 
 * @author hieuvh
 * @access public
 */
public class ConsumerSessionInfo extends SessionInfo {
    
    private static final long serialVersionUID = 7373059896369653720L;
    public static final String APP_CONSUMER_SESSION_ID_KEY = "__a_eu_s_id";
    private Consumer consumer;

    /**
     * get value of <b>consumer</b>.
     * 
     * @return the consumer
     */
    public Consumer getConsumer() {
        return consumer;
    }

    /**
     * Set value to <b>consumer</b>.
     * 
     * @param consumer
     *            the consumer to set
     */
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

}
