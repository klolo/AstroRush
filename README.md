[![Build Status](https://travis-ci.org/travis-ci/travis-web.svg?branch=master)](https://travis-ci.org/klolo/AstroRush.svg?branch=master)
[![codecov](https://codecov.io/gh/klolo/AstroRush/branch/master/graph/badge.svg)](https://codecov.io/gh/klolo/AstroRush)

# Free adventure Java game by Kamil Lolo
![banner](https://raw.githubusercontent.com/klolo/AstroRush/master/core/src/main/resources/assets/banner.png "")

1. [Uruchomienie](#run)
2. [Budowa aplikacji](#build)
    1. [Silnik](#engine) 
    2. [Fizyka](#physics)
    3. [Mapa](#map)
    4. [Moduł Core](#core)
    5. [Moduł Desktop](#desktop)
    6. [Używane biblioteki](#library)
3. [Devguide](#devguide)
    1. [Dodawanie logiki do obiektu>](#addLogic)
    2. [Komunikacja między obiektami ](#communicationBeetwenObjects)
    3. [Obsługa properties](#properties)
    4. [Testy](#test)
    5. [Docelowe platformy](#platforms)
4. [Donate](#donate)

##Uruchomienie <a name="run">
1. Projekt importujemu do IntelliJ
2. Podczas importu zostanie wykryty projekt Gradle, należy włączyć tą naturę.
3. Zbudowanie i uruchomienie aplikacji odbywa się za pomocą tasku w oknie gradle: game->other->run
4. Alternatywnie w przypadku korzystania z konsoli wystarczy w katalogu projektu wydać polecenie: gradle run

## Budowa aplikacji   <a name="build">

### Silnik <a name="engine">
Gra wykorzystuje autorskich silnik zbudowany w oparciu o bibliotekę Libgdx, Box2d oraz Overlap2d.
Silnik ten pozwala na ładowanie map stworzonych w edytorze Overlap2D. Z poziomu edytora do stworzonych
obiektów na mapie można podpiąć logikę napisaną w Javie. Uproszczoną budowę aplikacji przedstawia 
poniższy schemat:

![silnik gry](https://raw.githubusercontent.com/klolo/AstroRush/master/doc/gameStructure.png "")

### Fizyka <a name="physics">
Biblioteką symulującą fizykę w grze jest biblioteka Box2D. To czy dany obiekt ma posiadać fizykę w grze,
oraz jakiego kształtu będzie jego ciało fizyczne definiuje się z poziomu edytora Overlap2D bez konieczności napisania
choćby linijki kodu.

### Mapa  <a name="map">
Gra korzysta z map wygenerowanych za pomocą edytora Overlap2D,
oraz własnego runtime dla tych map. Dokumentacja dotycząca tworzenia takiego
runtime: http://overlap2d.com/data-api-creating-custom-runtime/

Wymagana wersja edytora: 0.1.3 (developerska)
Informacje jak używać edytora: http://overlap2d.com/getting-started/

#### Moduł Core <a name="core">
Moduł zawiera implementacje gry, nie zawiera natomiast elementów zależnych od systemu na którym ma zostać uruchomiona gra.
Najwazniejsze pakiety jakie wchodzą w klad modułu:

- overlap_runtime - Moduł zawiera integracje z mapami wygenrowanymi za pomocą Overlap2d. Jest to wczytywanie np. obiektów graficznych
i fizyki dla nich.

- script - Klasy javy jakie są podpinane do obiketów w edytorze za pomocą custom var (pole logic, zawierające nazwę klasy bez pakietu)

- engine

#### Moduł Desktop <a name="desktop">
- desktop- implementacja gry AstroRush.

## Używane biblioteki <a name="library">
- libgdx (https://libgdx.badlogicgames.com/)
- Lombok (https://projectlombok.org/) - pozwala dzieki adnotatacjom w latwy sposob generowac gettery, settery, dodawac loggery itp.
- Box2d (http://box2d.org/) - biblioteka do symulacji fizyki 2D.

## DevGuide <a name="devguide">

#### Dodawanie logiki do obiektu <a name="addLogic">
Z poziomu edytora Overlap2D należy ustawić customsVars o nazwie logic,
gdzie wartością jest nazwa klasy która zostanie podpięta pod dany obiekt. Wszystkie
klasy z logiką obiektów znajdują się w pakiecie zdefiniowanym w properties
(plik: game.properties, pole: game.script.package). Podpięcie nastepuje podczas
ładowanie mapy. Każda klasa zawierająca logikę powinna implementować interfejs ILogic
i opcjonalnie rejestrować się w zarządcy kolizji (CollisionManager), poprzez ustawienie consumera:
    ```java
    runAnimation.getData().setCollisionConsumer(collisionProcesor::processCollision);
    ```
    Każdy skrypt logiki wymaga zaimplementowania metody:
    ```java
    void setGameObject(IGameObject gameObject)
    ```
gdzie gameObject to obiekt do którego podpięty jest skrypt. Może on zostać zrcutowany w zależności
od rodzaju elementu na np. TextureObject bądz tez AnimationObject.

#### Komunikacja między obiektami <a name="communicationBeetwenObjects">
Każdy obiekt który w edytorze będzie miał ustawiony identyfikator bądź fizykę zostanie podczas ładowania mapy
zarejestrowany w singletonie  ObjectsRegister, z którego inne obiekty mogą odwoływac się do innych obiektów, wyszukując
je metodami: getObjectByID, getObjectByBody
    
#### Obsługa properties <a name="properties">
3.1 Ustawienia
3.2 Komunikaty
    
### Testy <a name="test">
Testy w aplikacji znajdują się w katalogu core/src/test, aktualny poziom pokrycia dostępny jest do sprawdzenia:
    [test coverage](https://codecov.io/gh/klolo/AstroRush/)

Dodatkowo w celu podniesienia jakości kodu w projekcie używany jest PMD, który w przypadku kodu
nie zgodnego z regułami nie skompiluje go.

#### Docelowe platformy <a name="platforms">
Platformowa gra przygodowa z elementami logiki napisana w Javie 8, w całości darmowa i z otwartymi kodami. Docelowe
platformy na jakie gra będzie udostepniona:
- Windows
- Android
- Linux

## Donate me <a name="donate">
If you like this projects buy me a coffee cup :)