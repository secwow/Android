package dima.rebenko.notebook.Helpers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dima.rebenko.notebook.model.Note;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by User on 24.09.2017.
 */
public class RealmDB  extends ContentProvider{

    static final String AUTHORITY = "dima.rebenko.notebook";
    static final String CONTACT_PATH = "notes";
    public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CONTACT_PATH);

    // Типы данных
    // набор строк
    static final String NOTES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CONTACT_PATH;

    private static final UriMatcher uriMatcher;
    static final int URI_NOTES = 1;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_NOTES);
    }

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

    @Override
    public boolean onCreate() {
        Realm.init(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1)
    {
        Log.d("MY QUERY STRING", "query, " + uri.toString());
        Realm realm = Realm.getDefaultInstance();
        List<Note> notes = realm.where(Note.class).between(Note.Schema.DATE, this.getStartOfDay(new Date()), this.getEndOfDay(new Date())).findAll();
        Log.d("COUNTER", String.valueOf(notes.size()));
        MatrixCursor cursor = new MatrixCursor(new String[]{Note.Schema.ID, Note.Schema.DATE, Note.Schema.DESCRIPTION, Note.Schema.IMPORTANCE, Note.Schema.NAME, Note.Schema.PATH_TO_IMAGE});
        MatrixCursor.RowBuilder builder;
        for (Note note: notes) {
            Log.d("ROW",note.toString());
            builder = cursor.newRow();
            builder.add(Note.Schema.ID, note.getId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = note.getDate();

                builder.add(Note.Schema.DATE, formatter.format(date));


            builder.add(Note.Schema.NAME, note.getName());
            builder.add(Note.Schema.DESCRIPTION, note.getDescription());
            builder.add(Note.Schema.IMPORTANCE, note.getImportance());
            builder.add(Note.Schema.PATH_TO_IMAGE, note.getPathToImage());
            builder.add(Note.Schema.CREATIONAL_TIME, note.getCreationalTime().toString());
        }
        cursor.setNotificationUri(getContext().getContentResolver(),
                CONTACT_CONTENT_URI);
        Log.d("ROW","TRY TO GET");
        return cursor;
    }

    private Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    private Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return NOTES_CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}