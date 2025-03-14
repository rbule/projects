#pragma once

#include <iostream>
#include <SFML/Graphics.hpp>
#include <random>
#include "Prozor.h"
#include "Brod.h"
#include "Protivnici.h"
#include "Metak.h"
#include "Barikada.h"

class Igra {
public:
	Igra();
	~Igra();
	void obradiUlaz();
	void update();
	void renderiraj();
	Prozor* dohvatiProzor() {
		return &p;
	}
	sf::Time protekloVrijeme();
	void restartSata();
	void IgracPucaj();
	void ProvjeriSudare();
	void MetciIzvanIgre();
	void PokretIzvanzemaljaca();
	void ZabioUBarikadu();
	void ProlazGlavnog();
	void Pauza();
	void IzvanzemaljacPuca();
	void CrtajPauzu(Prozor*);
private:
	Prozor p;
	Brod B;
	Metak M;
	Protivnici P;
	Glavni G;
	Barikade bar;
	sf::Clock sat;
	sf::Time vrijeme;
	sf::Time vrijemeGlavni;
	sf::Time vrijemePucanje;
	bool pauza;
	bool stisnutEscape;
};