package ro.cs.pub.pubsub.message;

public class LoggingMessageContent implements MessageContent {
	private static final long serialVersionUID = 1L;

	private String message;

	public LoggingMessageContent(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message;
	}
}
