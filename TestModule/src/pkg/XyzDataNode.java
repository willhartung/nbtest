package pkg;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.util.Set;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.actions.OpenAction;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author geertjanwielenga
 */
public class XyzDataNode extends DataNode {
    
    @StaticResource
    private static final String ICON = "pkg/favicon.png";

    public XyzDataNode(XyzDataObject xyzData) throws IntrospectionException {
        this(xyzData, new InstanceContent());
    }

    private XyzDataNode(final XyzDataObject xyz, InstanceContent ic) throws IntrospectionException {
        super(xyz, Children.create(new XyzDataChildFactory(xyz), true), new AbstractLookup(ic));
        ic.add(new OpenCookie() {
            @Override
            public void open() {
                TopComponent tc = findTopComponent(xyz);
                if (tc == null) {
                    tc = new XyzTopComponent(xyz);
                    tc.open();
                }
                tc.requestActive();
            }
        });
        setDisplayName(xyz.getPrimaryFile().getNameExt());
    }

    private TopComponent findTopComponent(XyzDataObject xyz) {
        Set<TopComponent> openTopComponents = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : openTopComponents) {
            if (tc.getLookup().lookup(XyzDataObject.class) == xyz) {
                return tc;
            }
        }
        return null;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(ICON);
    }
    
    @Override
    public Image getOpenedIcon(int type) {
        return ImageUtilities.loadImage(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{SystemAction.get(OpenAction.class)};
    }
}
