package osteching.sca.binding.blazeds.provider;

import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.host.http.ServletHostExtensionPoint;
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

    public BlazeDSBindingProviderFactory(ExtensionPointRegistry extensionPoints) {
        ServletHostExtensionPoint servletHosts = extensionPoints.getExtensionPoint(ServletHostExtensionPoint.class);
        this.servletHost = servletHosts.getServletHosts().get(0);
    }
    
    // no BlazeDS reference till now
    public ReferenceBindingProvider createReferenceBindingProvider(RuntimeComponent arg0,
                    RuntimeComponentReference arg1, BlazeDSBinding arg2) {
        return null;
    }

    public ServiceBindingProvider createServiceBindingProvider(RuntimeComponent component,
                    RuntimeComponentService service, BlazeDSBinding binding) {
        return new BlazeDSServiceBindingProvider(component, service, binding, servletHost);
    }

    public Class<BlazeDSBinding> getModelType() {
        return BlazeDSBinding.class;
    }

}
