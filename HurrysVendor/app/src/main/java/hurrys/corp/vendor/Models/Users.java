package hurrys.corp.vendor.Models;


public class Users {

    public String UserId,Name,Age,Gender,Email,MobileNumber,AlternateNumber,Address,PostCode,BusinessName,VATCode,BusinessAddress,ShopAddress;
    public String BankName,AccountName,AccountNumber,SortCode,BranchName,BranchAddress,FranchiseCode,StoreName,StoreDescription,Category,StoreOpenTime,StoreCloseTime,DeliveryTime;
    public String Doc1,Doc2,Doc3,Doc4,Doc5,Doc6,Doc7,Doc8,Status,ApprovalStatus,Created,Commission,Comments;
    public int Ratings,PendingAmount,Wallet,Orders;

    public Users(String UserId, String Name, String Age, String Gender, String Email, String MobileNumber, String AlternateNumber, String Address, String PostCode, String BusinessName, String VATCode, String BusinessAddress, String ShopAddress,String BankName,String AccountName, String AccountNumber, String SortCode, String BranchName, String BranchAddress, String FranchiseCode, String StoreName, String StoreDescription, String Category, String StoreOpenTime, String StoreCloseTime, String Doc1, String Doc2, String Doc3, String Doc4, String Doc5, String Doc6, String Doc7, String Doc8, String Status, String ApprovalStatus, String Created, int Ratings, int PendingAmount, int Wallet,String Commission,String DeliveryTime,int Orders) {
        this.UserId = UserId;
        this.Name = Name;
        this.Age = Age;
        this.Gender = Gender;
        this.Email = Email;
        this.MobileNumber = MobileNumber;
        this.AlternateNumber = AlternateNumber;
        this.Address = Address;
        this.PostCode = PostCode;
        this.BusinessName = BusinessName;
        this.VATCode = VATCode;
        this.BusinessAddress = BusinessAddress;
        this.ShopAddress = ShopAddress;
        this.BankName = BankName;
        this.AccountName = AccountName;
        this.AccountNumber = AccountNumber;
        this.SortCode = SortCode;
        this.BranchName = BranchName;
        this.BranchAddress = BranchAddress;
        this.FranchiseCode = FranchiseCode;
        this.StoreName = StoreName;
        this.StoreDescription = StoreDescription;
        this.Category = Category;
        this.StoreOpenTime = StoreOpenTime;
        this.StoreCloseTime = StoreCloseTime;
        this.Doc1 = Doc1;
        this.Doc2 = Doc2;
        this.Doc3 = Doc3;
        this.Doc4 = Doc4;
        this.Doc5 = Doc5;
        this.Doc6 = Doc6;
        this.Doc7 = Doc7;
        this.Doc8 = Doc8;
        this.Status = Status;
        this.ApprovalStatus = ApprovalStatus;
        this.Created = Created;
        this.Ratings = Ratings;
        this.PendingAmount = PendingAmount;
        this.Wallet = Wallet;
        this.Commission=Commission;
        this.DeliveryTime=DeliveryTime;
        this.Orders=Orders;
    }

    public Users(){}
}
