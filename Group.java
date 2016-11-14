package minitwitter;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

public class Group extends User implements ModelComponent, ModelVisitable{
	private Map<String, User> users;
	
	public Group(String groupID){
		super(groupID);
		users = new HashMap<>();
	}
	
	public boolean userExists(String userID, Group usergroup){
		if (usergroup.getID().equals(userID) || usergroup.getusers().containsKey(userID)) {
            return true;
        }

		for (Map.Entry<String, User> group : usergroup.getusers().entrySet()) {
            if (group.getValue() instanceof Group && userExists(userID, (Group) group.getValue())) {
                return true;
            }
        }
        return false;
	}
	
	public void add(User user, Group group){
		if (!userExists(user.getID(), group)) {
            users.put(user.getID(), user);
        } else {
            System.out.println("Error:" + user.getID() + " already exists");
        }
		
	}

    
    //getters and setters
    public Map<String, User> getusers() {
        return users;
    }
    
    public User getUser(String id, Group group) {
        if (group.getID().equals(id)) {
            return group;
        }
        else if (group.getusers().containsKey(id)) {
            return group.getusers().get(id);
        }

        for (Map.Entry<String, User> grouptwo : group.getusers().entrySet()) {
            if (grouptwo.getValue() instanceof Group && userExists(id, (Group) grouptwo.getValue())) {
                return getUser(id, (Group) grouptwo.getValue());
            }
        }
        return null;
    }

    @Override
    public DefaultMutableTreeNode getUserTreeNode() {
        DefaultMutableTreeNode group = new DefaultMutableTreeNode(super.getID());
        for (Map.Entry<String, User> user : users.entrySet()) {
            group.add(user.getValue().getUserTreeNode());
        }
        return group;
    }

    //Visitor Pattern
    @Override
    public void accept(ModelVisitor visitor) {
        visitor.getGroupInfo(this);
    }
}
