package minitwitter;

import java.util.Map;

public class UserVisitor implements ModelVisitor{
	private int totalusers;
	private int totaltweets;
	private int totalpositives;
	private int totalgroups;
	
	public UserVisitor(){
		totalgroups = 1;
		totalusers = 0;
	}
	private void setTotals(Map.Entry<String, User> user){
		totalusers++;
		
		for (String msg : user.getValue().getNewsfeed()){
			String currentmsg = msg.toLowerCase();
			if (currentmsg.contains(user.getValue().getID().toLowerCase())){
				totaltweets++;
				if (currentmsg.contains("good") || currentmsg.contains("great") || currentmsg.contains("excellent")){
					totalpositives++;
				}
			}
		}
	}
	
	//getters and setters
	public int getTotalUsers(){
		return totalusers;
	}
	
	public int getTotalGroups(){
		return totalgroups;
	}
	
	public int getTotalTweets(){
		return totaltweets;
	}
	
	public double getPositives(){
		return Math.round((((totalpositives * 1.0) / totaltweets) * 100) * 100) / 100.0;
	}

	@Override
	public int getUserInfo(User user) {
		return user.getNewsfeed().size();
	}

	@Override
	public void getGroupInfo(Group group) {
		for (Map.Entry<String, User> user : group.getusers().entrySet()) {
            if (user.getValue() instanceof Group) {
                totalgroups++;
                this.getGroupInfo((Group) user.getValue());
            } else {
                setTotals(user);
            }
        }
		
	}
}
