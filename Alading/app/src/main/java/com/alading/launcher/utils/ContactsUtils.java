package com.alading.launcher.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.toycloud.cmccwatchsdk.model.ContactItemInfo;
import com.toycloud.cmccwatchsdk.model.ContactsClassfiedInfo;

import java.util.List;

/**
 * Created by chongming on 18-5-21.
 */

public class ContactsUtils {
    private String TAG="ContactsUtils";
    private static List<ContactItemInfo> allContactsList;
    private static List<ContactItemInfo> normalContactsList;
    private static List<ContactItemInfo> singleContactsList;
    private static List<ContactItemInfo> sosContactsList;
    private static String name,phone,shortNumber,type;

    public static void AddContact(Context context,String name,String phone,String type,String shortNumber){
        /* 往 raw_contacts 中添加数据，并获取添加的id号*/
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        long contactId = ContentUris.parseId(resolver.insert(uri, values));

        /* 往 data 中添加数据（要根据前面获取的id号） */
        // 添加姓名
        uri = Uri.parse("content://com.android.contacts/data");
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data2", name);
        resolver.insert(uri, values);

        // 添加电话
        values.clear();
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data1", phone);
        resolver.insert(uri, values);

        // ＇备注＇中添加type(通话类型)
        values.clear();
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/note");
        values.put("data1", type);
        resolver.insert(uri, values);

        // ＇SIP＇中添加shortNumber(短号)
        values.clear();
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/sip_address");
        values.put("data1", type);
        resolver.insert(uri, values);
    }

    public static void AddContact(Context context, ContactsClassfiedInfo contactsClassfiedInfo){
        allContactsList = contactsClassfiedInfo.getAllContactsList();
        normalContactsList = contactsClassfiedInfo.getNormalContactsList();
        singleContactsList = contactsClassfiedInfo.getSingleContactsList();
        sosContactsList = contactsClassfiedInfo.getSosContactsList();
        for (ContactItemInfo contatcs:allContactsList){
                phone = contatcs.a();
                //短号
                shortNumber = contatcs.b();
                name = contatcs.c();
                //普通通话：２，紧急通话：１，单项聆听
                type = contatcs.d();
                AddContact(context,name,phone,type,shortNumber);
        }
    }

    public static void clearOldContatcs(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (cur.moveToNext()) {
            try{
                String lookupKey = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.
                        Contacts.CONTENT_LOOKUP_URI, lookupKey);
                System.out.println("The uri is " + uri.toString());
                cr.delete(uri, null, null);//删除所有的联系人
            }
            catch(Exception e)
            {
                System.out.println(e.getStackTrace());
            }
        }
    }
}
