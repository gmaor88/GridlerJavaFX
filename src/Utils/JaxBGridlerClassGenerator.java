package Utils;

import jaxb.GameDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Maor Gershkovitch on 8/17/2016.
 * for JaxB
 */
public class JaxBGridlerClassGenerator {
    public static GameDescriptor FromXmlFileToObject (String i_fileName) throws JAXBException{
        GameDescriptor gameDescriptor = null;
        File file = new File(i_fileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        gameDescriptor = (GameDescriptor) jaxbUnmarshaller.unmarshal(file);

        return gameDescriptor;
    }
}
