package dima.rebenko.notebook.model;

import android.util.Log;

import org.parceler.Parcel;

import java.util.Date;
import java.util.UUID;

import io.realm.NoteRealmProxy;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

@Parcel(implementations = { NoteRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Note.class })
public class Note extends RealmObject {

    @PrimaryKey
    long id;
    @Required
    String name;
    @Required
    String description;
    @Required
    String importance;
    @Required
    Date date;
    @Required
    Date creationalTime;
    String pathToImage;

    public enum Importance {
        NO_MATTER, IMPORTANT, MOST_IMPORTANT;
    }

    public interface Schema {
        public static String NAME = "name";
        public static String DESCRIPTION = "description";
        public static String IMPORTANCE = "importance";
        public static String DATE = "date";
        public static String CREATIONAL_TIME = "creationalTime";
        public static String PATH_TO_IMAGE = "pathToImage";
        public static String ID = "ID";
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note.this.pathToImage = pathToImage;
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note.this.name = name;
            }
        });
    }

    public void setupId() {
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    public long getId() {
        return this.id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note.this.description = description;
            }
        });

    }

    public Importance getImportance() {
        Log.d("Importance", this.importance);
        return Importance.valueOf(this.importance);
    }

    public void setImportance(Importance importance) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note.this.importance = importance.name();
            }
        });

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note. this.date = date;
            }
        });
    }

    public Date getCreationalTime() {
        return creationalTime;
    }

    public void setCreationalTime(Date creationalTime) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note.this.creationalTime = creationalTime;
            }
        });
    }
}
