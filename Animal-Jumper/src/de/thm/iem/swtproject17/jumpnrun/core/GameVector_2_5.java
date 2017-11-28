package de.thm.iem.swtproject17.jumpnrun.core;

/* Eine Vektor Klasse um Berechnungen durchzuführen
 * 2.5 Dimensional d.h. Hat 2 echte Dimensionen speichert aber noch zusätzlich die Z-Reihenfolge ab.
 * Für genauere Informationen im Bezug auf die Methoden siehe das GameVector Interface.
 */

public class GameVector_2_5 implements GameVector {

	private double[] coords;
	
	public GameVector_2_5( double x, double y, double z ) {
		coords = new double[] { x ,y ,z };
	}
	
	@Override
	public GameVector add( GameVector gv ) {
		double[] coords2 = gv.getCoords();
		coords[0] += coords2[0];
		coords[1] += coords2[1];
		return this;
	}

	@Override
	public GameVector sub( GameVector gv ) {
		double[] coords2 = gv.getCoords();
		coords[0] -= coords2[0];
		coords[1] -= coords2[1];
		return this;
	}
	
	@Override
	public GameVector subrem( GameVector gv ) {
		double[] coords2 = gv.getCoords();
		coords[0] = Math.max( coords[0] - coords2[0], 0 );
		coords[1] = Math.max( coords[1] - coords2[1], 0 );
		return this;
	}

	@Override
	public GameVector mul(GameVector gv) {
		double[] coords2 = gv.getCoords();
		coords[0] *= coords2[0];
		coords[1] *= coords2[1];
		return this;
	}
	
	@Override
	public GameVector mul( double m ) {
		coords[0] *= m;
		coords[1] *= m;
		return this;
	}
	
	@Override
	public GameVector div(GameVector gv) {
		double[] coords2 = gv.getCoords();
		coords[0] /= coords2[0];
		coords[1] /= coords2[1];
		return this;
	}
	
	@Override
	public GameVector div( double m ) {
		coords[0] /= m;
		coords[1] /= m;
		return this;
	}
	
	@Override
	public GameVector abs() {
		coords[0] = Math.abs( coords[0] );
		coords[1] = Math.abs( coords[1] );
		return this;
	}
	
	@Override
	public GameVector addc(GameVector gv) {
		double[] coords2 = gv.getCoords();
		return new GameVector_2_5( coords[0] + coords2[0], coords[1] + coords2[1], coords[2] );
	}

	@Override
	public GameVector subc(GameVector gv) {
		double[] coords2 = gv.getCoords();
		return new GameVector_2_5( coords[0] - coords2[0], coords[1] - coords2[1], coords[2] );
	}
	
	@Override
	public GameVector subremc( GameVector gv ) {
		double[] coords2 = gv.getCoords();
		return new GameVector_2_5( Math.max( coords[0] - coords2[0], 0 ), Math.max( coords[1] - coords2[1], 0 ), coords[2] );
	}

	@Override
	public GameVector mulc(GameVector gv) {
		double[] coords2 = gv.getCoords();
		return new GameVector_2_5( coords[0] * coords2[0], coords[1] * coords2[1], coords[2] );
	}
	
	@Override
	public GameVector mulc( double m ) {
		return new GameVector_2_5( coords[0] * m, coords[1] * m, coords[2] );
	}
	
	@Override
	public GameVector divc(GameVector gv) {
		double[] coords2 = gv.getCoords();
		return new GameVector_2_5( coords[0] / coords2[0], coords[1] / coords2[1], coords[2] );
	}
	
	@Override
	public GameVector divc( double m ) {
		return new GameVector_2_5( coords[0] / m, coords[1] / m, coords[2] );
	}
	
	@Override
	public GameVector absc() {
		return new GameVector_2_5( Math.abs( coords[0] ), Math.abs( coords[1] ), coords[2] );
	}
	
	@Override
	public double[] getCoords() {
		return coords.clone();
	}
	
	@Override
	public void setCoords( double[] coords2 ) {
		for ( int i = 0; i<3&&i<coords2.length ; i++ ) {
			coords[i] = coords2[ i ];
		}
	}
	
	@Override
	public void setCoords( GameVector gv ) {
		setCoords( gv.getCoords() );
	}
	
	@Override
	public GameVector clone() {
		return new GameVector_2_5( coords[0], coords[1], coords[2] );
	}

	@Override
	public double magnitude() {
		return Math.sqrt( Math.pow(coords[0], 2 ) + Math.pow(coords[1], 2) );
	}

	@Override
	public boolean lequal(GameVector gv) {
		double[] coords2 = gv.getCoords();
		return coords[0] <= coords2[0] && coords[1] <= coords2[1];
	}
	
}
