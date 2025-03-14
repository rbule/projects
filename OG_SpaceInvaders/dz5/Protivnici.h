#pragma once

#ifndef PROTIVNICI_H
#define PROTIVNICI_H

#include <iostream>
#include <SFML/Graphics.hpp>
#include <vector>
#include <utility>
#include "Prozor.h"

enum class Tip{None,Mali,Srednji,Veliki};
enum class SmjerProtivnik{None,Lijevo,Desno,Dolje};

typedef std::vector < std::pair < sf::Vector2f, Tip >> VektorProtivnici;

class Protivnici {
private:
	VektorProtivnici pozicije;
	SmjerProtivnik smjer;
	sf::Sprite protivnik;
	float brzina;
	int LijevoDesno;
	sf::Vector2f prviPrije;
public:
	Protivnici();
	~Protivnici();
	sf::Vector2f VratiPoziciju(int);
	Tip VratiTip(int);
	SmjerProtivnik VratiSmjer();
	int VratiLijevoDesno();
	int VratiBrojZivih();
	void PromijeniLijevoDesno(int);
	void PostaviSmjer(SmjerProtivnik);
	bool ProvjeraDolaskaDolje();
	void Reset();
	void Pomakni(float);
	float VratiBrzinu();
	void PovecajBrzinu();
	VektorProtivnici::iterator Pocetak();
	VektorProtivnici::iterator Kraj();
	sf::Vector2f Najlijeviji();
	sf::Vector2f Najdesniji();
	sf::Vector2f Najdonji();
	bool DovoljnoPomaknutDolje();
	Tip Pogoden(sf::Vector2f);
	void Crtaj(Prozor*);
};

class Glavni {
private:
	sf::Vector2f pozicija;
	float brzina;
	sf::Sprite glavni;
public:
	Glavni();
	~Glavni();
	sf::Vector2f VratiPoziciju();
	float VratiBrzinu();
	void Pomakni(float);
	void Kreni();
	void Premjesti();
	void PovecajBrzinu(float);
	bool Pogoden(sf::Vector2f);
	void Crtaj(Prozor*);
};

#endif