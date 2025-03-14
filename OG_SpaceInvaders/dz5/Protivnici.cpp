#include "Protivnici.h"

Protivnici::Protivnici() {
	Reset();
}

Protivnici::~Protivnici() {}

sf::Vector2f Protivnici::VratiPoziciju(int i) {
	return pozicije[i].first;
}

Tip Protivnici::VratiTip(int i) {
	return pozicije[i].second;
}

SmjerProtivnik Protivnici::VratiSmjer(){
	return smjer;
}

float Protivnici::VratiBrzinu() {
	return brzina;
}

int Protivnici::VratiLijevoDesno() {
	return LijevoDesno;
}

int Protivnici::VratiBrojZivih() {
	return pozicije.size();
}

void Protivnici::PromijeniLijevoDesno(int novo) {
	// 0 znaci da nisu jos pogodili ni jedan zid, 1 znaci pogodili su jedan zid i vracaju se natrag,
	// 2 znaci da ih treba spustiti dolje
	LijevoDesno = novo;
}

void Protivnici::Reset() {
	pozicije.clear();
	brzina = 10;
	smjer = SmjerProtivnik::None;
	LijevoDesno = 0;
	sf::Vector2f pocetni(50, 140);
	prviPrije = pocetni;
	for (int i = 0;i < 5;i++) {
		for (int j = 0;j < 11;j++) {
			if (i == 0)
				pozicije.push_back(std::make_pair(pocetni, Tip::Veliki));
			else if (i == 1 || i == 2)
				pozicije.push_back(std::make_pair(pocetni, Tip::Srednji));
			else
				pozicije.push_back(std::make_pair(pocetni, Tip::Mali));
			pocetni.x += 70;
		}
		pocetni.x = 50;
		pocetni.y += 65;
	}
}

void Protivnici::PovecajBrzinu() {
	brzina += 0.7;
}

void Protivnici::PostaviSmjer(SmjerProtivnik s) {
	smjer = s;
}

bool Protivnici::ProvjeraDolaskaDolje() {
	if (pozicije.size() == 0) return false;
	auto velicina = pozicije.size();
	if (Najdonji().y >= 650) 
		return true;
	return false;
}

void Protivnici::Pomakni(float koliko) {
	auto velicina = pozicije.size();

	for (int i = 0;i < pozicije.size();i++)
		switch (smjer) {
		case SmjerProtivnik::None:
			break;
			return;
		case SmjerProtivnik::Lijevo:
			pozicije[i].first.x -= brzina*koliko;
			break;
		case SmjerProtivnik::Desno:
			pozicije[i].first.x += brzina*koliko; 
			break;
		case SmjerProtivnik::Dolje:
			pozicije[i].first.y += brzina*koliko; 
			break;
		}
	if (smjer != SmjerProtivnik::Dolje && pozicije.size()>0)
		prviPrije = pozicije[0].first;
}

sf::Vector2f Protivnici::Najlijeviji() {
	sf::Vector2f najljeviji(1000,0);
	auto velicina = pozicije.size();
	for (int i = 0;i < velicina;i++) {
		if (pozicije[i].first.x < najljeviji.x)
			najljeviji = pozicije[i].first;
	}
	return najljeviji;
}

sf::Vector2f Protivnici::Najdesniji() {
	sf::Vector2f najdesniji(-1,0);
	auto velicina = pozicije.size();
	for (int i = 0;i < velicina;i++) {
		if (pozicije[i].first.x > najdesniji.x)
			najdesniji = pozicije[i].first;
	}
	return najdesniji;
}

sf::Vector2f Protivnici::Najdonji() {
	sf::Vector2f najdonji(0, 0);
	auto velicina = pozicije.size();
	for (int i = 0;i < velicina;i++) {
		if (pozicije[i].first.y > najdonji.y)
			najdonji = pozicije[i].first;
	}
	return najdonji;
}

VektorProtivnici::iterator Protivnici::Pocetak() {
	return pozicije.begin();
}

VektorProtivnici::iterator Protivnici::Kraj() {
	return pozicije.end();
}

bool Protivnici::DovoljnoPomaknutDolje() {
	if (pozicije.size() == 0) return false;
	if (pozicije[0].first.y - prviPrije.y>= 25) {
		return true;
	}
	return false;
}

 Tip Protivnici::Pogoden(sf::Vector2f metak) {
	auto velicina = pozicije.size();
	for (int i = 0;i < velicina;i++) {
		if (metak.x >= pozicije[i].first.x && metak.x <= pozicije[i].first.x + 50
			&& metak.y >= pozicije[i].first.y && metak.y <= pozicije[i].first.y + 50) {
			Tip t = pozicije[i].second;
			pozicije.erase(std::remove(pozicije.begin(), pozicije.end(), pozicije[i]), pozicije.end());
			return t;
		}
	}
	return Tip::None;
}

void Protivnici::Crtaj(Prozor* p) {
	auto velicina = pozicije.size();
	sf::Texture t;
	for (int i = 0;i < velicina;i++) {
		protivnik.setPosition(pozicije[i].first);
		switch (pozicije[i].second) {
		case Tip::Mali:
			t.loadFromFile("tipMali.png");
			protivnik.setTexture(t);
			p->crtaj(protivnik);
			break;
		case Tip::Srednji:
			t.loadFromFile("tipSrednji.png");
			protivnik.setTexture(t);
			p->crtaj(protivnik);
			break;
		case Tip::Veliki:
			t.loadFromFile("tipVeliki.png");
			protivnik.setTexture(t);
			p->crtaj(protivnik);
			break;
		}
	}

}

Glavni::Glavni() {
	pozicija = sf::Vector2f(-100, 75);
	glavni.setPosition(-100, 75);
}

Glavni::~Glavni(){}

sf::Vector2f Glavni::VratiPoziciju() {
	return pozicija;
}

float Glavni::VratiBrzinu() {
	return brzina;
}

void Glavni::PovecajBrzinu(float povecaj) {
	brzina += povecaj;
}

void Glavni::Pomakni(float koliko) {
	pozicija.x += brzina*koliko;
}

bool Glavni::Pogoden(sf::Vector2f metak) {
	if (glavni.getGlobalBounds().contains(metak)) {
		pozicija = sf::Vector2f(-100, 75);
		glavni.setPosition(-100,75);
		brzina = 0;
		return true;
	}
	return false;
}

void Glavni::Kreni() {
	brzina = 300;
}

void Glavni::Premjesti() {
	pozicija.x = -100;
	brzina = 0;
}

void Glavni::Crtaj(Prozor* p) {
	sf::Texture t;
	t.loadFromFile("glavni.png");
	glavni.setTexture(t);
	glavni.setPosition(pozicija);
	p->crtaj(glavni);
}

