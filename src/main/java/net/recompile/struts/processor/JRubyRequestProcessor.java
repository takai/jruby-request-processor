package net.recompile.struts.processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.InvalidCancelException;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.jruby.embed.EmbedEvalUnit;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JRubyRequestProcessor extends RequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JRubyRequestProcessor.class);

    private ScriptingContainer container;

    private Set<String> supportedMethods;

    private IRubyObject requestProcessor;

    @Override
    public void init(ActionServlet servlet, ModuleConfig moduleConfig) throws ServletException {
        super.init(servlet, moduleConfig);
        container = new ScriptingContainer(LocalContextScope.THREADSAFE);

        if (LOGGER.isDebugEnabled()) {
            String paths = (String) container.runScriptlet("$:.join(', ')");
            LOGGER.debug("LOAD_PATH is {}", paths);
        }

        container.put("SERVLET_CONTEXT", servlet.getServletContext());

        String path = moduleConfig.getControllerConfig().getProperty("scriptPath");
        InputStream stream = servlet.getServletContext().getResourceAsStream(path);
        EmbedEvalUnit unit = container.parse(stream, path);
        requestProcessor = unit.run();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Ruby RequestProcessor is {}", requestProcessor);
        }
        String[] names = container.callMethod(requestProcessor, "public_methods", false, String[].class);
        supportedMethods = new HashSet<String>(Arrays.asList(names));
    }

    @Override
    public void destroy() {
        super.destroy();

        container.terminate();
    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (supportedMethods.contains("process")) {
            container.callMethod(requestProcessor, "process", request, response);
        } else {
            super.process(request, response);
        }
    }

    @Override
    protected Action processActionCreate(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException {
        if (supportedMethods.contains("process_action_create")) {
            return container.callMethod(requestProcessor, "process_action_create", new Object[] { request, response, mapping }, Action.class);
        } else {
            return super.processActionCreate(request, response, mapping);
        }
    }

    @Override
    protected ActionForm processActionForm(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) {
        if (supportedMethods.contains("process_action_form")) {
            return container.callMethod(requestProcessor, "process_action_form", new Object[] { request, response, mapping }, ActionForm.class);
        } else {
            return super.processActionForm(request, response, mapping);
        }
    }

    @Override
    protected void processForwardConfig(HttpServletRequest request, HttpServletResponse response, ForwardConfig forward) throws IOException, ServletException {
        if (supportedMethods.contains("process_forward_config")) {
            container.callMethod(requestProcessor, "process_forward_config", request, response, forward);
        } else {
            super.processForwardConfig(request, response, forward);
        }
    }

    @Override
    protected ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
        if (supportedMethods.contains("process_action_perform")) {
            return container.callMethod(requestProcessor, "process_action_perform", new Object[] { request, response, action, form, mapping }, ActionForward.class);
        } else {
            return super.processActionPerform(request, response, action, form, mapping);
        }
    }

    @Override
    protected void processCachedMessages(HttpServletRequest request, HttpServletResponse response) {
        if (supportedMethods.contains("process_cached_messages")) {
            container.callMethod(requestProcessor, "process_cached_messages", request, response);
        } else {
            super.processCachedMessages(request, response);
        }
    }

    @Override
    protected void processContent(HttpServletRequest request, HttpServletResponse response) {
        if (supportedMethods.contains("process_content")) {
            container.callMethod(requestProcessor, "process_content", request, response);
        } else {
            super.processContent(request, response);
        }
    }

    @Override
    protected ActionForward processException(HttpServletRequest request, HttpServletResponse response, Exception exception, ActionForm form, ActionMapping mapping) throws IOException,
            ServletException {
        if (supportedMethods.contains("process_exception")) {
            return container.callMethod(requestProcessor, "process_exception", new Object[] { request, response, exception, form, mapping }, ActionForward.class);
        } else {
            return super.processException(request, response, exception, form, mapping);
        }
    }

    @Override
    protected boolean processForward(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
        if (supportedMethods.contains("process_forward")) {
            Boolean result = container.callMethod(requestProcessor, "process_forward", new Object[] { request, response, mapping }, Boolean.class);
            return result.booleanValue();
        } else {
            return super.processForward(request, response, mapping);
        }
    }

    @Override
    protected boolean processInclude(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
        if (supportedMethods.contains("process_include")) {
            Boolean result = container.callMethod(requestProcessor, "process_include", new Object[] { request, response, mapping }, Boolean.class);
            return result.booleanValue();
        } else {
            return super.processInclude(request, response, mapping);
        }
    }

    @Override
    protected void processLocale(HttpServletRequest request, HttpServletResponse response) {
        if (supportedMethods.contains("process_locale")) {
            container.callMethod(requestProcessor, "process_locale", request, response);
        } else {
            super.processLocale(request, response);
        }
    }

    @Override
    protected ActionMapping processMapping(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        if (supportedMethods.contains("process_mapping")) {
            return container.callMethod(requestProcessor, "process_mapping", new Object[] { request, response, path }, ActionMapping.class);
        } else {
            return super.processMapping(request, response, path);
        }
    }

    @Override
    protected HttpServletRequest processMultipart(HttpServletRequest request) {
        if (supportedMethods.contains("process_multipart")) {
            return container.callMethod(requestProcessor, "process_multipart", new Object[] { request }, HttpServletRequest.class);
        } else {
            return super.processMultipart(request);
        }
    }

    @Override
    protected void processNoCache(HttpServletRequest request, HttpServletResponse response) {
        if (supportedMethods.contains("process_no_cache")) {
            container.callMethod(requestProcessor, "process_no_cache", request, response);
        } else {
            super.processNoCache(request, response);
        }
    }

    @Override
    protected String processPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (supportedMethods.contains("process_path")) {
            return container.callMethod(requestProcessor, "process_path", new Object[] { request, response }, String.class);
        } else {
            return super.processPath(request, response);
        }
    }

    @Override
    protected void processPopulate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws ServletException {
        if (supportedMethods.contains("process_populate")) {
            container.callMethod(requestProcessor, "process_populate", request, response, form, mapping);
        } else {
            super.processPopulate(request, response, form, mapping);
        }
    }

    @Override
    protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
        if (supportedMethods.contains("process_preprocess")) {
            Boolean result = container.callMethod(requestProcessor, "process_preprocess", new Object[] { request, response }, Boolean.class);
            return result.booleanValue();
        } else {
            return super.processPreprocess(request, response);
        }
    }

    @Override
    protected boolean processRoles(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
        if (supportedMethods.contains("process_roles")) {
            Boolean result = container.callMethod(requestProcessor, "process_roles", new Object[] { request, response, mapping }, Boolean.class);
            return result.booleanValue();
        } else {
            return super.processRoles(request, response, mapping);
        }
    }

    @Override
    protected boolean processValidate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws IOException, ServletException, InvalidCancelException {
        if (supportedMethods.contains("process_validate")) {
            Boolean result = container.callMethod(requestProcessor, "process_validate", new Object[] { request, response, mapping }, Boolean.class);
            return result.booleanValue();
        } else {
            return super.processValidate(request, response, form, mapping);
        }

    }
}