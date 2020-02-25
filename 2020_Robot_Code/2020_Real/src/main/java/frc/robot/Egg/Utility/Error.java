package frc.robot.Egg.Utility;
import javax.swing.*;

public class Error extends JOptionPane {
	
	//This is error class.  It allows a user to call new Error("Message", ErrorType) to create
	//a neat error popup
	
	private static final long serialVersionUID = -7123583803352886038L;

	
	public Error(String message, ErrorType errorType)  {
				
		switch (errorType) {
		case Fatal:
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			break;
		case NonFatal:
			JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
			break;
		case Information:
			JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.DEFAULT_OPTION);
			break;
		case Temporary:
			JOptionPane messagePane = new JOptionPane(
		            message,
		            JOptionPane.INFORMATION_MESSAGE);
			final JDialog dialog = messagePane.createDialog(this, "Temporary");

		      new SwingWorker<Void, Void>() {

		         @Override
		         protected Void doInBackground() throws Exception {
		            Thread.sleep(1000); 

		            return null;
		         }
		         
		         protected void done() {
		            dialog.dispose();
		         };
		      }.execute();

		      dialog.setVisible(true);
		      break;
		default:
    	    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			break;
		}
	}
	
	public Error() {
		
	}
	
	public static Boolean YesNo(String Message) {
		int dialogResult = JOptionPane.showConfirmDialog(null, Message, "Warning", JOptionPane.YES_NO_OPTION);
		if(dialogResult == 0) {
		  return true;
		} else {
		  return false;
		} 
	}
}
