package engine.DAL;

import engine.DAL.transpoolXMLSchema.TransPool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class TranspoolXmlSerializer {
    private final static String JAXB_ANNOTATED_PACKAGE = "engine.DAL.transpoolXMLSchema";

    public static TransPool deserialize(File f) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_ANNOTATED_PACKAGE);
        Unmarshaller umrshl = jc.createUnmarshaller();
        return (TransPool) umrshl.unmarshal(f);
    }

    public static void serialize(File destFile, TransPool xmlRootObj) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_ANNOTATED_PACKAGE);
        Marshaller mrshl = jc.createMarshaller();
        mrshl.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        mrshl.marshal(xmlRootObj, destFile);;
    }
}
