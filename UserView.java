package minitwitter;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class UserView extends JFrame{
	private User user;
	private Group group;
	private JList<String> following;
	private JList<String> newsfeed;
	
	public UserView(User user, Group group){
		this.user = user;
		this.group = group;
		
		this.setTitle(user.getID());
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		run();
		updateFollowing();
		updateNewsFeed();
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void run(){
		JPanel panel = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);

        JTextField userName = new JTextField("User ID");
        userName.setPreferredSize(new Dimension(150, 25));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(userName, constraints);

        JButton followUser = new JButton("Follow User");
        followUser.setPreferredSize(new Dimension(150, 25));
        followUser.addActionListener((ActionEvent) -> {
            User followedUser = group.getUser(userName.getText(), group);
            if (followedUser != null) {
                user.updateFollowing(followedUser);
                updateFollowing();
            }
            else{
            	System.out.println(userName.getText() + " does not exist");
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(followUser, constraints);

        following = new JList<>();
        JScrollPane scrollPane = new JScrollPane(following);
        following.setPreferredSize(new Dimension(300, 250));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(scrollPane, constraints);

        JTextField tweet = new JTextField("Tweet Message");
        tweet.setPreferredSize(new Dimension(150, 25));
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(tweet, constraints);

        JButton postMessage = new JButton("Post Message");
        postMessage.setPreferredSize(new Dimension(150, 25));
        postMessage.addActionListener((ActionEvent) -> {
            user.tweet(tweet.getText());
            updateNewsFeed();
        });
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(postMessage, constraints);

        String[] news = user.getNewsfeed().toArray(new String[user.getNewsfeed().size()]);
        newsfeed = new JList<>(news);
        JScrollPane messageScrollPane = new JScrollPane(newsfeed);
        newsfeed.setPreferredSize(new Dimension(300, 250));
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(messageScrollPane, constraints);

        add(panel);
	}
	public void updateFollowing() {
	    DefaultListModel<String> model = new DefaultListModel<String>();
	    for (Map.Entry<String, ModelObserver> followed : user.getFollowed().entrySet()) {
	        model.addElement(followed.getKey());
	    }
	    following.setModel(model);
	}

	public void updateNewsFeed() {
	    DefaultListModel<String> model = new DefaultListModel<String>();
	    for (String tweet : user.getNewsfeed()) {
	        model.addElement(tweet);
	    }
	    newsfeed.setModel(model);
	}
}
