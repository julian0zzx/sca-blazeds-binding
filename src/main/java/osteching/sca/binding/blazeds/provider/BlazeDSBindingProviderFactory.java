package osteching.sca.binding.blazeds.provider;

import org.apache.tuscany.sca.contribution.ModelFactoryExtensionPoint;
import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.host.http.ServletHostExtensionPoint;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.provider.BindingProviderFactory;
import org.apache.tuscany.sca.provider.ReferenceBindingProvider;
import org.apache.tuscany.sca.provider.ServiceBindingProvider;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentReference;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;

import osteching.sca.binding.blazeds.BlazeDSBinding;

/**
 * @author julian0zzx@gmail.com
 */
public class BlazeDSBindingProviderFactory implements BindingProviderFactory<BlazeDSBinding> {
    private ServletHost servletHost;
    private MessageFactory messageFactory;

    public BlazeDSBindingProviderFactory(ExtensionPointRegistry extensionPoints) {
        ServletHostExtensionPoint servletHosts = extensionPoints.getExtensionPoint(ServletHostExtensionPoint.class);
        this.servletHost = servletHosts.getServletHosts().get(0);
        ModelFactoryExtensionPoint modelFactories = extensionPoints.getExtensionPoint(ModelFactoryExtensionPoint.class);
        messageFactory = modelFactories.getFactory(MessageFactory.class);
    }
    
    // no BlazeDS reference till now
    public ReferenceBindingProvider createReferenceBindingProvider(RuntimeComponent arg0,
                    RuntimeComponentReference arg1, BlazeDSBinding arg2) {
        return null;
    }

    public ServiceBindingProvider createServiceBindingProvider(RuntimeComponent component,
                    RuntimeComponentService service, BlazeDSBinding binding) {
        return new BlazeDSServiceBindingProvider(component, service, binding, messageFactory, servletHost);
    }

    public Class<BlazeDSBinding> getModelType() {
        return BlazeDSBinding.class;
    }

}
