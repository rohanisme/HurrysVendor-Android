package hurrys.corp.vendor.Models.Inventory;

public class Inventory {
    public String ItemName;
    public String PushId;
    public String Status;
    public String AStatus;
    public String FoodType;
    public String SellingPrice;

    public Inventory(){}

    public Inventory(String ItemName,String PushId,String Status,String FoodType,String AStatus,String SellingPrice){
        this.ItemName=ItemName;
        this.PushId=PushId;
        this.Status=Status;
        this.FoodType=FoodType;
        this.AStatus=AStatus;
        this.SellingPrice=SellingPrice;
    }
}
