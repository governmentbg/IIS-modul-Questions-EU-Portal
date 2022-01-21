//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.02 at 01:52:08 PM EEST 
//


package eu2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for t_legal_procedure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_legal_procedure"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="document_legal" type="{http://publications.europa.eu/resource/core-metadata}t_document_legal"/&gt;
 *         &lt;sequence minOccurs="0"&gt;
 *           &lt;element name="article" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;sequence minOccurs="0"&gt;
 *             &lt;element name="paragraph" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *             &lt;sequence minOccurs="0"&gt;
 *               &lt;element name="alinea" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *               &lt;choice minOccurs="0"&gt;
 *                 &lt;element name="point" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;element name="phrase" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *               &lt;/choice&gt;
 *             &lt;/sequence&gt;
 *           &lt;/sequence&gt;
 *         &lt;/sequence&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://publications.europa.eu/resource/core-metadata}public"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_legal_procedure", namespace = "http://publications.europa.eu/resource/core-metadata", propOrder = {
    "documentLegal",
    "article",
    "paragraph",
    "alinea",
    "point",
    "phrase"
})
public class TLegalProcedure {

    @XmlElement(name = "document_legal", required = true)
    protected TDocumentLegal documentLegal;
    protected String article;
    protected String paragraph;
    protected String alinea;
    protected String point;
    protected String phrase;
    @XmlAttribute(name = "public", namespace = "http://publications.europa.eu/resource/core-metadata")
    protected Boolean _public;

    /**
     * Gets the value of the documentLegal property.
     * 
     * @return
     *     possible object is
     *     {@link TDocumentLegal }
     *     
     */
    public TDocumentLegal getDocumentLegal() {
        return documentLegal;
    }

    /**
     * Sets the value of the documentLegal property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDocumentLegal }
     *     
     */
    public void setDocumentLegal(TDocumentLegal value) {
        this.documentLegal = value;
    }

    /**
     * Gets the value of the article property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticle() {
        return article;
    }

    /**
     * Sets the value of the article property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticle(String value) {
        this.article = value;
    }

    /**
     * Gets the value of the paragraph property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParagraph() {
        return paragraph;
    }

    /**
     * Sets the value of the paragraph property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParagraph(String value) {
        this.paragraph = value;
    }

    /**
     * Gets the value of the alinea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlinea() {
        return alinea;
    }

    /**
     * Sets the value of the alinea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlinea(String value) {
        this.alinea = value;
    }

    /**
     * Gets the value of the point property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoint() {
        return point;
    }

    /**
     * Sets the value of the point property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoint(String value) {
        this.point = value;
    }

    /**
     * Gets the value of the phrase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * Sets the value of the phrase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhrase(String value) {
        this.phrase = value;
    }

    /**
     * Gets the value of the public property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isPublic() {
        if (_public == null) {
            return true;
        } else {
            return _public;
        }
    }

    /**
     * Sets the value of the public property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPublic(Boolean value) {
        this._public = value;
    }

}
