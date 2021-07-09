package com.flutter.mvc.common

import com.flutter.mvc.common.DartFileType.Companion.INSTANCE
import com.flutter.mvc.model.MvcBean
import com.google.gson.Gson
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.ui.JBColor
import java.awt.Color

class Config {

    companion object{

        fun init(key:String,json:String?){
            if(json!=null&&json.isNotEmpty()) {
                val mvcConfig=Gson().fromJson(json,MvcBean::class.java)
                mapMvcConfig[key] = mvcConfig
            }else{
                mapMvcConfig[key] = MvcBean()
            }
        }
        fun getMvcBean(key:String):MvcBean{
            return mapMvcConfig[key]!!
        }
        fun setMvcBean(project: Project, key:String, bean:MvcBean){
             mapMvcConfig[key]=bean;

            val directory = PsiManager.getInstance(project).findDirectory(project.baseDir)

            val json = Gson().toJson(bean)
            ApplicationManager.getApplication().runWriteAction {
                directory!!.findFile(".flutter_mvc.json")?.delete()
                val file = PsiFileFactory.getInstance(project).createFileFromText(".flutter_mvc.json", DartFileType.INSTANCE, json)
                directory!!.add(file)
            }

        }
        val mapMvcConfig=HashMap<String,MvcBean>()
        val background: Color = JBColor.namedColor("Plugins.SectionHeader.foreground", JBColor(0xffffff, 0x45494a))
        val authorColor: Color = JBColor.namedColor("Plugins.SectionHeader.authorColor", JBColor(0xeeeeee, 0x45494a))
        val editBackground: Color = JBColor.namedColor("Plugins.SectionHeader.textBackground", JBColor(0xffffff, 0x45494a))
    }
}