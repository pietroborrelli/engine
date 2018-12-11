//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.09.17 alle 10:02:25 PM CEST 
//


package com.engine.mapper.datamodel.legacy;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


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
 *         &lt;element name="Database" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="url" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="schema" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Entity" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Attribute" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="javaType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="OutgoingRoles" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="InverseRoles" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="className" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="database" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Role" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="inverseId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="target" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="maxCard" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="derived" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="database" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="direct" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="packageName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="volatileKeyPolicy" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="useQualifiedEntities" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "database",
    "entity",
    "role"
})
@XmlRootElement(name = "DataModel")
public class DataModel {

    @XmlElement(name = "Database")
    protected List<DataModel.Database> database;
    @XmlElement(name = "Entity")
    protected List<DataModel.Entity> entity;
    @XmlElement(name = "Role")
    protected List<DataModel.Role> role;
    @XmlAttribute(name = "packageName")
    protected String packageName;
    @XmlAttribute(name = "volatileKeyPolicy")
    protected String volatileKeyPolicy;
    @XmlAttribute(name = "useQualifiedEntities")
    protected String useQualifiedEntities;

    /**
     * Gets the value of the database property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the database property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatabase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataModel.Database }
     * 
     * 
     */
    public List<DataModel.Database> getDatabase() {
        if (database == null) {
            database = new ArrayList<DataModel.Database>();
        }
        return this.database;
    }

    /**
     * Gets the value of the entity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataModel.Entity }
     * 
     * 
     */
    public List<DataModel.Entity> getEntity() {
        if (entity == null) {
            entity = new ArrayList<DataModel.Entity>();
        }
        return this.entity;
    }

    /**
     * Gets the value of the role property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the role property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataModel.Role }
     * 
     * 
     */
    public List<DataModel.Role> getRole() {
        if (role == null) {
            role = new ArrayList<DataModel.Role>();
        }
        return this.role;
    }

    /**
     * Recupera il valore della proprietà packageName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Imposta il valore della proprietà packageName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageName(String value) {
        this.packageName = value;
    }

    /**
     * Recupera il valore della proprietà volatileKeyPolicy.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolatileKeyPolicy() {
        return volatileKeyPolicy;
    }

    /**
     * Imposta il valore della proprietà volatileKeyPolicy.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolatileKeyPolicy(String value) {
        this.volatileKeyPolicy = value;
    }

    /**
     * Recupera il valore della proprietà useQualifiedEntities.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseQualifiedEntities() {
        return useQualifiedEntities;
    }

    /**
     * Imposta il valore della proprietà useQualifiedEntities.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseQualifiedEntities(String value) {
        this.useQualifiedEntities = value;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="url" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="schema" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Database {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "url")
        protected String url;
        @XmlAttribute(name = "schema")
        protected String schema;
        @XmlAttribute(name = "duration")
        protected String duration;

        /**
         * Recupera il valore della proprietà value.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Imposta il valore della proprietà value.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
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
         * Recupera il valore della proprietà url.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUrl() {
            return url;
        }

        /**
         * Imposta il valore della proprietà url.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUrl(String value) {
            this.url = value;
        }

        /**
         * Recupera il valore della proprietà schema.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSchema() {
            return schema;
        }

        /**
         * Imposta il valore della proprietà schema.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSchema(String value) {
            this.schema = value;
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
     *         &lt;element name="Attribute" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="javaType" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="OutgoingRoles" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="InverseRoles" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="className" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="database" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "attribute",
        "outgoingRoles",
        "inverseRoles"
    })
    public static class Entity {

        @XmlElement(name = "Attribute")
        protected List<DataModel.Entity.Attribute> attribute;
        @XmlElement(name = "OutgoingRoles", required = true)
        protected String outgoingRoles;
        @XmlElement(name = "InverseRoles", required = true)
        protected String inverseRoles;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "name")
        protected String name;
        @XmlAttribute(name = "className")
        protected String className;
        @XmlAttribute(name = "duration")
        protected String duration;
        @XmlAttribute(name = "database")
        protected String database;

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
         * Recupera il valore della proprietà outgoingRoles.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOutgoingRoles() {
            return outgoingRoles;
        }

        /**
         * Imposta il valore della proprietà outgoingRoles.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOutgoingRoles(String value) {
            this.outgoingRoles = value;
        }

        /**
         * Recupera il valore della proprietà inverseRoles.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInverseRoles() {
            return inverseRoles;
        }

        /**
         * Imposta il valore della proprietà inverseRoles.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInverseRoles(String value) {
            this.inverseRoles = value;
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
         * Recupera il valore della proprietà className.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClassName() {
            return className;
        }

        /**
         * Imposta il valore della proprietà className.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClassName(String value) {
            this.className = value;
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
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="javaType" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class Attribute {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "id")
            protected String id;
            @XmlAttribute(name = "key")
            protected String key;
            @XmlAttribute(name = "name")
            protected String name;
            @XmlAttribute(name = "fieldName")
            protected String fieldName;
            @XmlAttribute(name = "type")
            protected String type;
            @XmlAttribute(name = "javaType")
            protected String javaType;

            /**
             * Recupera il valore della proprietà value.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Imposta il valore della proprietà value.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
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
             * Recupera il valore della proprietà key.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKey() {
                return key;
            }

            /**
             * Imposta il valore della proprietà key.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKey(String value) {
                this.key = value;
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
             * Recupera il valore della proprietà fieldName.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFieldName() {
                return fieldName;
            }

            /**
             * Imposta il valore della proprietà fieldName.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFieldName(String value) {
                this.fieldName = value;
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
             * Recupera il valore della proprietà javaType.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getJavaType() {
                return javaType;
            }

            /**
             * Imposta il valore della proprietà javaType.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setJavaType(String value) {
                this.javaType = value;
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
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="inverseId" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="target" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="maxCard" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="derived" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="database" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="direct" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Role {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "inverseId")
        protected String inverseId;
        @XmlAttribute(name = "source")
        protected String source;
        @XmlAttribute(name = "target")
        protected String target;
        @XmlAttribute(name = "maxCard")
        protected String maxCard;
        @XmlAttribute(name = "fieldName")
        protected String fieldName;
        @XmlAttribute(name = "derived")
        protected String derived;
        @XmlAttribute(name = "database")
        protected String database;
        @XmlAttribute(name = "direct")
        protected String direct;

        /**
         * Recupera il valore della proprietà value.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Imposta il valore della proprietà value.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
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
         * Recupera il valore della proprietà inverseId.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInverseId() {
            return inverseId;
        }

        /**
         * Imposta il valore della proprietà inverseId.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInverseId(String value) {
            this.inverseId = value;
        }

        /**
         * Recupera il valore della proprietà source.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSource() {
            return source;
        }

        /**
         * Imposta il valore della proprietà source.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSource(String value) {
            this.source = value;
        }

        /**
         * Recupera il valore della proprietà target.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTarget() {
            return target;
        }

        /**
         * Imposta il valore della proprietà target.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTarget(String value) {
            this.target = value;
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

        /**
         * Recupera il valore della proprietà fieldName.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFieldName() {
            return fieldName;
        }

        /**
         * Imposta il valore della proprietà fieldName.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFieldName(String value) {
            this.fieldName = value;
        }

        /**
         * Recupera il valore della proprietà derived.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDerived() {
            return derived;
        }

        /**
         * Imposta il valore della proprietà derived.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDerived(String value) {
            this.derived = value;
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
         * Recupera il valore della proprietà direct.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDirect() {
            return direct;
        }

        /**
         * Imposta il valore della proprietà direct.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDirect(String value) {
            this.direct = value;
        }

    }

}
