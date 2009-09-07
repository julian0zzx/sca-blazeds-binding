package osteching.sca.binding.blazeds.impl;

import osteching.sca.binding.blazeds.BlazeDSBinding;
import osteching.sca.binding.blazeds.BlazeDSBindingFactory;

public class BlazeDSBindingFactoryImpl implements BlazeDSBindingFactory {
    public BlazeDSBinding createBlazeDSBinding() {
        return new BlazeDSBindingImpl();
    }
}
