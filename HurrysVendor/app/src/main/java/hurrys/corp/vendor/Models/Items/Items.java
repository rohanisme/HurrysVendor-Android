package hurrys.corp.vendor.Models.Items;

public class Items {
    public String Name,Required,PushId,ProductPushId,MenuPushId;

    public Items(){}

    public Items(String Name,String Required,String PushId,String ProductPushId,String MenuPushId){
        this.Name = Name;
        this.Required = Required;
        this.PushId = PushId;
        this.ProductPushId = ProductPushId;
        this.MenuPushId = MenuPushId;
    }

}
