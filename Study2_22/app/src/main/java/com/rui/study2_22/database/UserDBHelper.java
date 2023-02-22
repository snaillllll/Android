package com.rui.study2_22.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rui.study2_22.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="user.db";
    private static final String TABLE_NAME="user_info";
    private static final int DB_VERSION=1;
    private static UserDBHelper mHelper=null;
    private SQLiteDatabase mRDB=null;
    private SQLiteDatabase mWDB=null;

    private UserDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static UserDBHelper getInstance(Context context){
        if(mHelper == null){
            mHelper=new UserDBHelper(context);
        }
        return mHelper;
    }
    //打开数据库的读连接
    public SQLiteDatabase openReadLink(){
        if(mRDB==null||!mRDB.isOpen()){
            mRDB=mHelper.getReadableDatabase();
        }
        return mRDB;
    }
    //打开数据库的写连接
    public SQLiteDatabase openWriteLink(){
        if(mWDB==null||!mWDB.isOpen()){
            mWDB=mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    //关闭数据库连接
    public void closeLink(){
        if(mRDB != null && mRDB.isOpen()){
            mRDB.close();
            mRDB=null;
        }
        if(mWDB != null && mRDB.isOpen()){
            mRDB.close();
            mRDB=null;
        }
    }


    //创建数据库，执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        String  sql="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "name VARCHAR NOT NULL,"+
                "married INTEGER NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(User user){
        ContentValues values=new ContentValues();
        values.put("name",user.name);
        values.put("married",user.married);
        return mWDB.insert(TABLE_NAME,null,values);
    }

    public long deleteByName(String name){
        return mWDB.delete(TABLE_NAME,"name=?",new String[]{name});
    }

    public long update(User user){
        ContentValues values=new ContentValues();
        values.put("name",user.name);
        values.put("married",user.married);
        return mWDB.update(TABLE_NAME,values,"name=?",new String[]{user.name});
    }

    public List<User> queryAll(){
        List<User> list=new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME,null,null,null,null,null,null);

        //循环取出游标指向的每条记录
        while(cursor.moveToNext()){
            User user = new User();
            user.id=cursor.getInt(0);
            user.name=cursor.getString(1);
            user.married=cursor.getInt(2)==1;
            list.add(user);
        }
        return list;
    }

}
