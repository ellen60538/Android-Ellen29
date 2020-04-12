package tw.org.iii.ellen.ellen29;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ContentResolver cr ;
    private Uri uri = Settings.System.CONTENT_URI ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_EXTERNAL_STORAGE,},
                    123);
        }else {
            init() ;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init() ;
    }

    private void init(){
        cr = getContentResolver() ;
        //要解的資料 content://Settings(設定資料)
        // content://ContactsContract(聯絡人)
        // content://CallLog(通話紀錄)
        // content://MediaStore()
    }

    public void test1(View view) {
        //select * from Settings
        Cursor c = cr.query(uri,null,null,null,null) ;
        Log.v("ellen","count : " + c.getColumnCount()) ;

        for (int i = 0; i < c.getColumnCount(); i++){
            String field = c.getColumnName(i) ;
            Log.v("ellen", field) ;
        }

        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex("name")) ;
            String value = c.getString(c.getColumnIndex("value")) ;
            Log.v("ellen",name + " : " + value) ;
        }
        c.close() ;
    }

    public void test2(View view) {
        Cursor c = cr.query(uri,null,
                "name = ?",
                new String[]{Settings.System.SCREEN_BRIGHTNESS},
                null) ;
        c.moveToNext() ;
        String v = c.getString(c.getColumnIndex("value")) ;
        Log.v("ellen" ,"v = " + v) ;

        c.close() ;
    }

    public void test3(View view) {
        try {
            int v = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS) ;
            Log.v("ellen","v = " + v) ;
        }catch (Exception e){}

    }

    public void test4(View view) {
        uri = ContactsContract.Contacts.CONTENT_URI ;
        Cursor c = cr.query(uri,null,null,null,null) ;
        Log.v("ellen","count : " + c.getColumnCount()) ;

//        for (int i = 0; i < c.getColumnCount(); i++){
//            String field = c.getColumnName(i) ;
//            Log.v("ellen", field) ;
//        }

        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex("display_name")) ;
            Log.v("ellen",name) ;
        }

        c.close() ;
    }

    public void test5(View view) {
        uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI ;
        Cursor c = cr.query(uri,null,null,null,null) ;
        Log.v("ellen","count : " + c.getColumnCount()) ;

        for (int i = 0; i < c.getColumnCount(); i++){
            String field = c.getColumnName(i) ;
            Log.v("ellen", field) ;
        }

        while (c.moveToNext()){
            String name =
                    c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) ;
            String phone =
                    c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ;
            Log.v("ellen",name + ":" + phone) ;
        }
        Log.v("ellen","count = " + c.getCount()) ;

        c.close() ;
    }

    public void test6(View view) {
        uri = CallLog.Calls.CONTENT_URI;
        Cursor c = cr.query(uri,null,null,null,null) ;
        Log.v("ellen","count : " + c.getColumnCount()) ;

        for (int i = 0; i < c.getColumnCount(); i++){
            String field = c.getColumnName(i) ;
            Log.v("ellen", field) ;
        }

        while (c.moveToNext()){
            String name =
                    c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)) ;
            String phone =
                    c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)) ;
            String type =
                    c.getString(c.getColumnIndex(CallLog.Calls.TYPE)) ;
            String date =
                    c.getString(c.getColumnIndex(CallLog.Calls.DATE)) ;
            String duration =
                    c.getString(c.getColumnIndex(CallLog.Calls.DURATION)) ;
            if (CallLog.Calls.INCOMING_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "in" ;
            }else if (CallLog.Calls.OUTGOING_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "out" ;
            }else if  (CallLog.Calls.MISSED_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "miss" ;
            }

            Log.v("ellen",name +":"+ phone +":"+ type +":"+ date +":"+ duration) ;
        }
        Log.v("ellen","count = " + c.getCount()) ;

        c.close() ;
    }
}
