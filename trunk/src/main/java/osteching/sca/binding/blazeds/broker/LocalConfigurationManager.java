package osteching.sca.binding.blazeds.broker;

import javax.servlet.ServletConfig;

import flex.messaging.config.ConfigurationFileResolver;
import flex.messaging.config.ConfigurationManager;
import flex.messaging.config.ConfigurationParser;
import flex.messaging.config.LocalFileResolver;
import flex.messaging.config.MessagingConfiguration;
import flex.messaging.config.XPathServerConfigurationParser;

public class LocalConfigurationManager implements ConfigurationManager {
    private ConfigurationParser parser = null;
    private static final String configurationPath = "/flex/services-config.xml";
    private ConfigurationFileResolver configurationResolver = null;

    @Override
    public MessagingConfiguration getMessagingConfiguration(ServletConfig arg0) {
        MessagingConfiguration config = new MessagingConfiguration();
        parser = getConfigurationParser();
        setupConfigurationPathAndResolver();
        parser.parse(configurationPath, configurationResolver, config);
        return config;
    }

    @Override
    public void reportTokens() {
        // do nothing
    }

    private ConfigurationParser getConfigurationParser() {
        return new XPathServerConfigurationParser();
    }
    
    private void setupConfigurationPathAndResolver() {
        configurationResolver = new LocalFileResolver(LocalFileResolver.SERVER);
    }
}
