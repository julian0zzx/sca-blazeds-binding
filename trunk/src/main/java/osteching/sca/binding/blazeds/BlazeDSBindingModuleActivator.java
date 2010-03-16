/**
 * 
 */
package osteching.sca.binding.blazeds;

import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.core.ModuleActivator;

import osteching.sca.binding.blazeds.broker.MessageBrokerProxy;

/**
 * @author julian0zzx@gmail.com
 *
 */
public class BlazeDSBindingModuleActivator implements ModuleActivator {

//    private ServletHost servletHost = null;
    
    @Override
    public void start(ExtensionPointRegistry registry) {
//        servletHost = new ExtensibleServletHost(registry.getExtensionPoint(ServletHostExtensionPoint.class));
        MessageBrokerProxy proxy = MessageBrokerProxy.getInstance();
        proxy.init();
    }

    @Override
    public void stop(ExtensionPointRegistry registry) {
//        MessageBrokerProxy proxy = registry.getExtensionPoint(MessageBrokerProxy.class);
//        proxy.destory();
//        registry.removeExtensionPoint(proxy);
//        servletHost = null;
    }

}
