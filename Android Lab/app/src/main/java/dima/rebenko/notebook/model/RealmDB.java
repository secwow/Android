package dima.rebenko.notebook.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by User on 24.09.2017.
 */
public class RealmDB {

    public static void init(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("univer.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
        Note note = new Note();
        note.setName("Name");
        note.setDate(new Date());
        note.setCreationalTime(new Date());
        note.setDescription("My description");
        note.setImportance(Note.Importance.IMPORTANT);

        addNote(note);
    }

    public static void addNote(Note note) {
        Realm realmConnection = Realm.getDefaultInstance();
        realmConnection.executeTransaction(realm -> realm.copyToRealm(note));
        realmConnection.close();
    }

    public static void updateNote(Note note) {
        Realm realmConnection = Realm.getDefaultInstance();
        realmConnection.executeTransaction(realm -> realm.copyToRealmOrUpdate(note));
        realmConnection.close();
    }

    public static Note getNoteById(long id) {
        Realm realmConnection = Realm.getDefaultInstance();
        return realmConnection.where(Note.class).equalTo("id", id).findFirst();
    }

    public static List<Note> getAllNotes() {
        Realm realmConnection = Realm.getDefaultInstance();
        return realmConnection.where(Note.class).findAll();
    }

    public static void deleteItem(Note note) {
        Realm realmConnection = Realm.getDefaultInstance();
        realmConnection.executeTransaction(realm -> realm.where(Note.class).equalTo("id", note.getId()).findAll().deleteAllFromRealm());
    }

    public static List<Note> searchNotesByDescription(String description) {
        Realm realmConnection = Realm.getDefaultInstance();
        return realmConnection.where(Note.class).contains(Note.Schema.DESCRIPTION, description).findAll();
    }

    public static List<Note> filterNotesByDescription(String description) {
        Realm realmConnection = Realm.getDefaultInstance();
        return realmConnection.where(Note.class).equalTo(Note.Schema.DESCRIPTION, description).findAll();
    }

    public static List<Note> filterByImportance(Note.Importance importance) {
        Realm realmConnection = Realm.getDefaultInstance();
        Log.d("TAG",importance.name());
        return realmConnection.where(Note.class).equalTo(Note.Schema.IMPORTANCE, importance.name()).findAll();
    }
}