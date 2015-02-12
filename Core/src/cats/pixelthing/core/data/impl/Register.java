package cats.pixelthing.core.data.impl;

import cats.pixelthing.core.data.Data;
import cats.pixelthing.core.data.EventID;

public class Register extends Data {

    public Register(final String name, final String pass){
        super(EventID.REGISTER, name, pass);
    }

    public String name(){
        return (String) args[0];
    }

    public String pass(){
        return (String) args[1];
    }
}
