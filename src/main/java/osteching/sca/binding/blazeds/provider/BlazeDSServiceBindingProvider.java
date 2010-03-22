package osteching.sca.binding.blazeds.provider;

import javax.servlet.Servlet;

import org.apache.tuscany.sca.host.http.SecurityContext;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.interfacedef.InterfaceContract;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.provider.ServiceBindingProvider;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;

import osteching.sca.binding.blazeds.BlazeDSBinding;
import osteching.sca.binding.blazeds.broker.MessageBrokerServlet;

public class BlazeDSServiceBindingProvider implements ServiceBindingProvider {

    private RuntimeComponent component;
    private RuntimeComponentService service;
    private BlazeDSBinding binding;
    private MessageFactory messageFactory;

    private ServletHost servletHost;
    private String servletMapping;

    BlazeDSServiceBindingProvider(RuntimeComponent component, RuntimeComponentService service,
                    BlazeDSBinding binding, ServletHost servletHost) {
        this.component = component;
        this.service = service;
        this.binding = binding;
        this.servletHost = servletHost;
    }

    @Override
    public InterfaceContract getBindingInterfaceContract() {
        return service.getInterfaceContract();
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
        return false;
    }

}
