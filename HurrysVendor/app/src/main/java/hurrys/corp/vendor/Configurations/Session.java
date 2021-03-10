package hurrys.corp.vendor.Configurations;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusername(String usename)
    {
        prefs.edit().putString("usename", usename).commit();
    }

    public String getusername() {
        String usename = prefs.getString("usename","");
        return usename;
    }


    public void setrole(String usename)
    {
        prefs.edit().putString("role", usename).commit();
    }

    public String getrole() {
        String usename = prefs.getString("role","");
        return usename;
    }

    public void setdeliverytime(String usename)
    {
        prefs.edit().putString("deliverytime", usename).commit();
    }

    public String getdeliverytime() {
        String usename = prefs.getString("deliverytime","");
        return usename;
    }


    public void setname(String usename)
    {
        prefs.edit().putString("name", usename).commit();
    }

    public String getname() {
        String usename = prefs.getString("name","");
        return usename;
    }

    public void setemail(String usename)
    {
        prefs.edit().putString("email", usename).commit();
    }

    public String getemail() {
        String usename = prefs.getString("email","");
        return usename;
    }


    public void setpassword(String usename)
    {
        prefs.edit().putString("pass", usename).commit();
    }

    public String getpassword() {
        String usename = prefs.getString("pass","");
        return usename;
    }

    public void setselection(String usename)
    {
        prefs.edit().putString("setselection", usename).commit();
    }

    public String getselection() {
        String usename = prefs.getString("setselection","");
        return usename;
    }

    public void setstartime(String usename)
    {
        prefs.edit().putString("setstarttime", usename).commit();
    }

    public String getstarttime() {
        String usename = prefs.getString("setstarttime","");
        return usename;
    }

    public void setendtime(String usename)
    {
        prefs.edit().putString("setendtime", usename).commit();
    }

    public String getendtime() {
        String usename = prefs.getString("setendtime","");
        return usename;
    }

    public void setlocation(String usename)
    {
        prefs.edit().putString("location", usename).commit();
    }

    public String getlocation() {
        String usename = prefs.getString("location","");
        return usename;
    }



    public void setpp(String usename)
    {
        prefs.edit().putString("pp", usename).commit();
    }

    public String getpp() {
        String usename = prefs.getString("pp","");
        return usename;
    }


    public void setdaname(String username) {
        prefs.edit().putString("daname", username).commit();
    }

    public String getdaname() {
        String username = prefs.getString("daname", "");
        return username;
    }


    public void setdaaddress(String username) {
        prefs.edit().putString("daaddress", username).commit();
    }

    public String getdaaddress() {
        String username = prefs.getString("daaddress", "");
        return username;
    }

    public void setdaf(String username) {
        prefs.edit().putString("daf", username).commit();
    }

    public String getdaf() {
        String username = prefs.getString("daf", "");
        return username;
    }


    public void setdal(String username) {
        prefs.edit().putString("dal", username).commit();
    }

    public String getdal() {
        String username = prefs.getString("dal", "");
        return username;
    }

    public void setdaloc(String username) {
        prefs.edit().putString("daloc", username).commit();
    }

    public String getdaloc() {
        String username = prefs.getString("daloc", "");
        return username;
    }

    public void setdadist(String username) {
        prefs.edit().putString("dadist", username).commit();
    }

    public String getdadist() {
        String username = prefs.getString("dadist", "");
        return username;
    }


    public void setisfirsttime(String username) {
        prefs.edit().putString("first", username).commit();
    }

    public String getisfirsttime() {
        String username = prefs.getString("first", "");
        return username;
    }


    public void setcart(String username) {
        prefs.edit().putString("cart", username).commit();
    }

    public String getcart() {
        String username = prefs.getString("cart", "");
        return username;
    }



    public void settoken(String username) {
        prefs.edit().putString("token", username).commit();
    }

    public String gettoken() {
        String username = prefs.getString("token", "");
        return username;
    }

    public void setsub(String username) {
        prefs.edit().putString("sub", username).commit();
    }

    public String getsub() {
        String username = prefs.getString("sub", "");
        return username;
    }



    public void setrange(String username) {
        prefs.edit().putString("range", username).commit();
    }

    public String getrange() {
        String username = prefs.getString("range", "");
        return username;
    }



    public void setpincode(String username) {
        prefs.edit().putString("pincode", username).commit();
    }

    public String getpincode() {
        String username = prefs.getString("pincode", "");
        return username;
    }


    public void setnumber(String username) {
        prefs.edit().putString("setnumber", username).commit();
    }

    public String getnumber() {
        String username = prefs.getString("setnumber", "");
        return username;
    }



    public void setextras(String username) {
        prefs.edit().putString("esetextras", username).commit();
    }

    public String getextras() {
        String username = prefs.getString("esetextras", "");
        return username;
    }


    public void setcommision(String username) {
        prefs.edit().putString("commision", username).commit();
    }

    public String getcommision() {
        String username = prefs.getString("commision", "");
        return username;
    }


    public void setstatus(String username) {
        prefs.edit().putString("status", username).commit();
    }

    public String getstatus() {
        String username = prefs.getString("status", "");
        return username;
    }


    public void setaddress(String username) {
        prefs.edit().putString("address", username).commit();
    }

    public String getaddress() {
        String username = prefs.getString("address", "");
        return username;
    }


    public void setcity(String username) {
        prefs.edit().putString("city", username).commit();
    }

    public String getcity() {
        String username = prefs.getString("city", "");
        return username;
    }


    public void setlocality(String username) {
        prefs.edit().putString("locality", username).commit();
    }

    public String getlocality() {
        String username = prefs.getString("locality", "");
        return username;
    }


    public void setreferral(String username) {
        prefs.edit().putString("referral", username).commit();
    }

    public String getreferral() {
        String username = prefs.getString("referral", "");
        return username;
    }


    public void setstorename(String username) {
        prefs.edit().putString("storename", username).commit();
    }

    public String getstorename() {
        String username = prefs.getString("storename", "");
        return username;
    }


    public void setcategory(String username) {
        prefs.edit().putString("storecategory", username).commit();
    }

    public String getcategory() {
        String username = prefs.getString("storecategory", "");
        return username;
    }

    public void setapprovalstatus(String username) {
        prefs.edit().putString("approvalstatus", username).commit();
    }

    public String getapprovalstatus() {
        String username = prefs.getString("approvalstatus", "");
        return username;
    }


    public void settemp(String username) {
        prefs.edit().putString("temp", username).commit();
    }

    public String gettemp() {
        String username = prefs.getString("temp", "");
        return username;
    }


    public void setloc(String username) {
        prefs.edit().putString("loc", username).commit();
    }

    public String getloc() {
        String username = prefs.getString("loc", "");
        return username;
    }


    public void setsubmitted(String username) {
        prefs.edit().putString("submitted", username).commit();
    }

    public String getsubmitted() {
        String username = prefs.getString("submitted", "");
        return username;
    }


    public void seta(String username) {
        prefs.edit().putString("a", username).commit();
    }

    public String geta() {
        String username = prefs.getString("a", "");
        return username;
    }

    public void setb(String username) {
        prefs.edit().putString("b", username).commit();
    }

    public String getb() {
        String username = prefs.getString("b", "");
        return username;
    }

    public void setc(String username) {
        prefs.edit().putString("c", username).commit();
    }

    public String getc() {
        String username = prefs.getString("c", "");
        return username;
    }

    public void setd(String username) {
        prefs.edit().putString("d", username).commit();
    }

    public String getd() {
        String username = prefs.getString("d", "");
        return username;
    }

    public void sete(String username) {
        prefs.edit().putString("e", username).commit();
    }

    public String gete() {
        String username = prefs.getString("e", "");
        return username;
    }

    public void setf(String username) {
        prefs.edit().putString("f", username).commit();
    }

    public String getf() {
        String username = prefs.getString("f", "");
        return username;
    }

    public void setg(String username) {
        prefs.edit().putString("g", username).commit();
    }

    public String getg() {
        String username = prefs.getString("g", "");
        return username;
    }

    public void seth(String username) {
        prefs.edit().putString("h", username).commit();
    }


    public String geth() {
        String username = prefs.getString("h", "");
        return username;
    }

    public void seti(String username) {
        prefs.edit().putString("i", username).commit();
    }

    public String geti() {
        String username = prefs.getString("i", "");
        return username;
    }

    public void setj(String username) {
        prefs.edit().putString("j", username).commit();
    }

    public String getj() {
        String username = prefs.getString("j", "");
        return username;
    }



    public void setapprovalfirst(String username) {
        prefs.edit().putString("approvalfirst", username).commit();
    }

    public String getapprovalfirst() {
        String username = prefs.getString("approvalfirst", "");
        return username;
    }








}
