package ctg.permissionx.mylibrary

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Created by YiLei 2022/4/11 12:39
 *
 */
class InvisibleFragment :Fragment() {
    private var callback :(PermissionCallback)?=null
    fun requestNow(cb:PermissionCallback,vararg permissions: String){
        callback=cb
        requestPermissions(permissions,1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==1){
            val deniedList=ArrayList<String>()
            for ((index,result)in grantResults.withIndex()){
                if (result != PackageManager.PERMISSION_GRANTED){
                    deniedList.add(permissions[index])
                }
            }
            val allGranted=deniedList.isEmpty()
            callback?.let { it(allGranted,deniedList) }
        }
    }
}
object PermissionX{
    private const val TAG="InvisibleFragment"

    fun request(activity:FragmentActivity,vararg permissions:String,cb: PermissionCallback){
        val fragmentManager =activity.supportFragmentManager
        val existedFragment=fragmentManager.findFragmentByTag(TAG)
        val fragment=if (existedFragment !=null){
            existedFragment as InvisibleFragment
        }else{
            val invisibleFragment=InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }
        fragment.requestNow(cb,*permissions)
    }
}
typealias PermissionCallback = (Boolean, List<String>)->Unit