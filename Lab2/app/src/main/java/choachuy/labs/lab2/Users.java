package choachuy.labs.lab2;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Users extends RealmObject
{
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String username;
    private String password;

    public Users() {}
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Users{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", password=" + password +
                '}';
    }
}
