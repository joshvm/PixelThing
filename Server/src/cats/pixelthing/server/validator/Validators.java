package cats.pixelthing.server.validator;

public final class Validators {

    public static final Validator NAME_VALIDATOR = (out, str) -> {
        if(str.length() < 3 || str.length() > 20)
            out.append("Name must be [3-20] characters in length\n");
        if(str.contains(" "))
            out.append("Name can't contain any white spaces\n");
        for(final char c : str.toCharArray()){
            if(!Character.isLetterOrDigit(c)){
                out.append("Name can only contain letters and digits\n");
                break;
            }
        }
        if(str.equalsIgnoreCase("server"))
            out.append("Name cannot be equal to server");
    };

    public static final Validator PASS_VALIDATOR = (out, str) -> {
        if(str.length() < 6 || str.length() > 20)
            out.append("Password must be [6-20] characters in length\n");
        if(str.contains(" "))
            out.append("Password can't contain any white spaces\n");
        int symbolCount = 0, letterCount = 0, digitCount = 0;
        for(final char c : str.toCharArray()){
            if(Character.isLetter(c))
                letterCount++;
            else if(Character.isDigit(c))
                digitCount++;
            else if(!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c))
                symbolCount++;
        }
        if(symbolCount < 1)
            out.append("Password must contain at least 1 symbol\n");
        if(letterCount < 1)
            out.append("Password must contain at least 1 letter\n");
        if(digitCount < 1)
            out.append("Password must contain at least 1 digit\n");
    };

    private Validators(){}

    public static String validate(final Validator validator, final String str){
        final StringBuilder builder = new StringBuilder();
        validator.validate(builder, str);
        return builder.toString().trim();
    }
}
