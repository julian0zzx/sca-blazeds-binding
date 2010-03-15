package osteching.sca.binding.blazeds;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuddyServiceImpl implements BuddyService {
    final static Logger logger = LoggerFactory.getLogger(BuddyServiceImpl.class);
    private static List<Buddy> BUDDIES = new ArrayList<Buddy>();
    
    static {
        Buddy bb = new Buddy("bb", "M", 15, "Beijing");
        bb.setId(0);
        BUDDIES.add(bb);
    }
    
    @Override
    public Buddy add(Buddy buddy) {
        if (logger.isDebugEnabled()) {
            logger.debug("->-- add buddy into buddy collection - " + buddy);
        }
        buddy.setId(BUDDIES.size());
        BUDDIES.add(buddy);
        return buddy;
    }
    
    @Override
    public List<Buddy> list() {
        if (logger.isDebugEnabled()) {
            logger.debug("->-- get buddies list - size is " + BUDDIES.size());
        }
        return BUDDIES;
    }
    
}
