//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.12.10 alle 06:30:57 PM CET 
//


package com.engine.mapper.datamodel;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="Entity" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="Attribute" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                             &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}column use="required""/>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="duration" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute ref="{http://www.webratio.com/2006/WebML/Graph}x use="required""/>
 *                   &lt;attribute ref="{http://www.webratio.com/2006/WebML/Graph}y use="required""/>
 *                   &lt;attribute ref="{http://www.webratio.com/2006/WebML/Graph}hideAttributes"/>
 *                   &lt;attribute name="attributeOrder" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}database use="required""/>
 *                   &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}table use="required""/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Relationship" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="RelationshipRole1">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element ref="{http://www.webratio.com/2006/WebML/Database}JoinColumn"/>
 *                             &lt;/sequence>
 *                             &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="maxCard" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="RelationshipRole2">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element ref="{http://www.webratio.com/2006/WebML/Database}JoinColumn"/>
 *                             &lt;/sequence>
 *                             &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="maxCard" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="sourceEntity" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="targetEntity" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}database use="required""/>
 *                   &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}table use="required""/>
 *                   &lt;attribute ref="{http://www.webratio.com/2006/WebML/Graph}bendpoints"/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element ref="{http://www.webratio.com/2006/WebML/Database}Database"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entityOrRelationshipOrDatabase"
})
@XmlRootElement(name = "DataModel", namespace = "")
public class DataModel {

    @XmlElements({
        @XmlElement(name = "Entity", type = DataModel.Entity.class),
        @XmlElement(name = "Relationship", type = DataModel.Relationship.class),
        @XmlElement(name = "Database", namespace = "http://www.webratio.com/2006/WebML/Database", type = Database.class)
    })
    protected List<Object> entityOrRelationshipOrDatabase;

