package com.flutter.mvc.ui;

import com.flutter.mvc.model.MvcBean;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.WindowManager;
import common.widget.VFlowLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Config implements SearchableConfigurable {
    private JPanel panel1;
    private JEditorPane txtControllerImport;
    private JEditorPane txtControllerExtends;
    private JEditorPane txtMvcList;
    private JEditorPane txtViewImport;
    private JEditorPane txtViewExtends;
    private JEditorPane txtHttpImport;
    private JEditorPane txtHttpExtends;


    public static Project getCurrProject() {

        ProjectManager projectManager = ProjectManager.getInstance();
        Project[] openProjects = projectManager.getOpenProjects();
        if (openProjects.length == 0) {
            return projectManager.getDefaultProject();//没有打开项目
        } else if (openProjects.length == 1) {
            // 只存在一个打开的项目则使用打开的项目
            return openProjects[0];
        }

        //如果有项目窗口处于激活状态
        try {
            WindowManager wm = WindowManager.getInstance();
            for (Project project : openProjects) {
                Window window = wm.suggestParentWindow(project);
                if (window != null && window.isActive()) {
                    return project;
                }
            }
        } catch (Exception ignored) {
        }

        //否则使用默认项目
        return projectManager.getDefaultProject();
    }
    private final Project project;
    public Config(){
        panel1.setLayout(new VFlowLayout());
        project=getCurrProject();
        init();
    }

    void init(){
        MvcBean mvcBean=com.flutter.mvc.common.Config.Companion.getMvcBean(Objects.requireNonNull(project.getBasePath()));
        txtControllerImport.setText(mvcBean.importController);
        txtControllerExtends.setText(mvcBean.extendsController);
        txtViewImport.setText(mvcBean.importView);
        txtViewExtends.setText(mvcBean.extendsView);
        txtHttpImport.setText(mvcBean.importHttp);
        txtHttpExtends.setText(mvcBean.extendsHttp);
        txtMvcList.setText(mvcBean.mvcPath);
    }


    @Override
    public @NotNull
    @NonNls String getId() {
        return "flutterMvc";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "FlutterMvc";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return panel1;

    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        MvcBean mvcBean=com.flutter.mvc.common.Config.Companion.getMvcBean(Objects.requireNonNull(project.getBasePath()));
        mvcBean.importController=txtControllerImport.getText();
        mvcBean.extendsController=txtControllerExtends.getText();
        mvcBean.importView=txtViewImport.getText();
        mvcBean.extendsView=txtViewExtends.getText();
        mvcBean.importHttp=txtHttpImport.getText();
        mvcBean.extendsHttp=txtHttpExtends.getText();
        mvcBean.mvcPath=txtMvcList.getText();
        com.flutter.mvc.common.Config.Companion.setMvcBean(project,project.getBasePath(),mvcBean);



    }
}
