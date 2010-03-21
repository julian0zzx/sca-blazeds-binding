package osteching.sca.binding.blazeds;

import java.util.ArrayList;
import java.util.List;

public class BuddyServiceImpl implements BuddyService {
    private static List<Buddy> BUDDIES = new ArrayList<Buddy>();
    
    static {
        Buddy bb = new Buddy("bb", "M", 15, "Beijing");
        bb.setId(0);
        BUDDIES.add(bb);
    }
    
    @Override
    public Buddy add(Buddy buddy) {
        buddy.setId(BUDDIES.size());
        BUDDIES.add(buddy);
        return buddy;
    }
    
    @Override
    public List<Buddy> list() {
        return BUDDIES;
    }
    
}
