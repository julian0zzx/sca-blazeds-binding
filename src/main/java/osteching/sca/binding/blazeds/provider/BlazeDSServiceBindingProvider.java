package osteching.sca.binding.blazeds.provider;

import javax.servlet.Servlet;

import org.apache.tuscany.sca.host.http.SecurityContext;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.interfacedef.InterfaceContract;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.invocation.InvocationChain;
import org.apache.tuscany.sca.invocation.Invoker;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.provider.ServiceBindingProvider;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;
import org.apache.tuscany.sca.runtime.RuntimeWire;

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
        RuntimeComponentService componentService = (RuntimeComponentService) service;
        RuntimeWire wire = componentService.getRuntimeWire(binding);
        Invoker serviceInvoker = null;
        Servlet servlet = new MessageBrokerServlet(binding, wire.getInvocationChains(), messageFactory);
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
