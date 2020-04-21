package engine;

import engine.DAL.TranspoolXmlLoader;
import model.CustomExceptions.TranspoolXmlValidationException;
import model.CustomExceptions.UnsupportedFileTypeException;
import model.Enums.UserAction;
import model.Interfaces.IEngine;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class Engine implements IEngine {

    public static TranspoolManager transpoolManager;

    public Engine() {

    }

    @Override
    public void ReadXmlFile(String path)
            throws FileNotFoundException, UnsupportedFileTypeException,
            JAXBException, TranspoolXmlValidationException {
        TranspoolXmlLoader.Load(path);
    }
}
