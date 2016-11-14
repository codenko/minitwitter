package minitwitter;

public interface ModelVisitor {
	int getUserInfo(User user);
	void getGroupInfo(Group group);
}
