/**
 * 
 */
package osteching.sca.binding.blazeds.broker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tuscany.sca.assembly.Binding;
import org.apache.tuscany.sca.invocation.InvocationChain;
import org.apache.tuscany.sca.invocation.Invoker;
import org.apache.tuscany.sca.invocation.Message;
import org.apache.tuscany.sca.invocation.MessageFactory;

/**
 * 
 * @author julian0zzx@gmail.com
 * 
 */
public class MessageBrokerServlet extends HttpServlet {
    private Binding binding;
    private List<InvocationChain> invocationChain;
    private MessageFactory messageFactory;

    private static final long serialVersionUID = 724431501960433478L;

    public MessageBrokerServlet(Binding binding, List<InvocationChain> invocationChain,
                    MessageFactory messageFactory) {
        this.binding = binding;
        this.invocationChain = invocationChain;
        this.messageFactory = messageFactory;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
        // MessageBrokerProxy.getInstance().invoke(request, response);

        for (InvocationChain chain : invocationChain) {
            Invoker serviceInvoker = chain.getHeadInvoker();

            Message req = messageFactory.createMessage();
            Message res = serviceInvoker.invoke(req);
            if (res.isFault()) {
                Throwable e = (Throwable) res.getBody();
                ((HttpServletResponse) response).sendError(
                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
            }

            // Write the response from the service implementation to the response
            // output stream
            InputStream is = (InputStream) res.getBody();
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[2048];
            for (;;) {
                int n = is.read(buffer);
                if (n <= 0)
                    break;
                os.write(buffer, 0, n);
            }
            os.flush();
            os.close();
        }
    }

}
