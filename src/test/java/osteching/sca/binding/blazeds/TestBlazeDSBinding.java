package osteching.sca.binding.blazeds;

import static org.junit.Assert.assertNotNull;

import org.apache.tuscany.sca.host.embedded.SCADomain;
import org.apache.tuscany.sca.node.SCAClient;
import org.apache.tuscany.sca.node.SCANode;
import org.apache.tuscany.sca.node.SCANodeFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestBlazeDSBinding {

    private static SCADomain domain = null;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        domain = SCADomain.newInstance("blazeds.composite");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        domain = null;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIsAssembled() {
        BuddyService service = domain.getService(BuddyService.class, "BlazeDSComponent");
        assertNotNull(service);
    }
    
    @Test
    public void testRequest() {
        
    }
}
