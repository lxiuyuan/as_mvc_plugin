package com.flutter.mvc.common.utils

import com.flutter.mvc.common.element.FileElement
import java.lang.StringBuilder

class ElementFenUtils {
    companion object {
        //检查是不是方法
        fun checkIsFun(text: String): Boolean {
            val start = text.indexOf('=')
            if(start==-1){
                return false
            }
            val leftCode = text.subSequence(0, start)
            if (leftCode.trimEnd().endsWith(')')) {
                return true
            }
            return false
        }

        //是否是扩展函数
        fun checkIsExtension(text: String): Boolean {
            return (text.contains("extension ") && text.contains(" on "))
        }
        //是否是get方法
        fun checkIsGet(text: String): Boolean {
            if(!text.contains("get ")){
                return false
            }
            val index=text.indexOf("get ")
            if(index==0){
                return true
            }
            val left=text[index-1]
            return (left=='\n' || left==' ')
        }
        //是否是set方法
        fun checkIsSet(text: String): Boolean {
            if(!text.contains("set ")){
                return false
            }
            val index=text.indexOf("set ")
            if(index==0){
                return true
            }
            val left=text[index-1]
            return (left=='\n' || left==' ')
        }



        fun getAnnotation(text:String):String{
            val lines=text.split("\n")
            val sb=StringBuilder()
            for(line in lines){
                if(line.startsWith("//")){
                    sb.append(line+"\n")
                }else{
                    break;
                }
            }
            return sb.toString();

        }

        //获取方法名字
        fun getFunName(text:String):String{
            val startIndex=text.indexOf('(')
            for(i in startIndex downTo 0){
                val char=text[i]
                if(char==' ') {
                    return text.substring(i,startIndex).trim()
                }

            }
            return ""

        }
        //获取方法体
        fun getFunBody(text:String):String{
            val startIndex=text.indexOf(')')+1
            return text.substring(startIndex)
        }
        //getter名称
        fun getGetterNameTypeValue(text:String):List<String>{
            val str=ArrayList<String>();
            val index=text.indexOf('=')
            val left=text.subSequence(0,index).trim();
            val words=left.split(" ")
            //添加name
            str.add(words.last())
            val type=words[words.lastIndex-1]
            //添加类型
            if(type=="get"){
                str.add("dyanmic")
            }else{
                str.add(type)
            }
            //添加value
            str.add(text.substring(index,text.length))
            return str
        }


        fun getFieldNameTypeValue(text:String):List<String>{
            val str=ArrayList<String>()
            val index=text.indexOf('=')
            val left=text.subSequence(0,index)
            val words=left.split(" ")
            //添加name
            str.add(words.last())
            //添加类型
            val type=words[words.lastIndex-1]
            if(type=="final"||type=="var"){
                str.add("dyanmic")
            }else{
                str.add(type)
            }
            //添加value
            str.add(text.substring(index,text.length))
            return str
        }


        fun getEndLine(text: String, i: Int): Int {
            val length=text.length
            for(j in i until length){
                val char=text[j]
                if(char=='\n'){
                    return j
                }
            }
            return length
        }


    }




}