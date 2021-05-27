package hurrys.corp.vendor.Models.Portions;

public class Portions {
    public String Name;
    public String PushId;
    public boolean Default;

    public boolean isDefault() {
        return Default;
    }

    public void setDefault(boolean aDefault) {
        Default = aDefault;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPushId() {
        return PushId;
    }

    public void setPushId(String pushId) {
        PushId = pushId;
    }

    public Portions(){}

    public Portions(String PushId, String Name,boolean Default){
        this.PushId=PushId;
        this.Name=Name;
        this.Default = Default;
    }
}
