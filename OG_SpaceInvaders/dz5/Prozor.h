#pragma once

#include <iostream>
#include <SFML/Graphics.hpp>

#ifndef GLOBALS_H
#define GLOBALS_H

extern int HighScore;

#endif 

class Prozor {
public:
	Prozor();
	Prozor(const std::string&, const sf::Vector2u&);
	void prebaciNaCijeli();
	void ocisti();
	void crtaj(sf::Drawable&);
	void prikazi();
	void update();
	~Prozor();
	bool jelGotov() {
		return gotov;
	}
	bool jelCijeli() {
		return cijeliZaslon;
	}
	sf::Vector2u dohvatiVelicinu() {
		return velicina;
	}
private:
	void Stvori();
	void Unisti();
	void Postavi(const std::string&, const sf::Vector2u&);
	sf::RenderWindow prozor;
	sf::Vector2u velicina;
	std::string naslov;
	bool gotov;
	bool cijeliZaslon;
};