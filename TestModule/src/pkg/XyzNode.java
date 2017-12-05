package pkg;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

public class XyzNode extends DataNode {

    XyzDataObject dObj;
    List<String> lines;

    public XyzNode(XyzDataObject obj, Children ch, Lookup lookup) {
        super(obj, ch, new XyzNodeLookup(lookup));
        this.dObj = obj;
        loadXML();
        XyzNodeLookup ml = (XyzNodeLookup) this.getLookup();
        ml.getInstanceContent().add(new XyzOpenCookie(lines));
    }

    @Override
    public String getDisplayName() {
        return lines.get(0);
    }

    private void loadXML() {
        try {
            InputStream stream = dObj.getPrimaryFile().getInputStream();
            lines = new ArrayList<String>(dObj.getPrimaryFile().asLines());
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static class XyzOpenCookie implements OpenCookie {

        private final List<String> list;

        public XyzOpenCookie(List<String> list) {
            this.list = list;
        }

        @Override
        public void open() {
            XyzTopComponent stc = new XyzTopComponent(list);
            stc.open();
            stc.requestActive();
        }

    }

    private static class XyzTopComponent extends TopComponent {

        public XyzTopComponent(List<String> list) {
            setName(list.get(0));
            setLayout(new BorderLayout());
            JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            StringBuilder sb = new StringBuilder();
            sb.append("<body><ul>");
            for(String l : list) {
                sb.append("<li>" + l + "</li>");
            }
            sb.append("</ul></body>");
            editorPane.setContentType("text/html");
            editorPane.setText(sb.toString());
            add(new JScrollPane(editorPane), BorderLayout.CENTER);
        }
    }

    private static class XyzNodeLookup extends ProxyLookup {

        AbstractLookup abl;
        InstanceContent ic;

        public XyzNodeLookup(Lookup lookup) {
            ic = new InstanceContent();
            abl = new AbstractLookup(ic);
            setLookups(abl, lookup);
        }

        public InstanceContent getInstanceContent() {
            return ic;
        }
    }

}
