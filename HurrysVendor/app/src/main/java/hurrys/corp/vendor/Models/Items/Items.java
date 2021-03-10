package hurrys.corp.vendor.Models.Items;

public class Items {
    public String Name,Required,PushId,ProductPushId;

    public Items(){}

    public Items(String Name,String Required,String PushId,String ProductPushId){
        this.Name = Name;
        this.Required = Required;
        this.PushId = PushId;
        this.ProductPushId = ProductPushId;
    }

}
