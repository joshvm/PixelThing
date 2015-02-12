package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;

public class Login extends Data {

    public Login(final String name, final String pass){
        super(EventID.LOGIN, name, pass);
    }

    public String name(){
        return (String) args[0];
    }

    public String pass(){
        return (String) args[1];
    }
}
