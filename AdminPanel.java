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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

@SuppressWarnings("serial")
public class AdminPanel extends JFrame{
	private JTree tree;
	private Group root;
	private String activeGroup;
	private String activeUser;
	
	public AdminPanel(){
		root = new Group("Root");
		activeGroup = "Root";
		
		this.setTitle("Mini Twitter - Sean Zahrae - CS356");
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		run();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		
	}

	private void run() {
        JPanel panel = new JPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);


        DefaultTreeModel treeModel = new DefaultTreeModel(root.getUserTreeNode(), true);
        tree = new JTree(treeModel);
        tree.setPreferredSize(new Dimension(200, 300));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setSelectionRow(0);
        
        tree.addTreeSelectionListener((TreeSelectionEvent) -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            if (!node.getAllowsChildren()) {
                activeUser = (String) node.getUserObject();
                node = (DefaultMutableTreeNode) node.getParent();
            } else {
                activeUser = null;
            }
            activeGroup = (String) node.getUserObject();
        });

        JScrollPane scrollPane = new JScrollPane(tree);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 8;
        constraints.fill = GridBagConstraints.VERTICAL;
        panel.add(scrollPane, constraints);
        
        //username and group
        JTextField userName = new JTextField("User ID");
        userName.setPreferredSize(new Dimension(150, 25));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        panel.add(userName, constraints);

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener((ActionEvent) -> updateTree(new User(userName.getText()), (Group) root.getUser(activeGroup, root)));
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel.add(addUserButton, constraints);

        JTextField groupName = new JTextField("Group ID");
        groupName.setPreferredSize(new Dimension(150, 25));
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(groupName, constraints);

        JButton addGroup = new JButton("Add Group");
        addGroup.addActionListener((ActionEvent) -> updateTree(new Group(groupName.getText()), (Group) root.getUser(activeGroup, root)));
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(addGroup, constraints);

        JButton openUserView = new JButton("Open User View");
        openUserView.addActionListener((ActionEvent) -> {
            if (activeUser != null) {
                User user = root.getUser(activeUser, root);
                user.getUserView(root);
            }
            else{
            	System.out.println("User not selected");
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(openUserView, constraints);
        
        
        //option buttons
        JButton userTotals = new JButton("Show User Total");
        userTotals.addActionListener((ActionEvent) -> JOptionPane.showMessageDialog(this, "Number of Users: " + getVisitor().getTotalUsers()));
        constraints.insets = new Insets(200, 5, 5, 5);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        panel.add(userTotals, constraints);

        JButton groupTotals = new JButton("Show Group Total");
        groupTotals.addActionListener((ActionEvent) -> JOptionPane.showMessageDialog(this, "Number of Groups: " + getVisitor().getTotalGroups()));
        constraints.gridx = 2;
        constraints.gridy = 3;
        panel.add(groupTotals, constraints);

        JButton tweetTotals = new JButton("Show Messages Total");
        tweetTotals.addActionListener((ActionEvent) -> JOptionPane.showMessageDialog(this, "Number of Messages: " + getVisitor().getTotalTweets()));
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridheight = 1;
        panel.add(tweetTotals, constraints);

        JButton positiveTotals = new JButton("Show Positive Percentage");
        positiveTotals.addActionListener((ActionEvent) -> JOptionPane.showMessageDialog(this, "Percentage of Positive Messages: " + getVisitor().getPositives() + "%"));
        constraints.gridx = 2;
        constraints.gridy = 4;
        panel.add(positiveTotals, constraints);
        
        add(panel);
        
	}
	
	private void updateTree(User user, Group group){
	if (group != null){
		group.add(user,root);
		DefaultTreeModel model = new DefaultTreeModel(root.getUserTreeNode(), true);
		tree.setModel(model);
        }
	}
	public UserVisitor getVisitor(){
        UserVisitor visitor = new UserVisitor();
		root.accept(visitor);
		return visitor;
	}
	
}
