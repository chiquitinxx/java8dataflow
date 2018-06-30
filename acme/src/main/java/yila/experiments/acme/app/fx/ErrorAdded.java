package yila.experiments.acme.app.fx;

/**
 * JFL 29/6/18
 */
public class ErrorAdded {

    private boolean last;
    private final String errorMessage;

    public ErrorAdded(String errorMessage) {
        this.errorMessage = errorMessage;
        this.last = true;
    }

    public void setLast(boolean value) {
        this.last = value;
    }

    public boolean isLast() {
        return last;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
