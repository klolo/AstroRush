[![Build Status](https://travis-ci.org/travis-ci/travis-web.svg?branch=master)](https://travis-ci.org/travis-ci/travis-web)

[Test coverage](https://codecov.io/gh/klolo/AstroRush/)

# AstroRush 2.0
![ikona](https://raw.githubusercontent.com/klolo/AstroRush/master/core/src/main/resources/assets/banner.png "")

Platformowa gra przygodowa z elementami logiki napisana w Javie 8, w całości darmowa i z otwartymi kodami. Docelowe
platformy na jakie gra będzie udostepniona:
-Windows
-Android
-Linux


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
- libgdx (https://libgdx.badlogicgames.com/)
- Lombok (https://projectlombok.org/) - pozwala dzieki adnotatacjom w latwy sposob generowac gettery, settery, dodawac loggery itp.
- Box2d (http://box2d.org/) - biblioteka do symulacji fizyki 2D.


##DevGuide

1. Dodawanie logiki do obiektu
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

2. Komunikacja między obiektami
    Każdy obiekt który w edytorze będzie miał ustawiony identyfikator bądź fizykę zostanie podczas ładowania mapy
    zarejestrowany w singletonie  ObjectsRegister, z którego inne obiekty mogą odwoływac się do innych obiektów, wyszukując
    je metodami: getObjectByID, getObjectByBody

3. Obsługa properties
    3.1 Ustawienia
    3.2 Komunikaty
