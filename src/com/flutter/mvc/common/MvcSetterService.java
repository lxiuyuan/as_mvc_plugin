package com.flutter.mvc.common;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.jdom.Element;


@State(name = "Test", storages = {@Storage(file = StoragePathMacros.WORKSPACE_FILE+"/.flutter_mvc.xml"
)})
public class MvcSetterService implements PersistentStateComponent<Element> {
    String ceva;
    public MvcSetterService() {
        ceva = "sad";
        System.out.println("constr");
    }
    public String getCeva() {
        return ceva;
    }
    public void setCeva(String ceva) {
        this.ceva = ceva;
    }
    public void loadState(Element state) {
        System.out.println("cstate load");
        ceva = (String) state.getContent().get(0).getValue();
    }
    public Element getState() {
        System.out.println("cstate retu");
        Element configurationsElement = new Element("testtt");
        configurationsElement.addContent(ceva);
        return configurationsElement;
    }

    public static void main(String[] args){

    }
}