//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.09.17 alle 10:02:25 PM CEST 
//


package com.engine.mapper.datamodel.legacy;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.polimi.mapper.domainmodel package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.polimi.mapper.domainmodel
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataModel }
     * 
     */
    public DataModel createDataModel() {
        return new DataModel();
    }

    /**
     * Create an instance of {@link DataModel.Entity }
     * 
     */
    public DataModel.Entity createDataModelEntity() {
        return new DataModel.Entity();
    }

    /**
     * Create an instance of {@link DataModel.Database }
     * 
     */
    public DataModel.Database createDataModelDatabase() {
        return new DataModel.Database();
    }

    /**
     * Create an instance of {@link DataModel.Role }
     * 
     */
    public DataModel.Role createDataModelRole() {
        return new DataModel.Role();
    }

    /**
     * Create an instance of {@link DataModel.Entity.Attribute }
     * 
     */
    public DataModel.Entity.Attribute createDataModelEntityAttribute() {
        return new DataModel.Entity.Attribute();
    }

}
