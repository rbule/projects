#include "Brod.h"

Brod::Brod() {
	Reset();
}

Brod::~Brod(){}

sf::Vector2f Brod::VratiPoziciju() {
	return pozicija;
}

int Brod::VratiScore() {
	return score;
}

int Brod::VratiZivote() {
	return brZivota;
}

Smjer Brod::VratiSmjer() {
	return smjer;
}



bool Brod::JelIzgubio() {
	return izgubio;
}

bool Brod::SmijeLiPucati() {
	return smijePucati;
}

void Brod::SmanjiBrojZivota(){
	brZivota--;
	if (brZivota == 0)
		Izgubio();
}


void Brod::Izgubio() {
	izgubio = true;
}

void Brod::MijenjajSmijePucati(bool p) {
	smijePucati = p;
}

void Brod::Reset() {
	pozicija = sf::Vector2f(430,740); 
	PostaviSmjer(Smjer::None);
	brZivota = 3;
	score = 0;
	izgubio = false;
	smijePucati = true;
}

void Brod::PostaviSmjer(Smjer s) {
	smjer = s;
}

void Brod::PovecajScore(int povecaj) {
	score += povecaj;
}

void Brod::Pomakni(Smjer s,float koliko) {
	switch (s) {
	case Smjer::Lijevo:
		pozicija.x -= 200*koliko;
		break;
	case Smjer::Desno:
		pozicija.x += 200*koliko;
		break;

	}
	PostaviSmjer(Smjer::None);
}

bool Brod::Pogoden(sf::Vector2f metak) {
	if (metak.x >= pozicija.x && metak.x <= pozicija.x + 75
		&& metak.y >= pozicija.y && metak.y <= pozicija.y + 40) {
		SmanjiBrojZivota();
		return true;
	}
	return false;	
}

void Brod::Crtaj(Prozor *p) {
	sf::Texture t;
	t.loadFromFile("igrac.png");
	brod.setTexture(t);
	brod.setPosition(pozicija);
	p->crtaj(brod);
}

void Brod::CrtajScore(Prozor* p) {
	sf::Text t;
	sf::Font f;
	f.loadFromFile("arial.ttf");
	t.setFont(f);
	t.setString("SCORE: " + std::to_string(score));
	t.setFillColor(sf::Color::White);
	t.setCharacterSize(30);
	t.setPosition(650, 10);
	p->crtaj(t);
	sf::RectangleShape linija;
	linija.setPosition(0, 68);
	linija.setFillColor(sf::Color::Green);
	linija.setSize(sf::Vector2f(930,2));
	p->crtaj(linija);
	sf::Texture text;
	text.loadFromFile("igrac.png");
	brod.setTexture(text);
	if (brZivota >= 1) {
		brod.setPosition(15, 10);
		p->crtaj(brod);
	}
	if (brZivota >= 2) {
		brod.setPosition(100, 10);
		p->crtaj(brod);
	}
	if (brZivota == 3) {
		brod.setPosition(185, 10);
		p->crtaj(brod);
	}
}