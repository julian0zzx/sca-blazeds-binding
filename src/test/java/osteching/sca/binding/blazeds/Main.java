/**
 * 
 */
package osteching.sca.binding.blazeds;

import org.apache.tuscany.sca.node.SCAClient;
import org.apache.tuscany.sca.node.SCANode;
import org.apache.tuscany.sca.node.SCANodeFactory;

/**
 * @author julian0zzx@gmail.com
 *
 */
public class Main {

    public static void main(String[] args) {

        SCANodeFactory factory = SCANodeFactory.newInstance();
        SCANode node = factory.createSCANodeFromClassLoader("blazeds.composite", Main.class.getClassLoader());
        node.start();
        
        BuddyService buddyService = ((SCAClient)node).getService(BuddyService.class, "BlazeDSComponent");
        
        System.out.println(buddyService);

    }

}
