package de.thm.iem.swtproject17.jumpnrun.core;

interface GameVector {
	
	//Diese Methoden sind zur Modifikation eines bestehenden Vektors gut sie modifizieren this:
	//vektor1.add( vektor2 ) würde vektor1 modifizieren und zurückgeben
	abstract GameVector add( GameVector gv ); //addiert den Vektor gv zu dem Vektor this und gibt this zurück
	abstract GameVector sub( GameVector gv ); //subtrahiert gv von this und gibt this zurück
	abstract GameVector subrem( GameVector gv ); //subtrahiert gv von this - der Wert von this kann maximal auf 0 sinken
	abstract GameVector mul( GameVector gv ); //multipliziert gv mit this und gibt this zurück
	abstract GameVector mul( double m ); //multipliziert eine Zahl m mit thi und gibt this zurück
	abstract GameVector div( GameVector gv ); //dividiert this durch gv und gibt this zurück
	abstract GameVector div( double m ); //dividiert this durch die Zahl m und gibt this zurück
	abstract GameVector abs(); //setzt alle Koordinaten von this auf positiv und gibt this zurück
	
	//Diese Methoden geben einen neuen Vektor zurück und Modifizieren keinen der Vektoren
	abstract GameVector addc( GameVector gv ); //wie oben
	abstract GameVector subc( GameVector gv ); //wie oben
	abstract GameVector subremc( GameVector gv ); //wie oben
	abstract GameVector mulc( GameVector gv ); //wie oben
	abstract GameVector mulc( double m ); //wie oben
	abstract GameVector divc( GameVector gv ); //wie oben
	abstract GameVector divc( double m ); //wie oben
	abstract GameVector absc(); //wie oben
	
	abstract double magnitude(); //gibt den Betrag eines Vektors zurück
	abstract boolean lequal( GameVector gv ); //bestimmt ob alle Werte des Vektors this kleiner oder gleich des Vektors gv sind
	
	abstract double[] getCoords(); //gibt die Liste der Werte des Vektors aus
	abstract void setCoords( double[] coords2 ); //setzt die Werte des Vektors
	abstract void setCoords( GameVector gv ); //setzt die Werte eines Vektors von einem anderen Vektor
	abstract GameVector clone(); //klont diesen Vektor
}
