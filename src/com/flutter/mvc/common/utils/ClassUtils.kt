package com.flutter.mvc.common.utils

import java.lang.StringBuilder

class ClassUtils {
    companion object{

        fun getPara(clazz:String):String{
            val startIndex=clazz.indexOf("(")
            val endIndex=clazz.indexOf(")")
            var para=clazz.substring(startIndex+1,endIndex)
            if(para.contains("{")){
                para=para.substring(1,para.length-1)
            }
            val names=getParaName(para)
            if(names.size==0){
                return "暂无";
            }
            val sb=StringBuilder(names[0])
            for (index in 1..names.size-1) {
                sb.append(","+names[index])
            }
            return sb.toString()
        }
        private fun getParaName(para:String):List<String>{
            val names=ArrayList<String>()
            val list=para.split(",")
            for (s in list) {
                if(s.contains("this.")){
                    names.add(s.substring("this.".length,s.length))
                }else if(s.contains(" ")){
                    names.add(s.trim().split(" ")[1])
                }else{
                    names.add(s)
                }
            }
            return names;
        }

    }
}