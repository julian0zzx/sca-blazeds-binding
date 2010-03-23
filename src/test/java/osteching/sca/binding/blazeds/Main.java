/**
 * 
 */
package osteching.sca.binding.blazeds;

import java.io.IOException;

import org.apache.tuscany.sca.node.SCAClient;
import org.apache.tuscany.sca.node.SCANode;
import org.apache.tuscany.sca.node.SCANodeFactory;

/**
 * @author julian0zzx@gmail.com
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {

        SCANodeFactory factory = SCANodeFactory.newInstance();
        SCANode node = factory.createSCANodeFromClassLoader("blazeds.composite", Main.class.getClassLoader());
        node.start();
        
        BuddyService buddyService = ((SCAClient)node).getService(BuddyService.class, "BlazeDSComponent");
//        while(true) {}
        System.out.println(buddyService);

    }

}
