package testcomponent.heyongrui.com.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by lambert on 2018/9/30.
 */

public class Global {
    public static final String KEY_USER = "user";

    public static User loginUser;

    public static class User implements Parcelable, Serializable {
        private static final long serialVersionUID = 1L;

        private String userName;
        private int id;

        public User(int id, String userName) {
            this.id = id;
            this.userName = userName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userName);
            dest.writeInt(this.id);
        }

        protected User(Parcel in) {
            this.userName = in.readString();
            this.id = in.readInt();
        }

        public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
            @Override
            public User createFromParcel(Parcel source) {
                return new User(source);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }

}
