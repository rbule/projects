#include <iostream>
#include <SFML/Graphics.hpp>
#include "Igra.h"

sf::Time Igra::protekloVrijeme() {
	return vrijeme;
}
void Igra::restartSata() {
	vrijeme = sat.restart();
	vrijemeGlavni += vrijeme;
	vrijemePucanje += vrijeme;
}

void Igra::renderiraj() {
	p.ocisti();
	Prozor* proz = dohvatiProzor();
	B.Crtaj(proz);
	B.CrtajScore(proz);
	P.Crtaj(proz);
	G.Crtaj(proz);
	M.Crtaj(proz);
	bar.Crtaj(proz);
	if (pauza) CrtajPauzu(proz);
	p.prikazi();
}

void Igra::update() {
	p.update();
	stisnutEscape = sf::Keyboard::isKeyPressed(sf::Keyboard::Escape);
	if (!pauza) {
		float vrijemeIteracije = 1.0f / 250;
		if (vrijeme.asSeconds() >= vrijemeIteracije) {
			ProvjeriSudare();
			MetciIzvanIgre();
			ZabioUBarikadu();
			ProlazGlavnog();
			PokretIzvanzemaljaca();
			IzvanzemaljacPuca();
			B.Pomakni(B.VratiSmjer(), vrijeme.asSeconds());
			M.Putuj(vrijeme.asSeconds());
			P.Pomakni(vrijeme.asSeconds());
			G.Pomakni(vrijeme.asSeconds());
			vrijeme -= sf::seconds(vrijemeIteracije);
			if (P.ProvjeraDolaskaDolje())
				B.Izgubio();
			if (B.JelIzgubio()) {
				B.Reset();
				P.Reset();
				bar.Reset();
			}
		}
	}
	else{
		vrijemeGlavni = sat.restart();
		vrijemePucanje = sat.restart();
	}
}

void Igra::obradiUlaz() {
	if ((sf::Keyboard::isKeyPressed(sf::Keyboard::Left) || sf::Keyboard::isKeyPressed(sf::Keyboard::A))
		&& B.VratiPoziciju().x >= 0)
		B.PostaviSmjer(Smjer::Lijevo);
	if ((sf::Keyboard::isKeyPressed(sf::Keyboard::Right) || sf::Keyboard::isKeyPressed(sf::Keyboard::D))
		&& B.VratiPoziciju().x <= 855)
		B.PostaviSmjer(Smjer::Desno);
	if ((sf::Keyboard::isKeyPressed(sf::Keyboard::Space) || sf::Keyboard::isKeyPressed(sf::Keyboard::P)) && B.SmijeLiPucati())
		IgracPucaj();
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Escape) && !stisnutEscape) {
		stisnutEscape = true;
		Pauza();
	}
}

void Igra::IgracPucaj() {
	M.NoviMetak(B.VratiPoziciju()+sf::Vector2f(39,-10), true);
	B.MijenjajSmijePucati(false);
}

void Igra::ProvjeriSudare() {
	for (auto it = M.PocetniIterator();it != M.ZadnjiIterator();it++) {
		bool jelPogodio = false;
		Tip T;
		if (it->second && (T=P.Pogoden(it->first))!=Tip::None && !jelPogodio) {
			switch (T)
			{
			case Tip::Mali:
				B.PovecajScore(10);
				break;
			case Tip::Srednji:
				B.PovecajScore(20);
				break;
			case Tip::Veliki:
				B.PovecajScore(30);
				break;
			}
			it = M.UnistiMetak(it);
			jelPogodio = true;
			B.MijenjajSmijePucati(true);
		}
		if (!jelPogodio && B.Pogoden(it->first)) {
			it=M.UnistiMetak(it);
			jelPogodio = true;
		}
		if (!jelPogodio && bar.Pogoden(it->first)) {
			if (M.IgracevMetak(it)) B.MijenjajSmijePucati(true);
			it = M.UnistiMetak(it);
			jelPogodio = true;
		}
		if (!jelPogodio && G.Pogoden(it->first)) {
			B.PovecajScore(rand() % 50);
			B.MijenjajSmijePucati(true);
			it = M.UnistiMetak(it);
		}
		if (it == M.ZadnjiIterator())
			break;
	}
}

void Igra::MetciIzvanIgre() {
	for (auto it = M.PocetniIterator();it != M.ZadnjiIterator();it++) {
		if (it->second && M.IzasoIzvan(it))
			B.MijenjajSmijePucati(true);
		if (it == M.ZadnjiIterator())
			break;
	}
}

void Igra::PokretIzvanzemaljaca() {
	if (P.Najlijeviji().x <= 10)
		P.PromijeniLijevoDesno(2);
	else if (P.Najdesniji().x >= 870)
		P.PromijeniLijevoDesno(1);
	if (P.DovoljnoPomaknutDolje()) P.PromijeniLijevoDesno(0);
	switch (P.VratiLijevoDesno())
	{
	case 0:
		P.PostaviSmjer(SmjerProtivnik::Desno);
		break;
	case 1:
		P.PostaviSmjer(SmjerProtivnik::Lijevo);
		break;
	case 2:
		P.PostaviSmjer(SmjerProtivnik::Dolje);
		P.PovecajBrzinu();
		break;
	}
}

void Igra::ZabioUBarikadu() {
	sf::Vector2f pocetak(85,600);
	
	for (auto it = P.Pocetak();it != P.Kraj();it++) {
		sf::Vector2f donjeDesno = it->first + sf::Vector2f(50, 50);
		sf::Vector2f donjeLijevo = it->first + sf::Vector2f(0, 50);
		for (int j = 0;j < 4;j++) {
			bar.Unisti(bar.UnutarBarikade(donjeDesno));
			bar.Unisti(bar.UnutarBarikade(donjeLijevo));
		}
		pocetak.x += 220;
	}
}

void Igra::ProlazGlavnog() {
	if (vrijemeGlavni.asSeconds() >= rand() % 25 + 10) {
		G.Kreni();
		vrijemeGlavni -= sf::seconds(rand() % 25 + 10);
	}
	if (G.VratiPoziciju().x > 900) {
		G.Premjesti();
	}
}

void Igra::Pauza() {
	pauza = (pauza == true) ? false : true;
}

void Igra::IzvanzemaljacPuca() {
	if (P.VratiBrojZivih() == 0) return;
	int i = rand() % P.VratiBrojZivih();
	if (vrijemePucanje.asSeconds() >= rand() % 5 + 1) {
		M.NoviMetak(P.VratiPoziciju(i) + sf::Vector2f(25, 75), false);
		vrijemePucanje -= sf::seconds(rand() % 5 + 1);
	}
}

void Igra::CrtajPauzu(Prozor* p) {
	sf::RectangleShape Pauza;
	Pauza.setPosition(890, 100);
	Pauza.setSize(sf::Vector2f(80, 20));
	Pauza.setRotation(270);
	Pauza.setFillColor(sf::Color(255, 255, 255, 180));
	p->crtaj(Pauza);
	Pauza.setPosition(850, 100);
	p->crtaj(Pauza);
}

Igra::Igra() : p("Space Invaders", sf::Vector2u(930, 800)) {
	pauza = false;
	stisnutEscape = false;
}

Igra::~Igra() {}
