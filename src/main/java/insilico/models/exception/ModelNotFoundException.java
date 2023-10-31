package insilico.models.exception;

/**
 * Default class for descriptor having a missing value exception 
 * 
 * @author Alberto Manganaro (a.manganaro@kode-solutions.net)
 */
public class ModelNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;


    public ModelNotFoundException() {
            // TODO Auto-generated constructor stub
    }

    public ModelNotFoundException(String message) {
            super(message);
            // TODO Auto-generated constructor stub
    }

    public ModelNotFoundException(Throwable cause) {
            super(cause);
            // TODO Auto-generated constructor stub
    }

    public ModelNotFoundException(String message, Throwable cause) {
            super(message, cause);
            // TODO Auto-generated constructor stub
    }

}
