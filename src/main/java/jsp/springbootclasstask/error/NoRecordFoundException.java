package jsp.springbootclasstask.error;

@SuppressWarnings("serial")
public class NoRecordFoundException extends RuntimeException {
	public NoRecordFoundException(String message) {
		super(message);

	}
}
