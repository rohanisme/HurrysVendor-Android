package hurrys.corp.vendor.Models.MenuTitles;

public class Title {
    public String Name,Required,PushId,ProductPushId;

    public Title(){}

    public Title(String Name,String Required,String PushId,String ProductPushId){
        this.Name = Name;
        this.Required = Required;
        this.PushId = PushId;
        this.ProductPushId = ProductPushId;
    }

}
