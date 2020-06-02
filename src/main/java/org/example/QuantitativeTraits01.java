import java.util.stream.Collectors; 
import java.util.stream.Stream; 
import java.io.*;
import java.nio.file.*;

import com.google.common.collect.*;

import org.phenopackets.schema.v1.*;
import org.phenopackets.schema.v1.core.*;
import org.biohackathon.covid19bh.*;

import com.google.protobuf.util.JsonFormat;

public class QuantitativeTraits01 {

    public static void main(String[] args) {

	Phenopacket co19qt = new Main().covid19qt();
	
	Path path = Paths.get("./out.pb");
	try (OutputStream outputStream = Files.newOutputStream(path)) {	    
	    co19qt.writeTo(outputStream);
	} catch (IOException e) {
	    e.printStackTrace();
	}

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
	    .setType(ontologyClass("http://www.semanticweb.org/clininf/covid19#OWLClass_0000000007", "definite COVID-19"))
	    .build();

	QuantitativeTraitFeature complexTrait = QuantitativeTraitFeature.newBuilder()
	    .setType(ontologyClass("CMO:0000015", "body temperature"))
	    .build();
	    
	Individual proband = Individual.newBuilder()
            .setSex(Sex.FEMALE)
            .setId(PROBAND_ID)
            .setAgeAtCollection(Age.newBuilder().setAge("P27Y3M").build())
            .build();
	
	return Phenopacket.newBuilder()
	    .setSubject(proband)
	    .addPhenotypicFeatures(covid19)
	    .addQuantitativeTraitFeatures(complexTrait)
	    /*
	      .setMetaData(metaData) 
	    */
	    .build();
    }
}

