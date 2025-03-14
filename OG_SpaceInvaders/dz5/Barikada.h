#pragma once

#ifndef BARIKADA_H
#define BARIKADA_H

#include <iostream>
#include <SFML/Graphics.hpp>
#include "Prozor.h"
#include <vector>

class Barikade {
private:
	std::vector<std::pair<sf::Vector2f,int>> pozicije;
	sf::Sprite barikada;
public:
	Barikade();
	~Barikade();
	void Reset();
	int VratiBrojZivota(int);
	void SmanjiBrojZivota(int);
	bool Pogoden(sf::Vector2f);
	void Unisti(int);
	int UnutarBarikade(sf::Vector2f);
	void Crtaj(Prozor*);
};

#endif
