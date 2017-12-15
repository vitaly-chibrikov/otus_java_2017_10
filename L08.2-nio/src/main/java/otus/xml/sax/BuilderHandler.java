package otus.xml.sax;


import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by tully.
 */
public abstract class BuilderHandler extends DefaultHandler {
    public abstract Object build();
}
