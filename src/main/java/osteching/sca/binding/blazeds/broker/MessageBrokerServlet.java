/**
 * 
 */
package osteching.sca.binding.blazeds.broker;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tuscany.sca.assembly.Binding;
import org.apache.tuscany.sca.invocation.MessageFactory;

/**
 * 
 * @author julian0zzx@gmail.com
 *
 */
public class MessageBrokerServlet extends HttpServlet {
    private Binding binding;
    private MessageFactory messageFactory;

    
    private static final long serialVersionUID = 724431501960433478L;

    public MessageBrokerServlet(Binding binding, MessageFactory messageFactory) {
        this.binding = binding;
        this.messageFactory = messageFactory;
    }
    
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        MessageBrokerProxy.getInstance().invoke(request, response);
    }
    
}
