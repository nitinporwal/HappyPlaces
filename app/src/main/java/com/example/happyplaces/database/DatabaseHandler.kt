package com.example.happyplaces.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.happyplaces.models.HappyPlaceModel
import java.lang.Exception

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HappyPlacesDatabase"
        private const val TABLE_HAPPY_PLACES = "HappyPlacesTable"

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGIUTDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_HAPPY_PLACE_TABLE =
            ("CREATE TABLE $TABLE_HAPPY_PLACES ($KEY_ID INTEGER PRIMARY KEY, $KEY_TITLE TEXT, $KEY_IMAGE TEXT, $KEY_DESCRIPTION TEXT, $KEY_DATE TEXT, $KEY_LOCATION TEXT, $KEY_LATITUDE TEXT, $KEY_LONGIUTDE TEXT)")
        db?.execSQL(CREATE_HAPPY_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_PLACES")
        onCreate(db)
    }

    fun addHappyPlaces(happyPlace: HappyPlaceModel): Long {
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(KEY_TITLE, happyPlace.title)
        contentValue.put(KEY_IMAGE, happyPlace.image)
        contentValue.put(KEY_DESCRIPTION, happyPlace.description)
        contentValue.put(KEY_DATE, happyPlace.date)
        contentValue.put(KEY_LOCATION, happyPlace.location)
        contentValue.put(KEY_LATITUDE, happyPlace.latitude)
        contentValue.put(KEY_LONGIUTDE, happyPlace.longitude)

        val result = db.insert(TABLE_HAPPY_PLACES, null, contentValue)
        db.close()
        return result
    }


    fun updateHappyPlaces(happyPlace: HappyPlaceModel): Int {
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(KEY_TITLE, happyPlace.title)
        contentValue.put(KEY_IMAGE, happyPlace.image)
        contentValue.put(KEY_DESCRIPTION, happyPlace.description)
        contentValue.put(KEY_DATE, happyPlace.date)
        contentValue.put(KEY_LOCATION, happyPlace.location)
        contentValue.put(KEY_LATITUDE, happyPlace.latitude)
        contentValue.put(KEY_LONGIUTDE, happyPlace.longitude)

        val success = db.update(TABLE_HAPPY_PLACES, contentValue, KEY_ID + "=" + happyPlace.id, null)
        db.close()
        return success
    }

    fun getHappyPlacesList(): ArrayList<HappyPlaceModel> {
        val happyPlaceList: ArrayList<HappyPlaceModel> = ArrayList<HappyPlaceModel>()

        val selectQuery = "SELECT * FROM $TABLE_HAPPY_PLACES"
        val db = this.readableDatabase
        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = HappyPlaceModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGIUTDE))
                    )
                    happyPlaceList.add(place)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        return happyPlaceList
    }

    fun deleteHappyPlace(happyPlace: HappyPlaceModel): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_HAPPY_PLACES, KEY_ID + "=" + happyPlace.id, null)
        db.close()
        return success
    }
}