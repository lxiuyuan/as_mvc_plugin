package com.flutter.mvc.common.element

import com.flutter.mvc.common.utils.StringUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.StringBuilder

open class JsonElement(
        var name: String,
        var jsonKey: String
) {

    val spec = "  "
    open fun toCode(): String {
        return "";
    }

    open fun toParse(parent: String): String {
        return "";
    }

    open fun toGenerate(): String {
        return "";
    }
}

class JsonClassElement(
        name: String,
        jsonKey: String,
        private var fields: List<JsonFieldElement>
) : JsonElement(name, jsonKey) {

    override fun toCode(): String {
        val sb = StringBuilder("\nclass ${name}Bean {\n");

        fields.forEach {
            sb.appendLine("${it.toCode()}")
        }

    sb.appendLine("${spec}Map<String,dynamic> toJson()=>jsonFrom${name}Bean(this);")

        sb.appendLine("$spec${name}Bean(Map<String,dynamic> json){")
        sb.appendLine("$spec${spec}jsonTo${name}Bean(this,json);")
        sb.appendLine("$spec}")
        sb.appendLine("}");

        return sb.toString();

    }

    override fun toParse(parent: String): String {

        val sb = StringBuilder("void jsonTo${name}Bean(${name}Bean bean,Map<String,dynamic> json){\n")
        fields.forEach {
            sb.appendLine(it.toParse("bean"))

        }
        sb.appendLine("}")
        return sb.toString();
    }

    override fun toGenerate(): String {
        val sb = StringBuilder("Map<String,dynamic> jsonFrom${name}Bean(${name}Bean bean)=>{\n")

        fields.forEach {
            sb.appendLine(it.toGenerate())
        }
        sb.appendLine("};")
        return sb.toString();
    }

}

class JsonFieldElement(
        name: String,
        jsonKey: String,
        var type: String,
        var listType: String
) : JsonElement(name, jsonKey) {

    private fun getFieldName(): String {
        return StringUtils.initialLower(StringUtils._toLowName(jsonKey))
    }

    override fun toCode(): String {
        return if (type == "class") {
            "${spec}${name}Bean ${getFieldName()};"
        } else {
            "${spec}${type} ${getFieldName()};"
        }
    }

    private fun toOtherParse(parent: String, type: String): String {
        return if (type == "class") {
            "${parent}.${getFieldName()}=${name}Bean(json[\"${jsonKey}\"])"
        } else if (type == "int") {
            "${parent}.${getFieldName()}=int.parse(json[\"${jsonKey}\"]?.toString()??\"0\")"
        } else if (type == "double") {
            "${parent}.${getFieldName()}=double.parse(json[\"${jsonKey}\"]?.toString()??\"0\")"
        }  else if (type == "bool") {
            "${parent}.${getFieldName()}=bool.parse(json[\"${jsonKey}\"]?.toString()??\"false\")"
        } else if (type == "dynamic") {
            "${parent}.${getFieldName()}=json[\"${jsonKey}\"]"
        } else if(type == "String") {
            "${parent}.${getFieldName()}=json[\"${jsonKey}\"].toString()"
        }else{
            "${parent}.${getFieldName()}=${type}Bean(json[\"${jsonKey}\"])"
        }
    }private fun toOtherValueParse(type: String): String {
        return if (type == "class") {
            "${name}(json[\"${jsonKey}\"])"
        } else if (type == "int") {
            "int.parse(json[\"${jsonKey}\"]?.toString()??\"0\")"
        } else if (type == "double") {
            "double.parse(json[\"${jsonKey}\"]?.toString()??\"0\")"
        }  else if (type == "bool") {
            "bool.parse(json[\"${jsonKey}\"]?.toString()??\"false\")"
        } else if (type == "dynamic") {
            "json[\"${jsonKey}\"]"
        } else if (type == "String") {
            "json[\"${jsonKey}\"].toString()"
        } else {
            "${type}(item)"
        }
    }

    override fun toParse(parent: String): String {
        if (type.contains("List")) {
            val sb = StringBuilder("${spec}${parent}.${getFieldName()}=[];\n");
            sb.appendLine("${spec}for(var item in json[\"$jsonKey\"]??[]){")
            sb.appendLine("${spec + spec}${parent}.${getFieldName()}.add(${toOtherValueParse(listType)});")
            sb.appendLine("${spec}}")
            return sb.toString();
        }
        return spec + toOtherParse(parent, type)+";"
    }

    //    "prop_ids":bean.propIds.map((e)=>e.toJson()).toList(),
//    "status":bean.status,
    override fun toGenerate(): String {
        if (type.contains("List") && listType == "class") {

            return "$spec\"$jsonKey\":bean.${getFieldName()}.map((e)=>e.toJson()).toList(),"
        }
        return "$spec\"$jsonKey\":bean.${getFieldName()},"
    }

}

class DJsonParse {

    fun toCode(name:String, json: com.google.gson.JsonElement):String{
        parse(name,"",json)
        val sbCode=StringBuilder()
        val sbParse=StringBuilder()
        val sbGenerate=StringBuilder()
        for (listClass in listClass) {
            sbCode.append(listClass.toCode())
            sbParse.append(listClass.toParse(""))
            sbGenerate.append(listClass.toGenerate())
        }
        return sbCode.toString()+
                "\n//-------------解析方法----------------\n\n"+
                sbParse.toString()+sbGenerate.toString()
    }

    private val listClass = ArrayList<JsonClassElement>();
    private fun parse(key: String,parent:String, json: com.google.gson.JsonElement): JsonFieldElement {

        val name=StringUtils._toLowName(key);
        if (json.isJsonObject) {
            listClass.add(0,createJsonClassElement(key,parent, json.asJsonObject))
            return createJsonClassField(key,parent)
        }else if(json.isJsonPrimitive){
            val e=json.asJsonPrimitive;
            if(e.isNumber){
                if(e.toString().contains(".")){
                    return JsonFieldElement(name,key,"double","")
                }else{
                    return JsonFieldElement(name,key,"int","")
                }
            }else if(e.isBoolean){
                return JsonFieldElement(name,key,"bool","")
            }else{
                return JsonFieldElement(name,key,"String","")
            }

        }else if(json.isJsonArray){
            return createJsonArrayField(key,parent,json.asJsonArray)
        }else{
            return JsonFieldElement(name,key,"dynamic","")
        }

    }


    private fun createJsonArrayField(key: String,parent:String, array: JsonArray,):JsonFieldElement{
        val field=parse(key,parent,array.first())
        var childType=field.type
        if(childType=="class"){
            childType=field.name+"Bean"
        }
        val type="List<${childType}>"
        return JsonFieldElement(StringUtils._toLowName(key),key,type,childType);
    }
    private fun createJsonClassField(key: String,parent:String):JsonFieldElement{

        return JsonFieldElement(parent+StringUtils._toLowName(key),key,"class","");
    }


    private fun createJsonClassElement(key: String,parent:String, obj: JsonObject): JsonClassElement {
        val fields=ArrayList<JsonFieldElement>()
        val name=parent+StringUtils._toLowName(key)
        for (k in obj.keySet()) {
           val value=obj[k];
           fields.add(parse(k,name,value))
        }
        return JsonClassElement(name, key, fields);
    }
}