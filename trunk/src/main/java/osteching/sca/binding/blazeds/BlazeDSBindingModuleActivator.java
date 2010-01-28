/**
 * 
 */
package osteching.sca.binding.blazeds;

import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.core.ModuleActivator;
import org.apache.tuscany.sca.host.http.ExtensibleServletHost;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.host.http.ServletHostExtensionPoint;

import osteching.sca.binding.blazeds.broker.MessageBrokerProxy;

/**
 * @author julian0zzx@gmail.com
 *
 */
public class BlazeDSBindingModuleActivator implements ModuleActivator {

    private ServletHost servletHost;
    
    @Override
    public void start(ExtensionPointRegistry registry) {
        MessageBrokerProxy proxy = new MessageBrokerProxy();
        proxy.init();
        servletHost = new ExtensibleServletHost(registry.getExtensionPoint(ServletHostExtensionPoint.class));
    }

    @Override
    public void stop(ExtensionPointRegistry registry) {
        MessageBrokerProxy proxy = registry.getExtensionPoint(MessageBrokerProxy.class);
        proxy.destory();
        registry.removeExtensionPoint(proxy);
    }

}
