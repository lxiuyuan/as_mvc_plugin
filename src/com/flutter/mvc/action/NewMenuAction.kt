package com.flutter.mvc.action

import com.flutter.mvc.common.manager.RefreshListManager
import com.flutter.mvc.common.utils.StringUtils
import com.flutter.mvc.ui.CreateView
import com.flutter.mvc.ui.OverrideView
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.file.PsiDirectoryFactory
import java.awt.MouseInfo
import java.awt.Window


class NewMenuAction:AnAction("Flutter Mvc") {
    override fun actionPerformed(event: AnActionEvent) {
        val project=event.getData(PlatformDataKeys.PROJECT) ?:return


        val dataContext=event.dataContext

        val module=LangDataKeys.MODULE.getData(dataContext) ?:return

        val navigatable=LangDataKeys.NAVIGATABLE.getData(dataContext) ?:return

        val directory=when(navigatable){
            is PsiDirectory -> navigatable
            is PsiFile -> navigatable.containingDirectory
            else ->{
                val root=ModuleRootManager.getInstance(module)
                root.sourceRoots.asSequence().mapNotNull {
                    PsiManager.getInstance(project).findDirectory(it)
                }.firstOrNull()

            }
        }?:return

        val directoryFactory=PsiDirectoryFactory.getInstance(project)

        val packageName=directoryFactory.getQualifiedName(directory,true)

        val psiFileFactory=PsiFileFactory.getInstance(project)


        //计算位置大小
        var x=360
        var y=300
        arrayOf(event.inputEvent)
        try{
            val position= MouseInfo.getPointerInfo().location
            x=position.x-100
            y=position.y-32
        }catch (e:Exception){
            try {
                if(Window.WIDTH==0||Window.HEIGHT==0){
                    return;
                }
                x = Window.WIDTH / 2
                y = Window.HEIGHT / 2
            }catch (e:Exception){}
        }

        var isOverride=checkHasChild(directory,"view.dart")||checkHasChild(directory,"controller.dart")||checkHasChild(directory,"http.dart")

        CreateView(x,y,getNameForPackage(packageName),{
            name,description->
            if(isOverride){
                OverrideView(x,y,{
                    createFile(project, psiFileFactory, directory, name,description,isOverride)
                })
            }else {
                createFile(project, psiFileFactory, directory, name,description,isOverride)
            }
        })

    }


    private fun checkHasChild(directory: PsiDirectory,name:String):Boolean{

        return directory.findFile(name)!=null
    }

    private fun getNameForPackage(pkg:String):String{


        val map = System.getenv()

        for (key in map.keys) {
            println("${key}:${map[key]}")
        }


        if(!pkg.contains("/")&&!pkg.contains(".")){
            return "";
        }
        if(pkg.contains("/")) {
            val names = pkg.split("/")
            return names[names.size - 1];
        }else{

            val names = pkg.split(".")
            return names[names.size - 1];
        }
    }

    private fun getUser():String{
        val env=System.getenv()
        if(env.containsKey("USER")){
            return env["USER"]!!
        }
        if(env.containsKey("USERNAME")){
            return env["USERNAME"]!!;
        }
        return "";
    }

    private fun createFile(project: Project?, psiFileFactory: PsiFileFactory, directory: PsiDirectory, name:String,description:String,isOverride:Boolean){

        var user=getUser();
        var zname=StringUtils._toLowName(name)
        if(zname.endsWith("Page")){
            zname=zname.substring(0,zname.length-"Page".length)
        }
        fun getViewCode():String{
            val sb= StringBuilder("import 'controller.dart';\n")
            sb.appendln("import 'package:flutter/material.dart';")
            sb.appendln("import 'package:drive/drive.dart';\n")
            sb.appendln("///description:${description}")
            sb.appendln("class ${zname}Page extends BasePage<${zname}Controller>{")
            sb.appendln("   ")
            sb.appendln("   @override")
            sb.appendln("   Widget build(){")
            sb.appendln("       return Container();")
            sb.appendln("   }")
            sb.appendln("}")

            return sb.toString()
        }

        fun getControllerCode():String{
            val sb= StringBuilder("import 'view.dart';\n")
//            sb.appendln("import 'http.dart';")
            sb.appendln("import 'package:drive/drive.dart';\n")
            sb.appendln("///Description:${description}")
            sb.appendln("///Author:${user}")
            sb.appendln("class ${zname}Controller extends BaseController {")
            sb.appendln("   ")
            sb.appendln("   ${zname}Controller():super(${zname}Page());")
            sb.appendln("   var _http=${zname}Http();")
            sb.appendln("   ")
            sb.appendln("   @override")
            sb.appendln("   void initState(){")
            sb.appendln("       super.initState();")
            sb.appendln("       ")
            sb.appendln("   }")
            sb.appendln("   ")
            sb.appendln("}")
            return sb.toString()
        }
        fun getHttpCode():String{

            val sb= StringBuilder("import 'package:${project!!.name}/common/base/http.dart';\n")
            sb.appendln("///description:${description}")
            sb.appendln("class ${zname}Http extends BaseHttp {")
            sb.appendln("   ")
            sb.appendln("   Future<void> httpList() async{")
            sb.appendln("       var url='';")
            sb.appendln("       var para={};")
            sb.appendln("       var result=await httpPost(url,para);")
            sb.appendln("   }")
            sb.appendln("   ")
            sb.appendln("}")
            return sb.toString()
        }

        project.run {
            //是否文件已经存在
            if(isOverride){
                removeMvcFile(directory);
            }
            val file=psiFileFactory.createFileFromText("view.dart", JavaFileType.INSTANCE ,getViewCode())
            directory.add(file)
            val fileController=psiFileFactory.createFileFromText("controller.dart", JavaFileType.INSTANCE,getControllerCode())
            directory.add(fileController)
//            val fileHttp=psiFileFactory.createFileFromText("http.dart", JavaFileType.INSTANCE,getHttpCode())
//            directory.add(fileHttp)



            Notifications.Bus.notify(
                    Notification("ffff", "FlutterMVC", "创建完成", NotificationType.INFORMATION)
            )
            RefreshListManager.manager?.refresh()


        }


    }
    fun removeMvcFile(directory: PsiDirectory){
        try {
            directory.findFile("view.dart")?.delete()
            directory.findFile("controller.dart")?.delete()
//            directory.findFile("http.dart")?.delete()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}