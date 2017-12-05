package pkg;

import java.beans.IntrospectionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author geertjanwielenga
 */
public class XyzDataChildFactory extends ChildFactory<String> {

    private final XyzDataObject dObj;

    public XyzDataChildFactory(XyzDataObject dObj) {
        this.dObj = dObj;
    }
    
    @Override
    protected boolean createKeys(List<String> list) {
        try {
            list.addAll(dObj.getPrimaryFile().asLines());
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        BeanNode bn = null;
        try {
            bn = new BeanNode(key);
            bn.setDisplayName(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return bn;
    }
    
}
