package de.thm.iem.swtproject17.jumpnrun.core;

public class Cam extends MovingEntity {

	private Entity target;
	private GameVector screenSize;
	private int limitedDimension;
	private double limitedMin, limitedMax;
	private boolean limited = false;
	
	
	public Cam( Entity toFocus ) {
		focusOn( toFocus );
	}
	
	public void focusOn( Entity toFocus ) {
		if ( toFocus == null )
			new IllegalArgumentException( "Cam can't focus on nothing - Cam needs to focus on something" );
		target = toFocus;
	}
	
	public void setSize( GameVector screenSize ) {
		this.screenSize = screenSize;
	}
	
	public GameVector getSize() {
		return screenSize;
	}
	
	public void setMovementLimitations( int dimension, double min, double max ) {
		limitedDimension = dimension;
		limitedMin = min;
		limitedMax = max;
		limited = true;
	}
	
	public void setMovementLimitations() {
		limited = false;
	}
	
	public void updateCameraLogic() {
		getPosition().setCoords( target.getPosition() );
		if ( limited ) {
			double[] limitedArray = getPosition().getCoords();
			double limitedValue = limitedArray[ limitedDimension ];
			if ( limitedValue > limitedMax ) 
				limitedValue = limitedMax;
			else if ( limitedValue < limitedMin ) {
				limitedValue = limitedMin;
			} else
				return;
			limitedArray[ limitedDimension ] = limitedValue;
			getPosition().setCoords( limitedArray );
		}
		//sehr basic aber sollte für uns klappen
	}
	
	@Override
	PhysicalBorder getHitbox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	GameVector getHitboxSize() {
		// TODO Auto-generated method stub
		return null;
	}

}
