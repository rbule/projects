#pragma once

#ifndef METAK_H
#define METAK_H

#include <iostream>
#include <SFML/Graphics.hpp>
#include "Prozor.h"
#include <utility>

typedef std::vector<std::pair<sf::Vector2f, bool>> Vektor;

// za metke i igraca uzimamo istu brzinu 200 piksela po sekundi pa nisam dodao
// float brzina posto ce biti poznata konstanta

class Metak {
private:
	Vektor pozicije;
	sf::RectangleShape metak;
public:
	Metak();
	~Metak();
	sf::Vector2f VratiPoziciju(int);
	bool IgracevMetak(Vektor::iterator);
	int BrojMetaka();
	void NoviMetak(sf::Vector2f, bool);
	bool IzasoIzvan(Vektor::iterator&);
	Vektor::iterator PocetniIterator();
	Vektor::iterator ZadnjiIterator();
	Vektor::iterator UnistiMetak(Vektor::iterator);
	void Putuj(float);
	void Crtaj(Prozor*);
};

#endif