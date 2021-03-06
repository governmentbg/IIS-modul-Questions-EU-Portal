//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.02 at 01:52:08 PM EEST 
//


package eu2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for t_role.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="t_role"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ADDRESSEE"/&gt;
 *     &lt;enumeration value="ADDRESSEE_ACT"/&gt;
 *     &lt;enumeration value="ADDRESSEE_CONSULT"/&gt;
 *     &lt;enumeration value="ADDRESSEE_CONSULT_MANDATORY"/&gt;
 *     &lt;enumeration value="ADDRESSEE_CONSULT_OPTION"/&gt;
 *     &lt;enumeration value="ADDRESSEE_INFO"/&gt;
 *     &lt;enumeration value="ASSOC_CORP"/&gt;
 *     &lt;enumeration value="AUT"/&gt;
 *     &lt;enumeration value="AUTHORI"/&gt;
 *     &lt;enumeration value="AUT_OPIN"/&gt;
 *     &lt;enumeration value="AUT_REPORT"/&gt;
 *     &lt;enumeration value="CHAIR"/&gt;
 *     &lt;enumeration value="CHAIR_CMT"/&gt;
 *     &lt;enumeration value="CHAIR_VICE"/&gt;
 *     &lt;enumeration value="MEDIATOR"/&gt;
 *     &lt;enumeration value="MEMBER"/&gt;
 *     &lt;enumeration value="MEMBER_ALT"/&gt;
 *     &lt;enumeration value="MEMBER_COM"/&gt;
 *     &lt;enumeration value="MEMBER_EP"/&gt;
 *     &lt;enumeration value="MEMBER_REPORT"/&gt;
 *     &lt;enumeration value="OMBUDSMAN"/&gt;
 *     &lt;enumeration value="PRESID"/&gt;
 *     &lt;enumeration value="PRESID_CHAMB"/&gt;
 *     &lt;enumeration value="PRESID_CMT"/&gt;
 *     &lt;enumeration value="PRESID_COM"/&gt;
 *     &lt;enumeration value="PRESID_CONSIL"/&gt;
 *     &lt;enumeration value="PRESID_COR"/&gt;
 *     &lt;enumeration value="PRESID_CURIA"/&gt;
 *     &lt;enumeration value="PRESID_ECA"/&gt;
 *     &lt;enumeration value="PRESID_EP"/&gt;
 *     &lt;enumeration value="PRESID_EURCOU"/&gt;
 *     &lt;enumeration value="PRESID_VICE"/&gt;
 *     &lt;enumeration value="PRESID_VICE_COM"/&gt;
 *     &lt;enumeration value="PRESID_VICE_FIRST"/&gt;
 *     &lt;enumeration value="PUBLISHER"/&gt;
 *     &lt;enumeration value="QUAESTOR"/&gt;
 *     &lt;enumeration value="RAPP"/&gt;
 *     &lt;enumeration value="RAPP_CO"/&gt;
 *     &lt;enumeration value="RAPP_GEN"/&gt;
 *     &lt;enumeration value="RAPP_OPIN"/&gt;
 *     &lt;enumeration value="REPR_GOV"/&gt;
 *     &lt;enumeration value="REPR_HIGH"/&gt;
 *     &lt;enumeration value="REPR_PERM"/&gt;
 *     &lt;enumeration value="RESP_CORP"/&gt;
 *     &lt;enumeration value="RESP_JOINT_CORP"/&gt;
 *     &lt;enumeration value="RESP_JOINT_PERS"/&gt;
 *     &lt;enumeration value="RESP_PERS"/&gt;
 *     &lt;enumeration value="SEC_GEN"/&gt;
 *     &lt;enumeration value="SIGN"/&gt;
 *     &lt;enumeration value="TAKING_OVER"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "t_role", namespace = "http://publications.europa.eu/resource/roles/")
@XmlEnum
public enum TRole {

    ADDRESSEE,
    ADDRESSEE_ACT,
    ADDRESSEE_CONSULT,
    ADDRESSEE_CONSULT_MANDATORY,
    ADDRESSEE_CONSULT_OPTION,
    ADDRESSEE_INFO,
    ASSOC_CORP,
    AUT,
    AUTHORI,
    AUT_OPIN,
    AUT_REPORT,
    CHAIR,
    CHAIR_CMT,
    CHAIR_VICE,
    MEDIATOR,
    MEMBER,
    MEMBER_ALT,
    MEMBER_COM,
    MEMBER_EP,
    MEMBER_REPORT,
    OMBUDSMAN,
    PRESID,
    PRESID_CHAMB,
    PRESID_CMT,
    PRESID_COM,
    PRESID_CONSIL,
    PRESID_COR,
    PRESID_CURIA,
    PRESID_ECA,
    PRESID_EP,
    PRESID_EURCOU,
    PRESID_VICE,
    PRESID_VICE_COM,
    PRESID_VICE_FIRST,
    PUBLISHER,
    QUAESTOR,
    RAPP,
    RAPP_CO,
    RAPP_GEN,
    RAPP_OPIN,
    REPR_GOV,
    REPR_HIGH,
    REPR_PERM,
    RESP_CORP,
    RESP_JOINT_CORP,
    RESP_JOINT_PERS,
    RESP_PERS,
    SEC_GEN,
    SIGN,
    TAKING_OVER;

    public String value() {
        return name();
    }

    public static TRole fromValue(String v) {
        return valueOf(v);
    }

}
