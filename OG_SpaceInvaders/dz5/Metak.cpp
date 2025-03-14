#include "Metak.h"

Metak::Metak() {}

Metak::~Metak(){}

sf::Vector2f Metak::VratiPoziciju(int i) {
	return pozicije[i].first;
}

void Metak::NoviMetak(sf::Vector2f novaPozicija, bool igracevMetak) {
	pozicije.push_back(std::make_pair(novaPozicija, igracevMetak));
}

bool Metak::IzasoIzvan(Vektor::iterator &it) {
	if (it->first.y > 70 && it->first.y < 800)
		return false;
	it=pozicije.erase(it);
	return true;
}

int Metak::BrojMetaka() {
	return pozicije.size();
}

bool Metak::IgracevMetak(Vektor::iterator it) {
	return it->second;
}

Vektor::iterator Metak::PocetniIterator() {
	return pozicije.begin();
}

Vektor::iterator Metak::ZadnjiIterator() {
	return pozicije.end();
}


void Metak::Putuj(float koliko) {
	auto velicina = pozicije.size();
	if (velicina == 0) return;
	for (int i = 0;i < velicina;i++) {
		if (pozicije[i].second)
			pozicije[i].first.y -= 300*koliko;
		else
			pozicije[i].first.y += 300*koliko;
	}
}

Vektor::iterator Metak::UnistiMetak(Vektor::iterator it) {
	return pozicije.erase(it);
}

void Metak::Crtaj(Prozor* p) {
	auto velicina = pozicije.size();
	if (velicina == 0) return;
	for (int i = 0;i < velicina;i++) {
		if (pozicije[i].second) {
			metak.setPosition(pozicije[i].first);
			metak.setSize(sf::Vector2f(17,3));
			metak.setFillColor(sf::Color::Green);
			metak.setRotation(90);
		}
		else {
			metak.setPosition(pozicije[i].first);
			metak.setSize(sf::Vector2f(20,3));
			metak.setFillColor(sf::Color::White);
			metak.setRotation(90);
		}
		p->crtaj(metak);
	}
}