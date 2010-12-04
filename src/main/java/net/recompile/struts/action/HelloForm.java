package net.recompile.struts.action;

import org.apache.struts.action.ActionForm;

public class HelloForm extends ActionForm {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
