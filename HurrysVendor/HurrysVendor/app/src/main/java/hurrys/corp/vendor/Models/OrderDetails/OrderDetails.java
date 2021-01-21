package hurrys.corp.vendor.Models.OrderDetails;

public class OrderDetails {

    public String Image;
    public String Name;
    public String Price;
    public String PushId;
    public String Qty;
    public String Total;
    public String Units;

    public OrderDetails(){}

    public OrderDetails(String Image,String Name,String Price,String PushId,String Qty,String Total,String Units){
        this.Image=Image;
        this.Name=Name;
        this.Price=Price;
        this.PushId=PushId;
        this.Qty=Qty;
        this.Total=Total;
        this.Units=Units;
    }
}
