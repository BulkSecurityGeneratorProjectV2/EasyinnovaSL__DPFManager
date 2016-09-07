//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.08 at 01:08:28 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.niso;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for flashType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="flashType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Flash did not fire"/>
 *     &lt;enumeration value="Flash fired"/>
 *     &lt;enumeration value="Strobe return light not detected"/>
 *     &lt;enumeration value="Strobe return light detected"/>
 *     &lt;enumeration value="Flash fired, compulsory flash mode"/>
 *     &lt;enumeration value="Flash fired, compulsory flash mode, return light not detected"/>
 *     &lt;enumeration value="Flash fired, compulsory flash mode, return light detected"/>
 *     &lt;enumeration value="Flash did not fire, compulsory flash mode"/>
 *     &lt;enumeration value="Flash did not fire, auto mode"/>
 *     &lt;enumeration value="Flash fired, auto mode"/>
 *     &lt;enumeration value="Flash fired, auto mode, return light not detected"/>
 *     &lt;enumeration value="Flash fired, auto mode, return light detected"/>
 *     &lt;enumeration value="No flash function"/>
 *     &lt;enumeration value="Flash fired, red-eye reduction mode"/>
 *     &lt;enumeration value="Flash fired, red-eye reduction mode, return light not detected"/>
 *     &lt;enumeration value="Flash fired, red-eye reduction mode, return light detected"/>
 *     &lt;enumeration value="Flash fired, compulsory flash mode, red-eye reduction mode"/>
 *     &lt;enumeration value="Flash fired, compulsory flash mode, red-eye reduction mode, return light not detected"/>
 *     &lt;enumeration value="Flash fired, compulsory flash mode, red-eye reduction mode, return light detected"/>
 *     &lt;enumeration value="Flash fired, auto mode, red-eye reduction mode"/>
 *     &lt;enumeration value="Flash fired, auto mode, return light not detected, red-eye reduction mode"/>
 *     &lt;enumeration value="Flash fired, auto mode, return light detected, red-eye reduction mode"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "flashType")
@XmlEnum
public enum FlashType {

    @XmlEnumValue("Flash did not fire")
    FLASH_DID_NOT_FIRE("0000"),
    @XmlEnumValue("Flash fired")
    FLASH_FIRED("0001"),
    @XmlEnumValue("Strobe return light not detected")
    STROBE_RETURN_LIGHT_NOT_DETECTED("0005"),
    @XmlEnumValue("Strobe return light detected")
    STROBE_RETURN_LIGHT_DETECTED("0007"),
    @XmlEnumValue("Flash fired, compulsory flash mode")
    FLASH_FIRED_COMPULSORY_FLASH_MODE("0009"),
    @XmlEnumValue("Flash fired, compulsory flash mode, return light not detected")
    FLASH_FIRED_COMPULSORY_FLASH_MODE_RETURN_LIGHT_NOT_DETECTED("000d"),
    @XmlEnumValue("Flash fired, compulsory flash mode, return light detected")
    FLASH_FIRED_COMPULSORY_FLASH_MODE_RETURN_LIGHT_DETECTED("000f"),
    @XmlEnumValue("Flash did not fire, compulsory flash mode")
    FLASH_DID_NOT_FIRE_COMPULSORY_FLASH_MODE("0010"),
    @XmlEnumValue("Flash did not fire, auto mode")
    FLASH_DID_NOT_FIRE_AUTO_MODE("0018"),
    @XmlEnumValue("Flash fired, auto mode")
    FLASH_FIRED_AUTO_MODE("0019"),
    @XmlEnumValue("Flash fired, auto mode, return light not detected")
    FLASH_FIRED_AUTO_MODE_RETURN_LIGHT_NOT_DETECTED("001d"),
    @XmlEnumValue("Flash fired, auto mode, return light detected")
    FLASH_FIRED_AUTO_MODE_RETURN_LIGHT_DETECTED("001f"),
    @XmlEnumValue("No flash function")
    NO_FLASH_FUNCTION("0020"),
    @XmlEnumValue("Flash fired, red-eye reduction mode")
    FLASH_FIRED_RED_EYE_REDUCTION_MODE("0041"),
    @XmlEnumValue("Flash fired, red-eye reduction mode, return light not detected")
    FLASH_FIRED_RED_EYE_REDUCTION_MODE_RETURN_LIGHT_NOT_DETECTED("0045"),
    @XmlEnumValue("Flash fired, red-eye reduction mode, return light detected")
    FLASH_FIRED_RED_EYE_REDUCTION_MODE_RETURN_LIGHT_DETECTED("0047"),
    @XmlEnumValue("Flash fired, compulsory flash mode, red-eye reduction mode")
    FLASH_FIRED_COMPULSORY_FLASH_MODE_RED_EYE_REDUCTION_MODE("0049"),
    @XmlEnumValue("Flash fired, compulsory flash mode, red-eye reduction mode, return light not detected")
    FLASH_FIRED_COMPULSORY_FLASH_MODE_RED_EYE_REDUCTION_MODE_RETURN_LIGHT_NOT_DETECTED("004d"),
    @XmlEnumValue("Flash fired, compulsory flash mode, red-eye reduction mode, return light detected")
    FLASH_FIRED_COMPULSORY_FLASH_MODE_RED_EYE_REDUCTION_MODE_RETURN_LIGHT_DETECTED("004f"),
    @XmlEnumValue("Flash fired, auto mode, red-eye reduction mode")
    FLASH_FIRED_AUTO_MODE_RED_EYE_REDUCTION_MODE("0059"),
    @XmlEnumValue("Flash fired, auto mode, return light not detected, red-eye reduction mode")
    FLASH_FIRED_AUTO_MODE_RETURN_LIGHT_NOT_DETECTED_RED_EYE_REDUCTION_MODE("005d"),
    @XmlEnumValue("Flash fired, auto mode, return light detected, red-eye reduction mode")
    FLASH_FIRED_AUTO_MODE_RETURN_LIGHT_DETECTED_RED_EYE_REDUCTION_MODE("005f");
    private final String value;

    FlashType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FlashType fromValue(String v) {
        for (FlashType c: FlashType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static boolean verifyTag(String v) {
        for (FlashType c: FlashType.values()) {
            if (c.value.equals(v)) {
                return true;
            }
        }
        return false;
    }

}