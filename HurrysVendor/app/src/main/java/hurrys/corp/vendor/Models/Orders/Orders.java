package hurrys.corp.vendor.Models.Orders;

public class Orders {

    public String OrderNo;
    public String ItemsDetails;
    public String Pushid;
    public String OrderDateTime;
    public String Subtotal;
    public String SellerCommission;
    public String DeliveryCharges;
    public String Taxes;
    public String Status;
    public String Seller;
    public String OrderType;
    public String VendorCustomer;
    public String CName;
    public String Address;
    public String Qty;
    public String Payment;
    public String DeliveryName;
    public String DeliveryNumber;
    public String DeliveryImage;
    public String DeliverySelection;

    public  Orders(){}

    public Orders(String OrderNo,String ItemDetails,String Pushid,String OrderDateTime,String Status,String SellerCommission,String Subtotal,String DeliveryCharges,String Seller,String OrderType,String VendorCustomer,String CName,String Address,String Qty,String Payment,String DeliveryName,String DeliveryNumber,String DeliveryImage,String Taxes,String DeliverySelection){
        this.OrderDateTime=OrderDateTime;
        this.ItemsDetails=ItemDetails;
        this.Pushid=Pushid;
        this.OrderNo=OrderNo;
        this.Subtotal=Subtotal;
        this.SellerCommission=SellerCommission;
        this.DeliveryCharges=DeliveryCharges;
        this.Status=Status;
        this.Seller=Seller;
        this.OrderType=OrderType;
        this.VendorCustomer=VendorCustomer;
        this.CName=CName;
        this.Address=Address;
        this.Qty=Qty;
        this.Payment=Payment;
        this.DeliveryName=DeliveryName;
        this.DeliveryNumber=DeliveryNumber;
        this.DeliveryImage=DeliveryImage;
        this.Taxes=Taxes;
        this.DeliverySelection = DeliverySelection;
    }
}
