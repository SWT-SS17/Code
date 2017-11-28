package de.thm.iem.swtproject17.jumpnrun.core;

/* Eine enumeration welche die M�glichen Formen einer HITBOX darstellt
 */

public enum PhysicalBorder { //kann entweder
	HITBOX, //ein Rechteck sein oder
	HITELLIPSE; //eine Ellipse sein
	//null ist auch noch m�glich und bedeutet keinerlei Kollisionen
}