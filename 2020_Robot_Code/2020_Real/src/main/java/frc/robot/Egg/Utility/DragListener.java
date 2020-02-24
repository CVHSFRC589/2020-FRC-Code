package frc.robot.Egg.Utility;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public final class DragListener extends MouseAdapter {
	
	//This is an experimental class that should allow swing components to be dragged.  create
	//a new drag listener then call DragListener.add(component) to use it

	private Point OldPosition;
	private Point TargetLocation;
	private Point LocationOnScreen;
	private JComponent TargetComponent;	
	
	public void Add(Component ComponentToAdd) {
        ComponentToAdd.addMouseListener(this);
        ComponentToAdd.addMouseMotionListener(this);
   }
	
	@Override
    public void mousePressed(final MouseEvent e) {
        OldPosition = MouseInfo.getPointerInfo().getLocation();
        TargetLocation = new Point();
        TargetComponent = (JComponent) e.getComponent();
    }

	@Override
    public void mouseDragged(final MouseEvent e) {
        
        LocationOnScreen = e.getLocationOnScreen();
        TargetLocation = TargetComponent.getLocation(TargetLocation);
        TargetLocation.translate((int) (LocationOnScreen.getX() - OldPosition.getX()), (int) (LocationOnScreen.getY() - OldPosition.getY()));
        TargetComponent.setLocation(TargetLocation);
        OldPosition = MouseInfo.getPointerInfo().getLocation();
    }
	
	@Override
	public void mouseReleased(final MouseEvent e) {
		TargetComponent = null;
	}
    
}

