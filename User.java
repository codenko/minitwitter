package minitwitter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;

public class User implements ModelObserver, ModelComponent, ModelVisitable{
	private String userID;
	private Map<String, ModelObserver> followers;
	private Map<String, ModelObserver> followings;
	private List<String> newsfeed;
	private UserView view;
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	
	//create a new user
	public User(String userid){
		this.userID = userid;
		followers = new HashMap<>();
		followings = new HashMap<>();
		newsfeed = new ArrayList<>();
	}
	
	//add a follower
	public void addFollower(User user){
		if (!followers.containsKey( user.getID() )){
			followers.put(user.getID(), user);
		}
		else{
			System.out.print("No User found");
		}
	}

	public void updateFollowing(User followed){
		//follow yourself
		if (followed.getID().equals(userID)){
			return;
		}
		followings.put(followed.getID(), followed);
		followed.addFollower(this);
	}

	public void tweet(String tweet){
		Date date = new Date();
		newsfeed.add(dateFormat.format(date) + " " + userID + ": " + tweet);
		notifyFollowers(userID);
	}
	
    public void notifyFollowers(String userId) {
		//update every follower in hashmap
		for (Map.Entry<String, ModelObserver> observer : followers.entrySet()) {
            User user = (User) observer.getValue();
            user.update(userId, newsfeed.get(newsfeed.size() - 1));
        }
		
	}

    //getters and setters
    public String getID() {
        return userID;
    }

    public Map<String, ModelObserver> getFollowed() {
        return followings;
    }

    public List<String> getNewsfeed() {
        return newsfeed;
    }

    public void getUserView(Group group) {
        if (view == null) {
            view = new UserView(this, group);
        } else {
            view.setVisible(true);
        }
    }

    //models
    @Override
    public void accept(ModelVisitor visitor) {
        visitor.getUserInfo(this);
    }
    
    @Override
    public DefaultMutableTreeNode getUserTreeNode() {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(userID);
        node.setAllowsChildren(false);
        return node;
    }
    
    @Override
    public void update(String userId, String tweet) {
        if (followings.containsKey(userId) || userId.equals(userID)) {
            newsfeed.add(tweet);
            view.updateNewsFeed();
        }
    }






}
