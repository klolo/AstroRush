[![Build Status](https://travis-ci.org/travis-ci/travis-web.svg?branch=master)](https://travis-ci.org/travis-ci/travis-web)
[![Coverage Status](https://coveralls.io/repos/github/klolo/AstroRush/badge.svg?branch=master)](https://coveralls.io/github/klolo/AstroRush?branch=master)


#AstroRush 2.0
![ikona](https://raw.githubusercontent.com/klolo/AstroRush/master/core/src/main/resources/assets/splash.png "")

Platformowa gra przygodowa z elementami logiki napisana w Javie 8.


1. [Uruchomienie](#run)
2. [Budowa aplikacji](#paragraph1)
    1. [Silnik](#subparagraph1)
    2. [Fizyka](#subparagraph1)
    3. [Properties](#subparagraph1)
3. [Integracja z Overlap](#paragraph2)
4. [Fabuła](#paragraph2)
5. [Dodatkowe informacje](#addinfo)
    1. [PMD](#pmd)

##Uruchomienie <a name="run">
1. Projekt importujemu do IntelliJ
2. Podczas importu zostanie wykryty projekt Gradle, należy włączyć tą naturę.
3. Zbudowanie i uruchomienie aplikacji odbywa się za pomocą tasku w oknie gradle: game->other->run


##Budowa aplikacji
###Mapy
Gra korzysta z map wygenerowanych za pomocą edytora Overlap2D,
oraz własnego runtime dla tych map. Dokumentacja dotycząca tworzenia takiego
runtime: http://overlap2d.com/data-api-creating-custom-runtime/

Wymagana wersja edytora: 0.1.3 (developerska)
Informacje jak używać edytora: http://overlap2d.com/getting-started/

###Moduły

####Core
Moduł zawiera implementacje gry, nie zawiera natomiast elementów zależnych od systemu na którym ma zostać uruchomiona gra.
Najwazniejsze pakiety jakie wchodzą w klad modułu:

- overlap_runtime - Moduł zawiera integracje z mapami wygenrowanymi za pomocą Overlap2d. Jest to wczytywanie np. obiektów graficznych
i fizyki dla nich.

- script - Klasy javy jakie są podpinane do obiketów w edytorze za pomocą custom var (pole logic, zawierające nazwę klasy bez pakietu)

- engine

####Desktop
- desktop- implementacja gry AstroRush.



##Używane biblioteki
- Lombok (https://projectlombok.org/) - pozwala dzieki adnotatacjom w latwy sposob generowac gettery, settery, dodawac loggery itp.
- Box2d (http://box2d.org/) - biblioteka do symulacji fizyki 2D.
- libgdx (https://libgdx.badlogicgames.com/)


##TODO
- [ ] Skonfigurować checkstyle i formatter..
- [ ] Zrobić diagram klas
- [ ] Odkładanie logów do pliku
- [ ] Draft fabuły
- [X] Dokończenie runtime dla Overlap2D
- [X] Przygotowac task gradle do zbudwania standalone jar.
    - [ ] Dodac reguly pmd
- [X] Zrobić testy jednostkowe
    - [ ] Udroznic coberture
    - [ ] Docelowe minimalne pokrycie testami 60%
- [ ] Przygotować wersję pod przeglądarkę
- [ ] Przygotować wersję pod Androida
- [ ] Menu gry skladajace sie z kilku ekranow
- [ ] Savy gry
- [ ] Wybrać czcionke do gry
- [ ]

## CREDITS:
Graphics: www.kenney.nl