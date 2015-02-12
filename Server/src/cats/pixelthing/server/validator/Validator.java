package cats.pixelthing.server.validator;

public interface Validator {

    public void validate(final StringBuilder builder, final String string);
}
