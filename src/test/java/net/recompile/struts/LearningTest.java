package net.recompile.struts;

import java.io.InputStream;
import java.util.List;

import org.jruby.embed.EmbedEvalUnit;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;

public class LearningTest {

    public void testBoolean() throws Exception {
        String path = "/learning_test.rb";

        ScriptingContainer container = new ScriptingContainer();

        InputStream is = getClass().getResourceAsStream(path);
        EmbedEvalUnit unit = container.parse(is, path);

        IRubyObject obj = unit.run();
        Object ret = container.callMethod(obj, "return_true", Boolean.class);

        System.out.println(ret);
        System.out.println(ret.getClass());
    }

    public void testJavaxImport() throws Exception {
        String path = "/learning_test.rb";

        ScriptingContainer container = new ScriptingContainer();

        InputStream is = getClass().getResourceAsStream(path);
        EmbedEvalUnit unit = container.parse(is, path);
        IRubyObject obj = unit.run();
        container.callMethod(obj, "http_servlet_request", Boolean.class);
    }
    
    @Test
    public void testLoadPaths() throws Exception {
        ScriptingContainer container = new ScriptingContainer();
        List<String> loadPaths = container.getLoadPaths();
        for (String path : loadPaths) {
            System.out.println(path);
        }
        
        Object ret = container.runScriptlet("require 'time'");
        System.out.println(ret);
    }
}
