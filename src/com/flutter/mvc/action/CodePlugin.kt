package com.flutter.mvc.action

import com.flutter.mvc.model.ControllerInfoModel
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.project.runWhenProjectOpened
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.file.PsiDirectoryFactory
import com.intellij.testFramework.runInLoadComponentStateMode
import com.sun.jna.platform.win32.Winsvc
import org.jetbrains.builtInWebServer.findIndexFile
import java.nio.file.Path
import java.util.logging.Handler

class CodePlugin(private var project: Project):ProjectComponent {

    override fun initComponent() {

//        println1(project.basePath)


//
        project.runInLoadComponentStateMode {
            init()
        }
//        project.runWhenProjectOpened(object:Runnable{
//            override fun run() {
//                init()
//            }
//
//        })




    }

    fun init(){
        val directory=PsiManager.getInstance(project).findDirectory(project.baseDir)
        val lib=findDirectoryChildByName(directory!!,"lib")
        val page=findDirectoryChildByName(lib!!,"page")
        val list=ArrayList<PsiFile>();
        findControllerFiles(page!!,list)


        val manager=FileEditorManager.getInstance(project);
        manager.run {
            println1(manager.allEditors.size)
        }


        val infos=ArrayList<ControllerInfoModel>()
        for (psiFile in list) {
            val info=getControllerInfo(psiFile)
            infos.add(info)
        }




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

    fun println1(message:Any?){
        System.out.println("CodePlugin:${message}\n")
    }

}