package de.thm.iem.swtproject17.jumpnrun.core;

/* Eine enumeration welche die Möglichen Formen einer HITBOX darstellt
 */

public enum PhysicalBorder { //kann entweder
	HITBOX, //ein Rechteck sein oder
	HITELLIPSE; //eine Ellipse sein
	//null ist auch noch möglich und bedeutet keinerlei Kollisionen
}