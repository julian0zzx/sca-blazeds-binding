/**
 * 
 */
package osteching.sca.binding.blazeds.broker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import flex.management.MBeanLifecycleManager;
import flex.management.MBeanServerLocatorFactory;
import flex.messaging.FlexContext;
import flex.messaging.MessageBroker;
import flex.messaging.MessageException;
import flex.messaging.config.ConfigurationManager;
import flex.messaging.config.FlexConfigurationManager;
import flex.messaging.config.MessagingConfiguration;
import flex.messaging.endpoints.Endpoint;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.TypeMarshallingContext;

/**
 * This class set up BlaseDS environment. Most logic of this class comes from MessageBrokerServlet.
 * 
 * @author julian0zzx@gmail.com
 */
public final class MessageBrokerProxy {
    
    private static MessageBrokerProxy instance = new MessageBrokerProxy();
    
    private MessageBroker broker;
    
    private MessageBrokerProxy() {
        // allocate static thread local objects
        MessageBroker.createThreadLocalObjects();
        FlexContext.createThreadLocalObjects();
        SerializationContext.createThreadLocalObjects();
        TypeMarshallingContext.createThreadLocalObjects();
    }
    
    public static MessageBrokerProxy getInstance() {
        return instance;
    }
    
    /**
     * Set up BlazeDS environment
     */
    public void init() {
        ClassLoader loader = getClassLoader();
        // Get the configuration manager
        ConfigurationManager configManager = loadMessagingConfiguration();
        // Load configuration
        MessagingConfiguration config = configManager.getMessagingConfiguration(null);
        // omit logging setting
        // Create broker.
        broker = config.createBroker("SCA-BlazeDS-Binding-Broker", loader);

        // Set the servlet config as thread local
        FlexContext.setThreadLocalObjects(null, null, broker, null, null, null);
        setupInternalPathResolver();
        // // Set initial servlet context on broker
        // broker.setInitServletContext(servletConfig.getServletContext());
        // Create endpoints, services, security, and logger on the broker based on configuration
        config.configureBroker(broker);
        broker.start();

        // clear the broker and servlet config as this thread is done
        FlexContext.clearThreadLocalObjects();
    }

    public void destory() {
        if (broker != null) {
            broker.stop();
            if (broker.isManaged()) {
                MBeanLifecycleManager.unregisterRuntimeMBeans(broker);
            }
            // release static thread locals
            destroyThreadLocals();
        }
    }

    /**
     * Delegate to MessageBroker
     */
    public void invoke(HttpServletRequest req, HttpServletResponse res) {
        // Update thread locals
        broker.initThreadLocals();

        // there is no ServletConfig
        FlexContext.setThreadLocalObjects(null, null, broker, req, res, null);

        // omit user check

        String contextPath = req.getContextPath();
        String pathInfo = req.getPathInfo();
        String endpointPath = req.getServletPath();
        if (pathInfo != null) {
            endpointPath = endpointPath + pathInfo;
        }

        Endpoint endpoint = null;
        try {
            endpoint = broker.getEndpoint(endpointPath, contextPath);
        } catch (MessageException me) {
            me.printStackTrace();
            return;
        }
        
        endpoint.service(req, res);
        
        FlexContext.clearThreadLocalObjects();
    }

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    private ConfigurationManager loadMessagingConfiguration() {
        return new FlexConfigurationManager();
    }

    private void setupInternalPathResolver() {
    }
    
    private void destroyThreadLocals() {
        // clear static member variables
        MBeanServerLocatorFactory.clear();
        // Destroy static thread local objects
        MessageBroker.releaseThreadLocalObjects();
        FlexContext.releaseThreadLocalObjects();
        SerializationContext.releaseThreadLocalObjects();
        TypeMarshallingContext.releaseThreadLocalObjects();
    }
}
