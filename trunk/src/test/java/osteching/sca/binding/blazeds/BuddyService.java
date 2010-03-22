/**
 * 
 */
package osteching.sca.binding.blazeds;

import java.util.List;

import org.osoa.sca.annotations.Remotable;
import org.osoa.sca.annotations.Service;

/**
 * @author julian0zzx@gmail.com
 * 
 */
@Remotable
@Service
public interface BuddyService {
    public Buddy add(Buddy buddy);

    public List<Buddy> list();
}
