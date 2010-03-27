/**
 * 
 */
package osteching.sca.binding.blazeds.broker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tuscany.sca.assembly.Binding;
import org.apache.tuscany.sca.core.context.InstanceWrapper;
import org.apache.tuscany.sca.core.scope.ScopeContainer;
import org.apache.tuscany.sca.core.scope.ScopedRuntimeComponent;
import org.apache.tuscany.sca.implementation.java.invocation.JavaImplementationInvoker;
import org.apache.tuscany.sca.invocation.Interceptor;
import org.apache.tuscany.sca.invocation.InvocationChain;
import org.apache.tuscany.sca.invocation.Invoker;
import org.apache.tuscany.sca.invocation.Message;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.runtime.RuntimeComponent;

import flex.messaging.io.MessageDeserializer;
import flex.messaging.io.MessageIOConstants;
import flex.messaging.io.MessageSerializer;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ActionContext;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.AmfMessageDeserializer;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.messages.AcknowledgeMessage;
import flex.messaging.messages.RemotingMessage;

/**
 * 
 * @author julian0zzx@gmail.com
 * 
 */
public class MessageBrokerServlet extends HttpServlet {
    private Binding binding;
    private List<InvocationChain> invocationChain;
    private MessageFactory messageFactory;
    private RuntimeComponent component;

    private static final long serialVersionUID = 724431501960433478L;

    public MessageBrokerServlet(Binding binding, List<InvocationChain> invocationChain,
                    RuntimeComponent component, MessageFactory messageFactory) {
        this.binding = binding;
        this.invocationChain = invocationChain;
        this.component = component;
        this.messageFactory = messageFactory;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
        // MessageBrokerProxy.getInstance().invoke(request, response);
        invokeBlazeDS(request, response);
    }

    private void invokeBlazeDS(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
        ActionContext context = new ActionContext();
        // Create an empty ActionMessage object to hold our response
        ActionMessage actionMessage = new ActionMessage();
        actionMessage.addBody(new MessageBody());
        context.setResponseMessage(actionMessage);
        SerializationContext sc = SerializationContext.getSerializationContext();
        sc.setDeserializerClass(AmfMessageDeserializer.class);
        // Deserialize the input stream into an "ActionMessage" object.
        MessageDeserializer deserializer = sc.newMessageDeserializer();
        // Set up the deserialization context
        InputStream in = request.getInputStream();
        deserializer.initialize(sc, in, null);
        // record the length of the input stream for performance metrics
        context.setDeserializedBytes(request.getContentLength());

        try {
            ActionMessage m = new ActionMessage();
            context.setRequestMessage(m);
            deserializer.readMessage(m, context);

        } catch (Throwable t) {
            // just ignore for test
            t.printStackTrace();
        } finally {
            // Use the same ActionMessage version for the response
            ActionMessage respMsg = context.getResponseMessage();
            respMsg.setVersion(context.getVersion());
        }

        MessageBody inMessage = context.getRequestMessageBody();
        MessageBody outMessage = context.getResponseMessageBody();
        flex.messaging.messages.Message inMes = (flex.messaging.messages.Message) inMessage.getData();

        String operationName = ((RemotingMessage) inMes).getOperation();
        Object[] args = ((RemotingMessage) inMes).getParameters().toArray();

        Object result = delegate2JavaImplementation(operationName, args, response); // use reject to invoke target Class instance
        
        AcknowledgeMessage ack = new AcknowledgeMessage();
        ack.setBody(result);
        ack.setCorrelationId(((RemotingMessage) inMes).getMessageId());
        ack.setClientId(((RemotingMessage) inMes).getClientId());

        // marshel ack
        Object outData = convertToSmallMessage((flex.messaging.messages.Message) ack);

        outMessage.setReplyMethod(MessageIOConstants.RESULT_METHOD);
        outMessage.setData(outData);

        try {
            ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
            MessageSerializer serializer = sc.newMessageSerializer();
            serializer.initialize(sc, outBuffer, null);
            serializer.writeMessage(context.getResponseMessage());

            // keep track of serializes bytes for performance metrics
            context.setSerializedBytes(outBuffer.size());
            context.setResponseOutput(outBuffer);
        } catch (Throwable t) {
            // just ignore for test
            t.printStackTrace();
        }

        ByteArrayOutputStream outBuffer = context.getResponseOutput();
        response.setContentType("application/x-amf");
        response.setContentLength(outBuffer.size());
        outBuffer.writeTo(response.getOutputStream());
        response.flushBuffer();
    }

    @SuppressWarnings("unchecked")
    private Object delegate2JavaImplementation(String methodName, Object[] payload, HttpServletResponse response) throws IOException {
        for (InvocationChain chain : invocationChain) {
            Invoker serviceInvoker = chain.getHeadInvoker();
            ScopeContainer scopeContainer = ((ScopedRuntimeComponent) component)
                            .getScopeContainer();
            try {
                // parameter, contextId, is just simply set as null
                InstanceWrapper wrapper = scopeContainer.getWrapper(null);
                Object instance = wrapper.getInstance();

                // it is a interceptor too
                Interceptor interceptor = ((Interceptor) serviceInvoker);
                JavaImplementationInvoker invoker = (JavaImplementationInvoker) interceptor
                                .getNext();
                // I know there is a field called method on the 'invoker'
                Field field = invoker.getClass().getDeclaredField("method");
                Method method = (Method) field.get(invoker);
                if (methodName.equals(method.getName())) {
                    return method.invoke(instance, payload);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((HttpServletResponse) response).sendError(
                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
            }
        }
        return null;
    }

    private flex.messaging.messages.Message convertToSmallMessage(
                    flex.messaging.messages.Message message) {
        if (message instanceof flex.messaging.messages.SmallMessage) {
            flex.messaging.messages.Message smallMessage = ((flex.messaging.messages.SmallMessage) message)
                            .getSmallMessage();
            if (smallMessage != null)
                message = smallMessage;
        }

        return message;
    }
}
