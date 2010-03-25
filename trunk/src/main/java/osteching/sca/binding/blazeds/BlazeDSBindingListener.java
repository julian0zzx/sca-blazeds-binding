/**
 * 
 */
package osteching.sca.binding.blazeds;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import osteching.sca.binding.blazeds.broker.MessageBrokerProxy;

/**
 * @author julian0zzx@gmail.com
 * 
 */
public class BlazeDSBindingListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        MessageBrokerProxy.getInstance().destory();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        MessageBrokerProxy proxy = MessageBrokerProxy.getInstance();
        proxy.init(event.getServletContext());
    }

}
