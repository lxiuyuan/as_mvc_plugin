package com.flutter.mvc.ui

import com.flutter.mvc.common.manager.RefreshListManager
import com.flutter.mvc.common.utils.ClassUtils
import com.flutter.mvc.model.ControllerInfoModel
import com.flutter.mvc.common.utils.StringUtils
import com.flutter.mvc.common.widget.RoundPanel
import com.intellij.ide.actions.OpenFileAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.openapi.ui.ComboBox
import common.widget.VFlowLayout
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*
import javax.swing.*
import kotlin.collections.ArrayList


class ListControllerViewz(private val project: Project):JPanel() {

    init {
        addView()
        initListener()
        init()
    }


    private lateinit var jList:JPanel
    private lateinit var jSearchTextField: JTextField
    private lateinit var jAuthorBox:JComboBox<String>
    private lateinit var jRefreshButton:JButton
    private lateinit var dataControllerInfo:ArrayList<ControllerInfoModel>
    private  var searchedControllerInfo=ArrayList<ControllerInfoModel>()
    private  var contentControllerInfo=ArrayList<ControllerInfoModel>()
    private var searchedAuthor="全部"
    private var searchedText=""

    fun init(){
        val directory= PsiManager.getInstance(project).findDirectory(project.baseDir)
        val lib=findDirectoryChildByName(directory!!,"lib")
        val page=findDirectoryChildByName(lib!!,"mvc")?:return
        val list=ArrayList<PsiFile>()
        findControllerFiles(page,list)


        val manager= FileEditorManager.getInstance(project)
        dataControllerInfo=ArrayList()
        for (psiFile in list) {
            val info=getControllerInfo(psiFile)
            dataControllerInfo.add(info)
        }

        val addedAuthor=ArrayList<String>()
        jList.removeAll()
        for (info in dataControllerInfo) {
            jList.add(createItem(info))
            if(!addedAuthor.contains(info.author)){
                addedAuthor.add(info.author)
                jAuthorBox.addItem(info.author)
            }
        }

    }

    fun refresh(){
        jSearchTextField.text=""
        searchedAuthor="全部"
        searchedControllerInfo.clear()
        jAuthorBox.removeAllItems()
        jAuthorBox.addItem("全部")
        init()
    }

    fun createItem(item: ControllerInfoModel):RoundPanel{
        val panel=RoundPanel()
        panel.layout=BorderLayout()
        panel.add(JLabel("  \n${item.name}(${item.description})\n"),BorderLayout.WEST)
        panel.add(JLabel(" "),BorderLayout.SOUTH)
        panel.add(JLabel(" "),BorderLayout.NORTH)
        panel.setOnClickListener {
            val file=item.file.virtualFile
            OpenFileAction.openFile(file,project)
        }
        return panel
    }

    fun  initListener(){
        jRefreshButton.addActionListener {
            refresh()
        }
        jSearchTextField.addKeyListener(object:KeyListener{
            override fun keyTyped(p0: KeyEvent?) {

            }

            override fun keyPressed(p0: KeyEvent?) {
            }

            override fun keyReleased(p0: KeyEvent?) {

                val text=jSearchTextField.text.trim()
                if(searchedText==text){
                    return
                }
                search(text)
            }

        })
        RefreshListManager.manager?.refreshListener={
            refresh()
        }

        jAuthorBox.addItemListener {
            val author=jAuthorBox.selectedItem?.toString()?.trim()?:return@addItemListener
            if(searchedAuthor==author){
                return@addItemListener
            }
            searchedAuthor=author
            search(searchedText)

        }

//        jList.addListSelectionListener {
//            val index=jList.selectedIndex
//            if(index<0){
//                return@addListSelectionListener
//            }
//            var file: VirtualFile
//            if(searchedControllerInfo.size>0){
//                file=searchedControllerInfo[index].file.virtualFile
//            }else{
//                file=dataControllerInfo[index].file.virtualFile
//            }
//        file=dataControllerInfo[index].file.virtualFile
//            OpenFileAction.openFile(file,project)
//        }
    }

