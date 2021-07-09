package com.flutter.mvc.action

import com.flutter.mvc.common.Config
import com.flutter.mvc.model.ControllerInfoModel
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ex.ApplicationUtil
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.*
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.testFramework.runInLoadComponentStateMode

class CodePlugin(private var project: Project): ProjectComponent {

    override fun initComponent() {

//        println1(project.basePath)

//

//        project.runWhenProjectOpened(object:Runnable{
//            override fun run() {
//                init()
//            }
//
//        })




    }

    override fun projectOpened() {
        super.projectOpened()

            init()
    }

    fun init(){
            ApplicationUtil.tryRunReadAction {

                val directory=PsiManager.getInstance(project).findDirectory(project.baseDir)
                println(project.basePath)

                val configFile= findConfigFile(directory!!)
                Config.init(project.basePath!!,configFile?.text)

            }

//        val lib=findDirectoryChildByName(directory!!,"lib")
//        val page=findDirectoryChildByName(lib!!,"page")
//        val list=ArrayList<PsiFile>();
//        findControllerFiles(page!!,list)

//
//        val manager=FileEditorManager.getInstance(project);
//        manager.run {
//            println1(manager.allEditors.size)
//        }
//
//
//        val infos=ArrayList<ControllerInfoModel>()
//        for (psiFile in list) {
//            val info=getControllerInfo(psiFile)
//            infos.add(info)
//        }




    }

    fun getControllerInfo(file:PsiFile):ControllerInfoModel{
        val info= ControllerInfoModel(file)
        for (child in file.children) {
            val text=child.text
            //get text.split("description:")
            if(text.contains("description:")){
                val i=text.split("description:")
                val description=if(i.size>1){i[1]}else{""}
                info.description=description
            }
            //get name
            if(text.contains("class")){
                val start=text.indexOf("class")
                val end=text.indexOf("Controller")
                val name=text.substring(start+5,end).trim().toLowerCase()
                info.name=name
            }
        }
        return info
    }

    //根据name从当前的directory查找需要的directory
    fun findDirectoryChildByName(directory:PsiDirectory,name:String):PsiDirectory?{
        for (child in directory.children) {
            if(child is PsiDirectory){
                if(name==child.name){
                    return child;
                }
            }
        }
        return null;
    }
    //根据name从当前的directory查找需要的directory
    fun findControllerFiles(directory:PsiDirectory,list:ArrayList<PsiFile>):PsiDirectory?{
        for (child in directory.children) {
            if(child is PsiDirectory){
                findControllerFiles(child,list)
            } else if(child is PsiFile){
                if(child.name=="controller.dart"){
                    list.add(child)
                }
            }
        }
        return null;
    }
    //根据name从当前的directory查找需要的directory
    fun findConfigFile(directory:PsiDirectory):PsiFile?{
        for (child in directory.children) {
            if(child is PsiFile){
                if(child.name==".flutter_mvc.json"){
                    return child
                }
            }
        }
        return null;
    }

    fun println1(message:Any?){
        System.out.println("CodePlugin:${message}\n")
    }

}