    /**
     * Gets the value of the entityOrRelationshipOrDatabase property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entityOrRelationshipOrDatabase property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntityOrRelationshipOrDatabase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataModel.Entity }
     * {@link DataModel.Relationship }
     * {@link Database }
     * 
     * 
     */
    public List<Object> getEntityOrRelationshipOrDatabase() {
        if (entityOrRelationshipOrDatabase == null) {
            entityOrRelationshipOrDatabase = new ArrayList<Object>();
        }
        return this.entityOrRelationshipOrDatabase;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Attribute" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}column use="required""/>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="duration" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Graph}x use="required""/>
     *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Graph}y use="required""/>
     *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Graph}hideAttributes"/>
     *       &lt;attribute name="attributeOrder" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}database use="required""/>
     *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}table use="required""/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "attribute"
    })
    public static class Entity {

        @XmlElement(name = "Attribute", required = true)
        protected List<DataModel.Entity.Attribute> attribute;
        @XmlAttribute(name = "id", required = true)
        protected String id;
        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "duration", required = true)
        protected String duration;
        @XmlAttribute(name = "x", namespace = "http://www.webratio.com/2006/WebML/Graph", required = true)
        @XmlSchemaType(name = "unsignedShort")
        protected int x;
        @XmlAttribute(name = "y", namespace = "http://www.webratio.com/2006/WebML/Graph", required = true)
        @XmlSchemaType(name = "unsignedShort")
        protected int y;
        @XmlAttribute(name = "hideAttributes", namespace = "http://www.webratio.com/2006/WebML/Graph")
        protected Boolean hideAttributes;
        @XmlAttribute(name = "attributeOrder", required = true)
        protected String attributeOrder;
        @XmlAttribute(name = "database", namespace = "http://www.webratio.com/2006/WebML/Database", required = true)
        protected String database;
        @XmlAttribute(name = "table", namespace = "http://www.webratio.com/2006/WebML/Database", required = true)
        protected String table;

        /**
         * Gets the value of the attribute property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the attribute property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAttribute().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DataModel.Entity.Attribute }
         * 
         * 
         */
        public List<DataModel.Entity.Attribute> getAttribute() {
            if (attribute == null) {
                attribute = new ArrayList<DataModel.Entity.Attribute>();
            }
            return this.attribute;
        }

        /**
         * Recupera il valore della proprietà id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Imposta il valore della proprietà id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Recupera il valore della proprietà name.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Imposta il valore della proprietà name.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Recupera il valore della proprietà duration.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDuration() {
            return duration;
        }

        /**
         * Imposta il valore della proprietà duration.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDuration(String value) {
            this.duration = value;
        }

        /**
         * Recupera il valore della proprietà x.
         * 
         */
        public int getX() {
            return x;
        }

        /**
         * Imposta il valore della proprietà x.
         * 
         */
        public void setX(int value) {
            this.x = value;
        }

        /**
         * Recupera il valore della proprietà y.
         * 
         */
        public int getY() {
            return y;
        }

        /**
         * Imposta il valore della proprietà y.
         * 
         */
        public void setY(int value) {
            this.y = value;
        }

        /**
         * Recupera il valore della proprietà hideAttributes.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isHideAttributes() {
            return hideAttributes;
        }

        /**
         * Imposta il valore della proprietà hideAttributes.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setHideAttributes(Boolean value) {
            this.hideAttributes = value;
        }

        /**
         * Recupera il valore della proprietà attributeOrder.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAttributeOrder() {
            return attributeOrder;
        }

        /**
         * Imposta il valore della proprietà attributeOrder.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAttributeOrder(String value) {
            this.attributeOrder = value;
        }

        /**
         * Recupera il valore della proprietà database.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatabase() {
            return database;
        }

        /**
         * Imposta il valore della proprietà database.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatabase(String value) {
            this.database = value;
        }

        /**
         * Recupera il valore della proprietà table.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTable() {
            return table;
        }

        /**
         * Imposta il valore della proprietà table.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTable(String value) {
            this.table = value;
        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}column use="required""/>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Attribute {

            @XmlAttribute(name = "name", required = true)
            protected String name;
            @XmlAttribute(name = "id", required = true)
            protected String id;
            @XmlAttribute(name = "type", required = true)
            protected String type;
            @XmlAttribute(name = "key")
            protected Boolean key;
            @XmlAttribute(name = "column", namespace = "http://www.webratio.com/2006/WebML/Database", required = true)
            protected String column;

            /**
             * Recupera il valore della proprietà name.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Imposta il valore della proprietà name.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Recupera il valore della proprietà id.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getId() {
                return id;
            }

            /**
             * Imposta il valore della proprietà id.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setId(String value) {
                this.id = value;
            }

            /**
             * Recupera il valore della proprietà type.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Imposta il valore della proprietà type.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Recupera il valore della proprietà key.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isKey() {
                return key;
            }

            /**
             * Imposta il valore della proprietà key.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setKey(Boolean value) {
                this.key = value;
            }

            /**
             * Recupera il valore della proprietà column.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getColumn() {
                return column;
            }

            /**
             * Imposta il valore della proprietà column.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setColumn(String value) {
                this.column = value;
            }

        }


		

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="RelationshipRole1">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{http://www.webratio.com/2006/WebML/Database}JoinColumn"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="maxCard" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="RelationshipRole2">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{http://www.webratio.com/2006/WebML/Database}JoinColumn"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="maxCard" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="sourceEntity" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="targetEntity" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}database use="required""/>
     *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Database}table use="required""/>
     *       &lt;attribute ref="{http://www.webratio.com/2006/WebML/Graph}bendpoints"/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "relationshipRole1",
        "relationshipRole2"
    })
    public static class Relationship {

        @XmlElement(name = "RelationshipRole1", required = true)
        protected DataModel.Relationship.RelationshipRole1 relationshipRole1;
        @XmlElement(name = "RelationshipRole2", required = true)
        protected DataModel.Relationship.RelationshipRole2 relationshipRole2;
        @XmlAttribute(name = "id", required = true)
        protected String id;
        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "sourceEntity", required = true)
        protected String sourceEntity;
        @XmlAttribute(name = "targetEntity", required = true)
        protected String targetEntity;
        @XmlAttribute(name = "database", namespace = "http://www.webratio.com/2006/WebML/Database", required = true)
        protected String database;
        @XmlAttribute(name = "table", namespace = "http://www.webratio.com/2006/WebML/Database", required = true)
        protected String table;
        @XmlAttribute(name = "bendpoints", namespace = "http://www.webratio.com/2006/WebML/Graph")
        protected String bendpoints;

        /**
         * Recupera il valore della proprietà relationshipRole1.
         * 
         * @return
         *     possible object is
         *     {@link DataModel.Relationship.RelationshipRole1 }
         *     
         */
        public DataModel.Relationship.RelationshipRole1 getRelationshipRole1() {
            return relationshipRole1;
        }

        /**
         * Imposta il valore della proprietà relationshipRole1.
         * 
         * @param value
         *     allowed object is
         *     {@link DataModel.Relationship.RelationshipRole1 }
         *     
         */
        public void setRelationshipRole1(DataModel.Relationship.RelationshipRole1 value) {
            this.relationshipRole1 = value;
        }

        /**
         * Recupera il valore della proprietà relationshipRole2.
         * 
         * @return
         *     possible object is
         *     {@link DataModel.Relationship.RelationshipRole2 }
         *     
         */
        public DataModel.Relationship.RelationshipRole2 getRelationshipRole2() {
            return relationshipRole2;
        }

        /**
         * Imposta il valore della proprietà relationshipRole2.
         * 
         * @param value
         *     allowed object is
         *     {@link DataModel.Relationship.RelationshipRole2 }
         *     
         */
        public void setRelationshipRole2(DataModel.Relationship.RelationshipRole2 value) {
            this.relationshipRole2 = value;
        }

        /**
         * Recupera il valore della proprietà id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Imposta il valore della proprietà id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Recupera il valore della proprietà name.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Imposta il valore della proprietà name.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Recupera il valore della proprietà sourceEntity.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSourceEntity() {
            return sourceEntity;
        }

        /**
         * Imposta il valore della proprietà sourceEntity.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSourceEntity(String value) {
            this.sourceEntity = value;
        }

        /**
         * Recupera il valore della proprietà targetEntity.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTargetEntity() {
            return targetEntity;
        }

        /**
         * Imposta il valore della proprietà targetEntity.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTargetEntity(String value) {
            this.targetEntity = value;
        }

        /**
         * Recupera il valore della proprietà database.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatabase() {
            return database;
        }

        /**
         * Imposta il valore della proprietà database.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatabase(String value) {
            this.database = value;
        }

        /**
         * Recupera il valore della proprietà table.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTable() {
            return table;
        }

        /**
         * Imposta il valore della proprietà table.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTable(String value) {
            this.table = value;
        }

        /**
         * Recupera il valore della proprietà bendpoints.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBendpoints() {
            return bendpoints;
        }

        /**
         * Imposta il valore della proprietà bendpoints.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBendpoints(String value) {
            this.bendpoints = value;
        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{http://www.webratio.com/2006/WebML/Database}JoinColumn"/>
         *       &lt;/sequence>
         *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="maxCard" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "joinColumn"
        })
        public static class RelationshipRole1 {

            @XmlElement(name = "JoinColumn", namespace = "http://www.webratio.com/2006/WebML/Database", required = true)
            protected JoinColumn joinColumn;
            @XmlAttribute(name = "id", required = true)
            protected String id;
            @XmlAttribute(name = "name", required = true)
            protected String name;
            @XmlAttribute(name = "maxCard", required = true)
            protected String maxCard;

            /**
             * Recupera il valore della proprietà joinColumn.
             * 
             * @return
             *     possible object is
             *     {@link JoinColumn }
             *     
             */
            public JoinColumn getJoinColumn() {
                return joinColumn;
            }

            /**
             * Imposta il valore della proprietà joinColumn.
             * 
             * @param value
             *     allowed object is
             *     {@link JoinColumn }
             *     
             */
            public void setJoinColumn(JoinColumn value) {
                this.joinColumn = value;
            }

            /**
             * Recupera il valore della proprietà id.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getId() {
                return id;
            }

            /**
             * Imposta il valore della proprietà id.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setId(String value) {
                this.id = value;
            }

            /**
             * Recupera il valore della proprietà name.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Imposta il valore della proprietà name.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Recupera il valore della proprietà maxCard.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMaxCard() {
                return maxCard;
            }

            /**
             * Imposta il valore della proprietà maxCard.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMaxCard(String value) {
                this.maxCard = value;
            }

        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{http://www.webratio.com/2006/WebML/Database}JoinColumn"/>
         *       &lt;/sequence>
         *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="maxCard" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "joinColumn"
        })
        public static class RelationshipRole2 {

            @XmlElement(name = "JoinColumn", namespace = "http://www.webratio.com/2006/WebML/Database", required = true)
            protected JoinColumn joinColumn;
            @XmlAttribute(name = "id", required = true)
            protected String id;
            @XmlAttribute(name = "name", required = true)
            protected String name;
            @XmlAttribute(name = "maxCard", required = true)
            protected String maxCard;

            /**
             * Recupera il valore della proprietà joinColumn.
             * 
             * @return
             *     possible object is
             *     {@link JoinColumn }
             *     
             */
            public JoinColumn getJoinColumn() {
                return joinColumn;
            }

            /**
             * Imposta il valore della proprietà joinColumn.
             * 
             * @param value
             *     allowed object is
             *     {@link JoinColumn }
             *     
             */
            public void setJoinColumn(JoinColumn value) {
                this.joinColumn = value;
            }

            /**
             * Recupera il valore della proprietà id.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getId() {
                return id;
            }

            /**
             * Imposta il valore della proprietà id.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setId(String value) {
                this.id = value;
            }

            /**
             * Recupera il valore della proprietà name.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Imposta il valore della proprietà name.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Recupera il valore della proprietà maxCard.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMaxCard() {
                return maxCard;
            }

            /**
             * Imposta il valore della proprietà maxCard.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMaxCard(String value) {
                this.maxCard = value;
            }

        }

    }

}
