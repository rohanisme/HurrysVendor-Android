package hurrys.corp.vendor.Models.Product;

public class Product {
    public String Name;
    public String PushId;

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

    public Product(){}

    public Product(String PushId,String Name){
        this.PushId=PushId;
        this.Name=Name;
    }
}
