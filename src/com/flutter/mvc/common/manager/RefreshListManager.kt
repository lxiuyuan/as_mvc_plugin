package com.flutter.mvc.common.manager

class RefreshListManager {

    var refreshListener:(()->Unit)?=null

    fun refresh(){
        refreshListener?.invoke()
    }

    companion object{
        var manager:RefreshListManager?=null
        get() {
            if(field==null){
                field= RefreshListManager()
            }
            return field
        }
    }
}