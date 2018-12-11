//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.12.10 alle 06:30:57 PM CET 
//


package com.engine.mapper.datamodel;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.engine.mapper.datamodel package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.engine.mapper.datamodel
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
     * Create an instance of {@link DataModel.Relationship }
     * 
     */
    public DataModel.Relationship createDataModelRelationship() {
        return new DataModel.Relationship();
    }

    /**
     * Create an instance of {@link DataModel.Entity }
     * 
     */
    public DataModel.Entity createDataModelEntity() {
        return new DataModel.Entity();
    }

    /**
     * Create an instance of {@link Database }
     * 
     */
    public Database createDatabase() {
        return new Database();
    }

    /**
     * Create an instance of {@link JoinColumn }
     * 
     */
    public JoinColumn createJoinColumn() {
        return new JoinColumn();
    }

    /**
     * Create an instance of {@link DataModel.Relationship.RelationshipRole1 }
     * 
     */
    public DataModel.Relationship.RelationshipRole1 createDataModelRelationshipRelationshipRole1() {
        return new DataModel.Relationship.RelationshipRole1();
    }

    /**
     * Create an instance of {@link DataModel.Relationship.RelationshipRole2 }
     * 
     */
    public DataModel.Relationship.RelationshipRole2 createDataModelRelationshipRelationshipRole2() {
        return new DataModel.Relationship.RelationshipRole2();
    }

    /**
     * Create an instance of {@link DataModel.Entity.Attribute }
     * 
     */
    public DataModel.Entity.Attribute createDataModelEntityAttribute() {
        return new DataModel.Entity.Attribute();
    }

}
