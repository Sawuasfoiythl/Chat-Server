/**
*    The following Code is hereby copyrighted by Myles Lumsden, so FUCK OFF JAMIE, COCO, GAVIN, ANDIE, and everyone else
*/

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame
{
	static JTextField username = new JTextField();
	static JTextField password = new JPasswordField();

	public static ChatClient cc;

	public Login()
	{

		Object[] msg = {"Permission Level, leave blank if unknown:", username, "Password:", password};

		JOptionPane op = new JOptionPane(
				msg,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION,
				null,
				null);

		JDialog dialog = op.createDialog(this, "Enter Permission Level...");
		dialog.setVisible(true);

		int result = JOptionPane.OK_OPTION;

		try
		{
			result = ((Integer)op.getValue()).intValue();
		}
		catch(Exception uninitializedValue)
		{}

		if(result == JOptionPane.OK_OPTION)
		{

			String permission = new String(username.getText());
			String password1 = new String(password.getText());

			if(permission.equals("dev"/*eveloper"*/) && password1.equals("aidenpearce"/*"you wee scriptkiddie"*/))
			{
				ChatClient.permissions = "programmer";
				System.out.println(ChatClient.permissions);
				String[] justatemp = {permission,password1}; ChatClient.main(justatemp);
			} else 
				if(permission.equals("super-admin") && password1.equals("raichu"))
				{
					ChatClient.permissions = "superadmin";
					System.out.println(ChatClient.permissions);
					String[] justatemp = {permission,password1}; ChatClient.main(justatemp);
				} else 
					//to troll connor and gav as they know the pass
					if(permission.equals("super-admin") && password1.equals("scrafty"))
					{
						JOptionPane option = new JOptionPane("Scrafty Used High-Jump Kick \nIt was super effective \nThe user fainted");
						JDialog dialog1 = option.createDialog(this, "Permission Level");
						dialog1.setVisible(true);
					} else 
					if(permission.equals("admin") && password1.equals("i am useless"))
					{
						ChatClient.permissions = "admin";
						System.out.println(ChatClient.permissions);
						String[] justatemp = {permission,password1}; ChatClient.main(justatemp);
					} else 
						if(permission.equals("") && password1.equals(""))
						{
							ChatClient.permissions = "default";
							System.out.println(ChatClient.permissions);
							String[] justatemp = {permission,password1}; ChatClient.main(justatemp);
						} else {



							ChatClient.permissions = "default";
							System.out.println(ChatClient.permissions);
							String[] justatemp = {permission,password1}; ChatClient.main(justatemp);
						}

					}
					else
					{
						System.out.println("Canceled");
						System.exit(-1);
						//ChatClient.permissions = "default";
						//System.out.println(ChatClient.permissions);
						//String[] justatemp = new String[2]; ChatClient.main(justatemp);
					}

				}


				public static void main(String[] args)
				{
					JFrame login = new Login();
					login.setDefaultCloseOperation( EXIT_ON_CLOSE );
				}
			}