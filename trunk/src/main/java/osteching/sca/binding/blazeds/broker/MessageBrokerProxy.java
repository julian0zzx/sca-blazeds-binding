/**
 * 
 */
package osteching.sca.binding.blazeds.broker;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
    
    private static final String FLEX_SERVICES_CONFIG_PATH_PREFIX = "/META-INF/flex/";

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
        ServletConfig mockServletConfig = getMockServletConfig();
        // Get the configuration manager
        ConfigurationManager configManager = loadMessagingConfiguration(mockServletConfig);
        // Load configuration
        MessagingConfiguration config = configManager.getMessagingConfiguration(mockServletConfig);
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

    private ConfigurationManager loadMessagingConfiguration(ServletConfig config) {
        return new FlexConfigurationManager();
    }

    private void setupInternalPathResolver() {
    }
    
    private ServletConfig getMockServletConfig() {
        return new ServletConfig() {

            @Override
            public String getInitParameter(String param) {
                if ("services.configuration.file".equals(param)) {
                    return FLEX_SERVICES_CONFIG_PATH_PREFIX + "services-config.xml";
                }
                return null;
            }

            @Override
            public Enumeration getInitParameterNames() {
                return null;
            }

            @Override
            public ServletContext getServletContext() {
                return new ServletContext() {

                    @Override
                    public Object getAttribute(String name) {
                        return null;
                    }

                    @Override
                    public Enumeration getAttributeNames() {
                        return null;
                    }

                    @Override
                    public ServletContext getContext(String uripath) {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public String getInitParameter(String name) {
                        return null;
                    }

                    @Override
                    public Enumeration getInitParameterNames() {
                        return null;
                    }

                    @Override
                    public int getMajorVersion() {
                        return 0;
                    }

                    @Override
                    public String getMimeType(String file) {
                        return null;
                    }

                    @Override
                    public int getMinorVersion() {
                        return 0;
                    }

                    @Override
                    public RequestDispatcher getNamedDispatcher(String name) {
                        return null;
                    }

                    @Override
                    public String getRealPath(String path) {
                        return null;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String path) {
                        return null;
                    }

                    @Override
                    public URL getResource(String path) throws MalformedURLException {
                        return null;
                    }

                    @Override
                    public InputStream getResourceAsStream(String path) {
                        if (path.startsWith(FLEX_SERVICES_CONFIG_PATH_PREFIX)) {
                            return MessageBrokerProxy.class.getResourceAsStream(path);
                        }
                        return null;
                    }

                    @Override
                    public Set getResourcePaths(String path) {
                        return null;
                    }

                    @Override
                    public String getServerInfo() {
                        return "---BlazeDS-Binding---";
                    }

                    @Override
                    public Servlet getServlet(String name) throws ServletException {
                        return null;
                    }

                    @Override
                    public String getServletContextName() {
                        return null;
                    }

                    @Override
                    public Enumeration getServletNames() {
                        return null;
                    }

                    @Override
                    public Enumeration getServlets() {
                        return null;
                    }

                    @Override
                    public void log(String msg) {
                    }

                    @Override
                    public void log(Exception exception, String msg) {
                    }

                    @Override
                    public void log(String message, Throwable throwable) {
                    }

                    @Override
                    public void removeAttribute(String name) {
                    }

                    @Override
                    public void setAttribute(String name, Object object) {
                    }
                };
            }

            @Override
            public String getServletName() {
                return null;
            }
        };
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