    fun search(text:String){

        if(searchedText.length>text.length){
            searchedControllerInfo.clear()
        }

        if(searchedControllerInfo.size==0){
            searchedControllerInfo.addAll(dataControllerInfo)
        }
        val dataInfo=ArrayList<ControllerInfoModel>()
        for (info in searchedControllerInfo) {
            if(text==""){
                dataInfo.add(info)
                continue
            }
            if(StringUtils.isContainChinese(text)){
                if(info.description.contains(text)){
                    dataInfo.add(info)
                }
            }else if(info.name.contains(text)||info.descriptionFirstLetter.contains(text)||info.descriptionPinyin.contains(text)){
                dataInfo.add(info)
            }
        }

        contentControllerInfo.clear()
        contentControllerInfo.addAll(searchByAuthor(dataInfo))
        jList.removeAll()
        for (info in contentControllerInfo) {
            jList.add(createItem(info))
        }
        jList.updateUI()


//        jList.setListData(vectory)
        searchedText=text;

    }

    fun searchByAuthor(infos:ArrayList<ControllerInfoModel>):ArrayList<ControllerInfoModel>{
        if(searchedAuthor=="全部"){
            return infos
        }
        val authorInfos=ArrayList<ControllerInfoModel>()
        for (info in infos) {
            if(info.author==searchedAuthor){
                authorInfos.add(info)
            }
        }
        return authorInfos
    }


    fun addView(){
        layout=BorderLayout()
        jList=JPanel(VFlowLayout())

        val searchJPanel=JPanel(BorderLayout())
        val  buttonJPanel=JPanel(BorderLayout())

        jSearchTextField= JTextField()
        jAuthorBox= ComboBox<String>()
        jAuthorBox.background= Color(0xeeeeee)
        jAuthorBox.addItem("全部")
        jRefreshButton= JButton("刷新")
        searchJPanel.add(jAuthorBox,BorderLayout.WEST)
        buttonJPanel.add(jRefreshButton,BorderLayout.EAST)
        searchJPanel.add(jSearchTextField,BorderLayout.CENTER)
        searchJPanel.add(buttonJPanel,BorderLayout.EAST)
//        createJLabel(panel)
        add(JScrollPane(jList),BorderLayout.CENTER)
        add(searchJPanel,BorderLayout.NORTH)
    }

    //根据pisFile转换controllerinfo
    fun getControllerInfo(file: PsiFile): ControllerInfoModel {
        val info= ControllerInfoModel(file)
        for (child in file.children) {
            val text=child.text
            //get text.split("Description:")
            if(text.contains("Description:")){
                val i=text.split("Description:")
                val description=if(i.size>1){i[1]}else{""}
                val pinyinAndFirstLetter= StringUtils.chineseToPinyinAndFirstLetter(description.trim())
                info.description=description
                info.descriptionPinyin=pinyinAndFirstLetter[0]
                info.descriptionFirstLetter=pinyinAndFirstLetter[1]
            }
            //get text.split("Description:")
            if(text.contains("Author:")){
                val i=text.split("Author:")
                val author=if(i.size>1){i[1]}else{""}
//                val pinyinAndFirstLetter= StringUtils.chineseToPinyinAndFirstLetter(author.trim())
                info.author=author
            }
            //get name
            if(text.contains("class")){
                val start=text.indexOf("class")
                val end=text.indexOf("Controller")

                if(end>0) {
                    try {
                        val name = text.substring(start + 5, end).trim()
//                val para=getClassPara(text,name)

                        info.name = name
                        info.para = ""
                    } catch (e: Exception) {
                        println(text)

                    }
                }
            }
        }
        return info
    }

    fun getClassPara(text:String,name:String):String{
        val n=name+"Controller"
        val split=text.split("\n")
        for (s in split) {
            if(s.trim()==""){
                continue
            }
            if(s.contains(n)&&s.contains("(")){
                var para=ClassUtils.getPara(s)

               return para;
            }
        }
        return ""
    }

    //根据name从当前的directory查找需要的directory
    fun findDirectoryChildByName(directory: PsiDirectory, name:String): PsiDirectory?{
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
    fun findControllerFiles(directory: PsiDirectory, list:ArrayList<PsiFile>): PsiDirectory?{
        for (child in directory.children) {
            if(child is PsiDirectory){
                findControllerFiles(child,list)
            } else if(child is PsiFile){
                if(child.name=="controller.dart"||child.name.endsWith("controller.dart")){
                    list.add(child)
                }
            }
        }
        return null;
    }


}