package osteching.sca.binding.blazeds.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tuscany.sca.policy.Intent;
import org.apache.tuscany.sca.policy.IntentAttachPointType;
import org.apache.tuscany.sca.policy.PolicySet;

import osteching.sca.binding.blazeds.BlazeDSBinding;

public class BlazeDSBindingImpl implements BlazeDSBinding {
    private String name;
    private String uri;
    private String channel;
    
    private List<Intent> requiredIntents = new ArrayList<Intent>();
    private List<PolicySet> policySets = new ArrayList<PolicySet>();
    private List<PolicySet> applicablePolicySets = new ArrayList<PolicySet>();
    private IntentAttachPointType intentAttachPointType = null;

    public String getName() {
        return name;
    }

    public String getURI() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    
    
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isUnresolved() {
        // The binding is always resolved
        return false;
    }

    public void setUnresolved(boolean unresolved) {
        // The binding is always resolved
    }
    
    public List<PolicySet> getPolicySets() {
        return policySets;
    }
    
    public List<Intent> getRequiredIntents() {
        return requiredIntents;
    }

    public IntentAttachPointType getType() {
        return intentAttachPointType;
    }
    
    public void setType(IntentAttachPointType intentAttachPointType) {
        this.intentAttachPointType = intentAttachPointType;
    }

    public void setPolicySets(List<PolicySet> policySets) {
        this.policySets = policySets; 
    }

    public void setRequiredIntents(List<Intent> intents) {
        this.requiredIntents = intents;
    }
    
    public List<PolicySet> getApplicablePolicySets() {
        return applicablePolicySets;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
