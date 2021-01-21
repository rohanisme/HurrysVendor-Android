package hurrys.corp.vendor.Models.ProductsList;

public class ProductsList {
    public String ItemName,PushId,ItemDescription,FoodType,ItemImage1,FoodImage;

    public ProductsList(){}

    public ProductsList(String ItemName,String PushId,String ItemDescription,String FoodType,String ItemImage1,String FoodImage){
       this.ItemName=ItemName;
       this.PushId=PushId;
       this.ItemDescription=ItemDescription;
       this.ItemImage1=ItemImage1;
       this.FoodType=FoodType;
       this.FoodImage=FoodImage;
    }



}
