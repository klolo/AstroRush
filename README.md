#AstroRush 2.0
![ikona](https://github.com/klolo/AstroRush/blob/master/game%2Fsrc%2Fmain%2Fresources%2Fassets%2Fico.png "")

Platformowa gra przygodowa z elementami logiki napisana w Javie 8.

##Instrukcja uruchomienia
1. Projekt importujemu do IntelliJ
2. Podczas importu zostanie wykryty projekt Gradle, należy włączyć tą naturę.
3. Zbudowanie i uruchomienie aplikacji odbywa się za pomocą tasku w oknie gradle: game->other->run


##Budowa aplikacji
Gra korzysta z map wygenerowanych za pomocą edytora Overlap2D,
oraz własnego runtime dla tych map. Dokumentacja dotycząca tworzenia takiego
runtime: http://overlap2d.com/data-api-creating-custom-runtime/


##Używane biblioteki
- Lombok (https://projectlombok.org/) - pozwala dzieki adnotatacjom w latwy sposob generowac gettery, settery, dodawac loggery itp.
- Box2d () - biblioteka do symulacji fizyki 2D.
- libgdx


##TODO
- [ ] Skonfigurować checkstyle i formatter..
- [ ] Zrobić diagram klas
- [ ] Odkładanie logów do pliku
- [ ] Draft fabuły
- [ ] Dokończenie runtime dla Overlap2D
- [ ] Przygotowac task gradle do zbudwania standalone jar.
- [ ] Zrobić testy jednostkowe


## CREDITS:
Graphics: www.kenney.nl