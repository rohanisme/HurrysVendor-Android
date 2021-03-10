package hurrys.corp.vendor.Models.OrderDetails;

public class OrderDetails1 {

    public String Name;
    public String Price;
    public String Type;
    public String Qty;
    public String Image;
    public String Order;
    public String Customised;
    public String CustomisedQty;

    public OrderDetails1(){}

    public OrderDetails1(String Name, String Price, String Type, String Qty,String Image,String Customised,String CustomisedQty){
        this.Name=Name;
        this.Price=Price;
        this.Type=Type;
        this.Qty=Qty;
        this.Image=Image;
        this.Customised=Customised;
        this.CustomisedQty=CustomisedQty;
    }
}
