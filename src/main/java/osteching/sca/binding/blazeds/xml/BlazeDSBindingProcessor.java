package osteching.sca.binding.blazeds.xml;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.tuscany.sca.assembly.xml.PolicyAttachPointProcessor;
import org.apache.tuscany.sca.contribution.ModelFactoryExtensionPoint;
import org.apache.tuscany.sca.contribution.processor.BaseStAXArtifactProcessor;
import org.apache.tuscany.sca.contribution.processor.StAXArtifactProcessor;
import org.apache.tuscany.sca.contribution.processor.StAXAttributeProcessor;
import org.apache.tuscany.sca.contribution.resolver.ModelResolver;
import org.apache.tuscany.sca.contribution.service.ContributionReadException;
import org.apache.tuscany.sca.contribution.service.ContributionResolveException;
import org.apache.tuscany.sca.contribution.service.ContributionWriteException;
import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.monitor.Monitor;
import org.apache.tuscany.sca.policy.PolicyFactory;

import osteching.sca.binding.blazeds.BlazeDSBinding;
import osteching.sca.binding.blazeds.BlazeDSBindingFactory;

public class BlazeDSBindingProcessor extends BaseStAXArtifactProcessor implements
                StAXArtifactProcessor<BlazeDSBinding> {
    private static final String BINDING_BLAZEDS = "binding.blazeds";
    private static final String NS = "http://osteching.com/sca/binding";
    private static final QName BINDING_BLAZEDS_QNAME = new QName(NS, BINDING_BLAZEDS);

    private static final String NAME = "name";
    private static final String URI = "uri";

    private BlazeDSBindingFactory blazeDSBindingFactory;
    private StAXArtifactProcessor<Object> extensionProcessor;
    private StAXAttributeProcessor<Object> extensionAttributeProcessor;
    private PolicyFactory policyFactory;
    private PolicyAttachPointProcessor policyProcessor;
    private Monitor monitor;
    
    public BlazeDSBindingProcessor(ExtensionPointRegistry extensionPoints,
                    StAXArtifactProcessor extensionProcessor,
                    StAXAttributeProcessor extensionAttributeProcessor, Monitor monitor) {
        ModelFactoryExtensionPoint modelFactories = extensionPoints
                        .getExtensionPoint(ModelFactoryExtensionPoint.class);
        this.blazeDSBindingFactory = modelFactories.getFactory(BlazeDSBindingFactory.class);
        this.extensionProcessor = (StAXArtifactProcessor<Object>) extensionProcessor;
        this.extensionAttributeProcessor = extensionAttributeProcessor;
        this.policyFactory = modelFactories.getFactory(PolicyFactory.class);
        this.policyProcessor = new PolicyAttachPointProcessor(policyFactory);
        this.monitor = monitor;
    }

    @Override
    public QName getArtifactType() {
        return BINDING_BLAZEDS_QNAME;
    }

    @Override
    public Class<BlazeDSBinding> getModelType() {
        return BlazeDSBinding.class;
    }

    @Override
    public BlazeDSBinding read(XMLStreamReader reader) throws ContributionReadException,
                    XMLStreamException {
        BlazeDSBinding binding = this.blazeDSBindingFactory.createBlazeDSBinding();
        // Read policies
        policyProcessor.readPolicies(binding, reader);
        
        while(reader.hasNext()) {
            QName elementName = null;
            int event = reader.getEventType();
            switch (event) {
                case START_ELEMENT:
                    elementName = reader.getName();
                     if (BINDING_BLAZEDS_QNAME.equals(elementName)) {
                         String name = getString(reader, NAME);
                         if(name != null) {
                             binding.setName(name);
                         }
                         
                         String uri = getString(reader, URI);
                         if (uri != null) {
                             binding.setURI(uri);
                         }
                     }
            }
            
            if (event == END_ELEMENT && BINDING_BLAZEDS_QNAME.equals(reader.getName())) {
                break;
            }
            
            // Read the next element
            if (reader.hasNext()) {
                reader.next();
            }
        }

        return binding;
    }

    @Override
    public void write(BlazeDSBinding binding, XMLStreamWriter writer) throws ContributionWriteException,
                    XMLStreamException {
        writeStart(writer, BINDING_BLAZEDS_QNAME.getNamespaceURI(), BINDING_BLAZEDS_QNAME.getLocalPart());

        //write policies
        policyProcessor.writePolicyAttributes(binding, writer);
        
        if (binding.getName() != null) {
            writer.writeAttribute(NAME, binding.getName());
        }

        if (binding.getURI() != null) {
            writer.writeAttribute(URI, binding.getURI());
        }
        writeEnd(writer);
    }

    @Override
    public void resolve(BlazeDSBinding binding, ModelResolver resolver)
                    throws ContributionResolveException {
        extensionAttributeProcessor.resolve(binding, resolver);
    }

}
