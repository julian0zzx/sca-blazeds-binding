/**
 * 
 */
package osteching.sca.binding.blazeds;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.core.ModuleActivator;
import org.apache.tuscany.sca.core.UtilityExtensionPoint;
import org.apache.tuscany.sca.host.http.ServletHostExtensionPoint;
import org.apache.tuscany.sca.http.tomcat.TomcatServer;
import org.apache.tuscany.sca.work.WorkScheduler;

import osteching.sca.binding.blazeds.broker.MessageBrokerProxy;

/**
 * @author julian0zzx@gmail.com
 *
 */
public class BlazeDSBindingModuleActivator implements ModuleActivator {
    private TomcatServer server;

    @Override
    public void start(ExtensionPointRegistry registry) {
        ServletHostExtensionPoint servletHosts = registry.getExtensionPoint(ServletHostExtensionPoint.class);
        if (servletHosts.getServletHosts().size() < 1) {
            UtilityExtensionPoint utilities = registry.getExtensionPoint(UtilityExtensionPoint.class);
            final WorkScheduler workScheduler = utilities.getUtility(WorkScheduler.class);
            // Allow privileged access to start MBeans. Requires MBeanPermission in security policy.
            server = AccessController.doPrivileged(new PrivilegedAction<TomcatServer>() {
                public TomcatServer run() {
                    return new TomcatServer(workScheduler);
                 }
            });        
            servletHosts.addServletHost(server);
        }
        // TODO to load resource from /WEB-INF/
        MessageBrokerProxy proxy = MessageBrokerProxy.getInstance();
        proxy.init();
    }

    @Override
    public void stop(ExtensionPointRegistry registry) {
        // Allow privileged access to stop MBeans. Requires MBeanPermission in security policy.
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                if (server != null) {
                    server.stop();
                }
                return null;
            }
        });   
        MessageBrokerProxy proxy = MessageBrokerProxy.getInstance();
        proxy.destory();
        
    }

}
