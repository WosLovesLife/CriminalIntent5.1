package com.bignerdranch.android.criminalintent51.single;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.criminalintent51.bean.Crime;
import com.bignerdranch.android.criminalintent51.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent51.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent51.database.CrimeDbSchema.CrimeTable;
import com.project.myutilslibrary.CloseStreamTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangH on 2016/5/24.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    /** 取单例实例 */
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimeLab;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    private CrimeCursorWrapper queryCrimes(String where, String[] whereArgs) {
        Cursor query = mDatabase.query(
                CrimeTable.NAME,
                null,
                where, whereArgs,
                null, null, null);
        return new CrimeCursorWrapper(query);
    }

    /** 获取所有Crimes记录 */
    public List<Crime> getCrimes() {
        ArrayList<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            CloseStreamTool.close(cursorWrapper);
        }
        return crimes;
    }

    /** 获取指定UUID的Crime记录 */
    public Crime getCrime(UUID uuid) {

        CrimeCursorWrapper cursorWrapper = queryCrimes(
                CrimeTable.Cols.UUID + "=?",
                new String[]{uuid.toString()}
        );

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            CloseStreamTool.close(cursorWrapper);
        }
    }

    public void updateCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        String uuidString = crime.getId().toString();
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + "=?", new String[]{uuidString});
    }

    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(Crime crime) {
        mDatabase.delete(
                CrimeTable.NAME, CrimeTable.Cols.UUID + "=?",
                new String[]{crime.getId().toString()}
        );
    }

    public File getPhotoFile(Crime crime) {
        File filesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        if (filesDir == null) {
            filesDir = new File(Environment.getExternalStorageDirectory(), "CriminalIntent");
            if (!filesDir.exists())
                filesDir.mkdir();
        }

        return new File(filesDir, crime.getPhotoName());
    }
}
