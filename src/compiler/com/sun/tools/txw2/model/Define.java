package com.sun.tools.txw2.model;

import com.sun.codemodel.JDefinedClass;
import com.sun.tools.txw2.model.prop.Prop;
import com.sun.xml.txw2.TypedXmlWriter;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Kohsuke Kawaguchi
 */
public final class Define extends WriterNode {
    public final Grammar scope;
    public final String name;

    JDefinedClass clazz;

    public Define(Grammar scope, String name) {
        super(null,null);
        this.scope = scope;
        this.name = name;
    }

    /**
     * Returns true if this define only contains
     * one child (and thus considered inlinable)
     */
    public boolean isInline() {
        return hasOneChild();
    }

    void declare(NodeSet nset) {
        if(isInline())  return;

        clazz = nset.createClass(name);
        clazz._implements(TypedXmlWriter.class);
    }

    void generate(NodeSet nset) {
        if(clazz==null)     return;

        HashSet<Prop> props = new HashSet<Prop>();
        for( Leaf l : this )
            l.generate(clazz,nset,props);
    }

    void generate(JDefinedClass clazz, NodeSet nset, Set<Prop> props) {
        if(isInline()) {
            for( Leaf l : this )
                l.generate(clazz,nset, props);
        } else {
            assert this.clazz!=null;
            clazz._implements(this.clazz);
        }
    }

    void prepare(NodeSet nset) {
        if(isInline() && leaf instanceof WriterNode && !name.equals(Grammar.START))
            ((WriterNode)leaf).alternativeName = name;
    }

    public String toString() {
        return "Define "+name;
    }
}
