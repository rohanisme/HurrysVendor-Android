package hurrys.corp.vendor.Models.Product;

public class Product {
    public String Name;
    public String PushId;

    public Product(){}

    public Product(String PushId,String Name){
        this.PushId=PushId;
        this.Name=Name;
    }
}
