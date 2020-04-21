package model.Interfaces;

import model.CustomExceptions.TranspoolXmlValidationException;
import model.CustomExceptions.UnsupportedFileTypeException;
import model.Enums.UserAction;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface IEngine {
    void ReadXmlFile(String path)
            throws FileNotFoundException, UnsupportedFileTypeException, JAXBException, TranspoolXmlValidationException;
}
