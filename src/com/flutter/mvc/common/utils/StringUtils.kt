package com.flutter.mvc.common.utils

import com.sun.jna.StringArray
import net.sourceforge.pinyin4j.PinyinHelper
import org.apache.http.util.TextUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

class StringUtils {
    companion object{

        ///汉字转换拼音
        fun chineseToPinyinAndFirstLetter(chinese:String):Array<String>{
            val chineseArray=chinese.toCharArray()
            val sbPintin= StringBuilder()
            val sbFirstLetter= StringBuilder()
//            判断是否是汉字 否则英文
            if(isContainChinese(chinese)) {
                for (c in chineseArray) {
                    val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c)
                    if (pinyinArray != null && pinyinArray.size > 0) {
                        val s = pinyinArray[0]
                        val pinyin = s.substring(0, s.length - 1)
                        sbPintin.append(pinyin)
                        sbFirstLetter.append(pinyin[0])
//                    for (s in pinyinArray) {
//                    }
                    } else if (!TextUtils.isEmpty(c.toString())) {
                        sbPintin.append(c)
                    }
                }
            }else{
                sbPintin.append(chinese)
                val words=chinese.split(" ")
                for (word in words) {
                    //判断是否有内容
                    if(word!="")
                    sbFirstLetter.append(word[0])
                }
            }
            val array= arrayOf(sbPintin.toString(),sbFirstLetter.toString())

            return array
        }


        /**
         * 判断字符串中是否包含中文
         * @param str
         * 待校验字符串
         * @return 是否为中文
         * @warn 不能校验是否为中文标点符号
         */
        fun isContainChinese(str: String): Boolean {
            val p: Pattern = Pattern.compile("[\u4e00-\u9fa5]")
            val m: Matcher = p.matcher(str)
            return if (m.find()) {
                true
            } else false
        }

        //"_"转换大写
        fun _toLowName(name:String):String{
            val sb=java.lang.StringBuilder()
            if(name.contains("_")){
                val names=name.split("_")
                for (n in names) {
                    sb.append(n[0].toUpperCase())
                    if(n.length>1){
                        sb.append(n.substring(1,n.length))
                    }

                }

            }else{
                sb.append(name[0].toUpperCase())
                if(name.length>1){
                    sb.append(name.substring(1,name.length))
                }
            }
            return sb.toString()
        }
    }
}