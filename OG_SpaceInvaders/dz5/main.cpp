#include <iostream>
#include <SFML/Graphics.hpp>
#include "Igra.h"

int main() {
	srand(time(NULL));
	Igra igra;
	while (!igra.dohvatiProzor()->jelGotov()) {
		igra.obradiUlaz();
		igra.update();
		igra.renderiraj();
		igra.restartSata();
	}
	return 0;
}