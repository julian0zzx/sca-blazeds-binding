package osteching.sca.binding.blazeds.provider;

import javax.servlet.Servlet;

import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.host.http.SecurityContext;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.interfacedef.InterfaceContract;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.provider.OperationSelectorProvider;
import org.apache.tuscany.sca.provider.ProviderFactoryExtensionPoint;
import org.apache.tuscany.sca.provider.ServiceBindingProvider;
import org.apache.tuscany.sca.provider.WireFormatProvider;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;

import osteching.sca.binding.blazeds.BlazeDSBinding;
import osteching.sca.binding.blazeds.broker.MessageBrokerServlet;

public class BlazeDSBindingProvider implements ServiceBindingProvider {
    private ExtensionPointRegistry extensionPoints;

    private RuntimeComponent component;
    private RuntimeComponentService service;
    private InterfaceContract serviceContract;
    private BlazeDSBinding binding;
    private MessageFactory messageFactory;

    private OperationSelectorProvider osProvider;
    private WireFormatProvider wfProvider;

    private ServletHost servletHost;
    private String servletMapping;

    public BlazeDSBindingProvider(RuntimeComponent component, RuntimeComponentService service,
                    BlazeDSBinding binding, ExtensionPointRegistry extensionPoints,
                    MessageFactory messageFactory, ServletHost servletHost) {
        this.component = component;
        this.service = service;

        this.binding = binding;
        this.extensionPoints = extensionPoints;
        this.messageFactory = messageFactory;
        this.servletHost = servletHost;

        // retrieve operation selector and wire format service providers

        ProviderFactoryExtensionPoint providerFactories = extensionPoints
                        .getExtensionPoint(ProviderFactoryExtensionPoint.class);

        // clone the service contract to avoid databinding issues
        try {
            this.serviceContract = (InterfaceContract) service.getInterfaceContract().clone();

            // configure data binding
            if (this.wfProvider != null) {
                wfProvider.configureWireFormatInterfaceContract(service.getInterfaceContract());
            }
        } catch (CloneNotSupportedException e) {
            this.serviceContract = service.getInterfaceContract();
        }

    }

    @Override
    public InterfaceContract getBindingInterfaceContract() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void start() {
        Servlet servlet = new MessageBrokerServlet(binding, messageFactory);
        
        servletMapping = binding.getURI();

        servletHost.addServletMapping(servletMapping, servlet, new SecurityContext());
    }

    @Override
    public void stop() {
        servletHost.removeServletMapping(servletMapping);
    }

    @Override
    public boolean supportsOneWayInvocation() {
        // TODO Auto-generated method stub
        return false;
    }

}
