package de.rwth.imi.flare.requestor;

import de.rwth.imi.flare.api.FlareResource;
import org.hl7.fhir.r4.model.*;

/**
 * FlareResource, represents a single FHIR Resource and it's associated patient
 */
public class FlareResourceImpl implements FlareResource {
    private String patientId;
    private final Resource underlyingFhirResource;

    /**
     * Constructs a FlareResource representing a given FHIR Resource
     * @param resource to base the FlareResource upon
     */
    public FlareResourceImpl(Resource resource){
        this.underlyingFhirResource = resource;
        this.extractId();
    }

    /**
     * Extracts the patientId from the {@link #underlyingFhirResource} into {@link #patientId}
     */
    private void extractId() {
        ResourceType resourceType = this.underlyingFhirResource.getResourceType();
        // TODO: Find a way to do map extractor methods to resource types that doesn't involve a switch statement
        switch (resourceType) {
            case Observation -> this.patientId = extractId((Observation) this.underlyingFhirResource);
            case Patient -> this.patientId = extractId((Patient) this.underlyingFhirResource);
            case Condition -> this.patientId = extractId((Condition) this.underlyingFhirResource);
            case Specimen -> this.patientId = extractId((Specimen) this.underlyingFhirResource);
            case Encounter -> this.patientId = extractId((Encounter) this.underlyingFhirResource);
        }
    }

    private String extractId(Specimen specimen) {
        return specimen.getSubject().getReference().toString();
    }

    private String extractId(Encounter encounter) {
        return encounter.getSubject().getReference().toString();
    }

    private String extractId(Condition condition) {
        return condition.getSubject().getReference().toString();
    }

    private String extractId(Patient patient) {
        return patient.getIdentifierFirstRep().getValue();
    }

    private String extractId(Observation observation) {
        return observation.getSubject().getReference().toString();
    }

    @Override
    public String getPatientId() {
        return this.patientId;
    }
}
