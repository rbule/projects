#pragma once

#ifndef BROD_H
#define BROD_H

#include <iostream>
#include <SFML/Graphics.hpp>
#include "Prozor.h"
#include <string>

enum class Smjer { None, Lijevo, Desno };

// za metke i igraca uzimamo istu brzinu 200 piksela po sekundi pa nisam dodao
// float brzina posto ce biti poznata konstanta

class Brod {
	sf::Vector2f pozicija;
	int brZivota;
	int score;
	Smjer smjer;
	bool izgubio;
	bool smijePucati;
	sf::Sprite brod;
public:
	Brod();
	~Brod();
	sf::Vector2f VratiPoziciju();
	int VratiZivote();
	int VratiScore();
	Smjer VratiSmjer();
	bool JelIzgubio();
	bool SmijeLiPucati();
	void Izgubio();
	void SmanjiBrojZivota();
	void MijenjajSmijePucati(bool);
	void PostaviSmjer(Smjer);
	void Reset();
	void PovecajScore(int);
	void Pomakni(Smjer,float);
	bool Pogoden(sf::Vector2f);
	void Crtaj(Prozor*);
	void CrtajScore(Prozor*);
};

#endif