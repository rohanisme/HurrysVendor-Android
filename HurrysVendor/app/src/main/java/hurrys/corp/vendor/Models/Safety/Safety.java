package hurrys.corp.vendor.Models.Safety;

public class Safety {
    public String Description,Image,Title,PushId,Url;

    public Safety(){}

    public Safety(String Description,String Image,String Title,String PushId,String Url){
        this.Description = Description;
        this.Image = Image;
        this.Title = Title;
        this.PushId = PushId;
        this.Url = Url;
    }
}
