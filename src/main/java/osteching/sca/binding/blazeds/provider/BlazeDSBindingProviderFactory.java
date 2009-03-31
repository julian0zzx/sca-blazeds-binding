package osteching.sca.binding.blazeds.provider;

import org.apache.tuscany.sca.contribution.ModelFactoryExtensionPoint;
import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.provider.BindingProviderFactory;
import org.apache.tuscany.sca.provider.ReferenceBindingProvider;
import org.apache.tuscany.sca.provider.ServiceBindingProvider;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentReference;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;

import osteching.sca.binding.blazeds.BlazeDSBinding;

public class BlazeDSBindingProviderFactory implements BindingProviderFactory<BlazeDSBinding> {
    private MessageFactory messageFactory;

    public BlazeDSBindingProviderFactory(ExtensionPointRegistry extensionPoints) {
        ModelFactoryExtensionPoint factories = extensionPoints.getExtensionPoint(ModelFactoryExtensionPoint.class);
        this.messageFactory = factories.getFactory(MessageFactory.class);
    }
    
    public ReferenceBindingProvider createReferenceBindingProvider(RuntimeComponent arg0,
                    RuntimeComponentReference arg1, BlazeDSBinding arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    public ServiceBindingProvider createServiceBindingProvider(RuntimeComponent arg0,
                    RuntimeComponentService arg1, BlazeDSBinding arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    public Class<BlazeDSBinding> getModelType() {
        return BlazeDSBinding.class;
    }

}
