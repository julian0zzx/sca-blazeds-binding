/**
 * 
 */
package osteching.sca.binding.blazeds.broker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import flex.messaging.FlexContext;
import flex.messaging.MessageBroker;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.TypeMarshallingContext;

/**
 * This class set up BlaseDS environment. 
 * Most logic of this class comes from MessageBrokerServlet.
 *  
 * @author julian0zzx@gmail.com
 */
public class MessageBrokerProxy {
    
    static {
        // allocate static thread local objects
        MessageBroker.createThreadLocalObjects();
        FlexContext.createThreadLocalObjects();
        SerializationContext.createThreadLocalObjects();
        TypeMarshallingContext.createThreadLocalObjects();
    }
    
    /**
     * Set up BlazeDS environment
     */
    public void init() {
        ClassLoader loader = this.getClass().getClassLoader();
        if (true) { // useContextClassLoader == true
            loader = Thread.currentThread().getContextClassLoader();
        }
        
    }
    
    public void destory() {
        
    }
    
    /** 
     * Delegate to MessageBroker
     */
    public void invoke(HttpServletRequest req, HttpServletResponse res) {
        
    }
}
