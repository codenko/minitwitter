package minitwitter;

public interface ModelObserver {
	void update(String userId, String tweet);
}
