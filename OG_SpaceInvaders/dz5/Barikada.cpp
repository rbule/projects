#include "Barikada.h"

Barikade::Barikade() {
	Reset();
}

Barikade::~Barikade(){}

void Barikade::Reset() {
	sf::Vector2f pocetak(85,600);
	for (int i = 0;i < 4;i++) {
		pozicije.push_back(std::make_pair(pocetak,3));
		pocetak.x += 220;
	}
}

int Barikade::VratiBrojZivota(int i) {
	return pozicije[i].second;
}

void Barikade::SmanjiBrojZivota(int i) {
	pozicije[i].second--;
	if (pozicije[i].second == 0)
		pozicije.erase(std::remove(pozicije.begin(), pozicije.end(), pozicije[i]),pozicije.end());
}

void Barikade::Unisti(int i) {
	if (i==-1) return;
	for (int j = 0;j < pozicije.size();j++)
		if (pozicije[j].first == sf::Vector2f(85 + i * 220, 600))
			pozicije.erase(std::remove(pozicije.begin(), pozicije.end(), pozicije[j]), pozicije.end());
}

bool Barikade::Pogoden(sf::Vector2f metak) {
	auto velicina = pozicije.size();
	for (int i = 0;i < velicina;i++) {
		if (metak.x >= pozicije[i].first.x && metak.x <= pozicije[i].first.x + 100
			&& metak.y >= pozicije[i].first.y && metak.y <= pozicije[i].first.y + 80) {
			SmanjiBrojZivota(i);
			return true;
		}
	}
		
	return false;
}

int Barikade::UnutarBarikade(sf::Vector2f pozicija) {
	for (int i = 0;i < pozicije.size();i++) {
		if (pozicija.x >= pozicije[i].first.x && pozicija.x <= pozicije[i].first.x + 100
			&& pozicija.y >= pozicije[i].first.y && pozicija.y <= pozicije[i].first.y + 80) {
			if (pozicije[i].first.x == 85) return 0;
			else if (pozicije[i].first.x == 85 + 220) return 1;
			else if (pozicije[i].first.x == 85 + 440) return 2;
			else return 3;
		}
	}
	return -1;
}

void Barikade::Crtaj(Prozor* p) {
	auto velicina = pozicije.size();
	for (int i = 0;i < velicina;i++) {
		barikada.setPosition(pozicije[i].first);
		sf::Texture t;
		t.loadFromFile("barikada.png");
		barikada.setTexture(t);
		p->crtaj(barikada);
	}
}