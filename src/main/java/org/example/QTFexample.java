import java.util.stream.Collectors; 
import java.util.stream.Stream; 
import java.io.*;
import java.nio.file.*;

import com.google.common.collect.*;

import org.phenopackets.schema.v1.*;
import org.phenopackets.schema.v1.core.*;
import org.biohackathon.covid19bh.*;

import com.google.protobuf.util.JsonFormat;

public class QTFexample {

    public static void main(String[] args) {

	Phenopacket co19qt = new QTFexample().covid19qt();

	/** Serialize to Protobuf */
	
	Path path = Paths.get("./out.pb");
	try (OutputStream outputStream = Files.newOutputStream(path)) {	    
	    co19qt.writeTo(outputStream);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	/** Serialize to JSON */	
	
	try {
	    String jsonString = JsonFormat.printer().includingDefaultValueFields().print(co19qt);
	    System.out.println(jsonString);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /** convenience function to help creating OntologyClass objects. */

    public static OntologyClass ontologyClass(String id, String label) {
	return OntologyClass.newBuilder()
	    .setId(id)
	    .setLabel(label)
	    .build();
    }
    

    public Phenopacket covid19qt() {

	final String PROBAND_ID = "PROBAND#1";
	PhenotypicFeature covid19 = PhenotypicFeature.newBuilder()
	    .setType(ontologyClass("DOID:0080600", "COVID-19"))
	    .build();

	QuantitativeTraitFeature complexTrait = QuantitativeTraitFeature.newBuilder()
	    .setType(ontologyClass("CMO:0000015", "body temperature"))
	    .build();
	    
	Individual proband = Individual.newBuilder()
            .setSex(Sex.FEMALE)
            .setId(PROBAND_ID)
            .setAgeAtCollection(Age.newBuilder().setAge("P27Y3M").build())
            .build();

	MetaData metaData = MetaData.newBuilder()
	    .addResources(Resource.newBuilder()
                      .setId("cmo")
                      .setName("Clinical measurement ontology")
                      .setNamespacePrefix("CMO")
                      .setIriPrefix("http://purl.obolibrary.org/obo/CMO_")
                      .setUrl("http://purl.obolibrary.org/obo/cmo.owl")
                      .setVersion("2019-02-19")
			  .build())
	    .addResources(Resource.newBuilder()
                      .setId("doid")
                      .setName("disease ontology")
                      .setNamespacePrefix("DOID")
                      .setIriPrefix("http://purl.obolibrary.org/obo/DOID_")
                      .setUrl("http://purl.obolibrary.org/obo/doid.owl")
                      .setVersion("2020-05-20")
			  .build())
	    .setCreatedBy("Covid19 Biohackathon Ontology Team")
	    .build();
	
	return Phenopacket.newBuilder()
	    .setSubject(proband)
	    .addPhenotypicFeatures(covid19)
	    .addQuantitativeTraitFeatures(complexTrait)
	    .setMetaData(metaData) 
	    .build();

    }
}

