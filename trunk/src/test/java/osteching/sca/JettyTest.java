package osteching.sca;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.xml.XmlConfiguration;
import org.xml.sax.SAXException;

public class JettyTest {

    private static final String serverConfig = 
    		"<Configure id=\"Server\" class=\"org.mortbay.jetty.Server\">" +
    		"    <Set name=\"Connectors\">" +
    		"      <Array type=\"org.mortbay.jetty.Connector\">" +
    		"        <Item>" +
    		"          <New class=\"org.mortbay.jetty.nio.SelectChannelConnector\">" +
    		"            <Set name=\"port\"><SystemProperty name=\"jetty.port\" default=\"8080\"/></Set>" +
    		"          </New>" +
    		"        </Item>" +
    		"      </Array>" +
    		"    </Set>" +
    		"</Configure>";
    
    private static final Server server = new Server(8080); 
    
    @BeforeClass
    public static void beforeClass() {
        server.setHandler(new DefaultHandler());
        try {
//            final XmlConfiguration configuration = new XmlConfiguration(new FileInputStream("./src/test/java/jetty.xml"));//()serverConfig);
            final XmlConfiguration configuration = new XmlConfiguration(serverConfig);
            configuration.configure(server);
            server.start();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Before
    public void before() {
        System.out.println("---------setup------");
    }
    
    @After
    public void after() {
        System.out.println("----after---------");
    }
    
    @AfterClass
    public static void afterClass() {
        server.destroy();
    }

    @Test
    public void test() {
        assertTrue(true);
    }
}
