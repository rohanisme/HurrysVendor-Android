package hurrys.corp.vendor.Models.ItemStatus;

public class ItemStatus {

    public String Name,Status,PushId,ProductId,Type;

    public ItemStatus(){}

    public ItemStatus(String Name,String Status,String PushId,String ProductId,String Type){
        this.Name = Name;
        this.Status = Status;
        this.PushId = PushId;
        this.ProductId  =ProductId;
        this.Type  = Type;
    }

}
