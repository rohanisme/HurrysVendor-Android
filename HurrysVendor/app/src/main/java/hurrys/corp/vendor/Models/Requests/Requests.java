package hurrys.corp.vendor.Models.Requests;

public class Requests {
    public String PushId,Name,Number,Email,Request,Status,Reference;

    public Requests(){}

    public Requests(String PushId,String Name,String Number,String Email,String Request,String Status,String Reference ){
     this.PushId = PushId;
     this.Name = Name;
     this.Number = Number;
     this.Email = Email;
     this.Request = Request;
     this.Status = Status;
     this.Reference = Reference;
    }
}

