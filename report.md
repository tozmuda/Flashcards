# Opis Architektury

Celem projektu jest aplikacja umożliwiająca użytkownikowi stworzenie talii fiszek poprzez interaktywny interfejs. Użytkownik ma możliwość wygenerowania listy fiszek do uzupełnienia z tekstu i wprowadzania ich tłumaczeń, oraz ręcznego dodawania nowych fiszek. Ostatecznie może pobrać plik CSV z gotową talią fiszek.

![diagram HLA](img\HLAdiagram.jpg)

### Wykorzystane technologie

- Frontend:

  - Typescript
  - React
  - NPM

- Backend:
  - Java
  - Gradle
  - Spring

# Changelog

#### Kamień milowy 3:

- Piotr Albiński, Piotr255
  - poprawienie elementów pakietu filegenerator (testy) , tworzenie pdf i testy powiązane
- Przemysław Zieliński
  - frontend - dopasowanie requestów do nowych wymagań, przesuwanie elementów do wybrania poziomów zagnieżdżenia
  - parser zwracający słowa oraz interpunkcję
- Tomasz Żmuda, tozmuda
  - Pakiet do wczytywania języków
  - Poprawki do pakietu text
  - poprawki do testów, optymalizacja SonarQube
- Michał Tomaszewski
  - Sentence, SimpleSentence, TextElement, Word, Punctuation
  - aktualizacja DeckControllerTest
- Adam Konior, Stiffo805
  - Deck, DeckService, DeckController, DeckTest, DeckServiceTest

#### Kamień milowy 2:

- Piotr Albiński, Piotr255
  - zmiany w CSV, + modyfikacja testów, napisanie metod w service, controller do zapisu decka + testy, stworzenie testu persystencji przy kontrolerze do zapisu decka
- Przemysław Zieliński
  - frontend
- Tomasz Żmuda, tozmuda
  - Dodanie połączenia z bazą danych i stworzenie do tego instrukcji
  - Dodanie poprawek w testach, aktualizacja nieaktualnych testów
- Michał Tomaszewski
  - zmiany w Deck i Flashcard z testami
  - PartOfSpeech
- Adam Konior, Stiffo805
  - PartOfSpeechController, testy do DeckController, DeckService, PartOfSpeech, PartOfSpeechController

#### Kamień milowy 1:

- Piotr Albiński, Piotr255
  - zajmowałem się pakietem csv
  - dodałem klasę konfiguracyjną gdzie można pisać @Beany
  - stworzyłem pakiet do łatwiejszej i uogólnionej obsługi błędów w endpointach
  - napisałem testy do csv i do obsługi błędu
  - diagram sekwencji
- Przemysław Zieliński
  - stworzenie całego frontendu (fetch, obsługa błędów)
  - konfiguracja CORS na backendzie
  - parser z testami
  - wysłanie CSV z backendu jako byte[] (ustawienie headerów itp.) oraz odbiór na frontendzie
- Tomasz Żmuda, tozmuda
  - kontroler i serwis zmieniający zdania na wyrazy w pakiecie text oraz napisanie do nich testów
  - napisanie testów funkcjonalnych
- Michał Tomaszewski
  - model Deck i Flashcard z testami
  - opis architektury
- Adam Konior, Stiffo805
  - serwis i kontroler odpowiadające za zwracanie i dodawanie fiszek oraz za resetowanie talii fiszek(DeckService, DeckController, DeckServiceTest, DeckControllerTest)
  - diagram sekwencji

# Diagramy

Diagram sekwencji:
!["diagram sekwencji"](img\diagram_sekwencji2.jpg)

Diagram aktywności:
!["diagram aktywnosci"](img\diagram_aktywnosci1.jpg)

# Co było trudne:

- Zapoznanie się ze sposobem testowania bardziej skomplikowanych elementów sprawiło trudności, są różne standardy i nie jest prosto od razu się z tym rozeznać.
- Sonarqube bardzo dokładnie wykrywa błędy, jest niewątpliwie pomocnym narzędziem, jednak sprawiał pewne problemy podczas instalacji i konfiguracji z projektem.
- w Parserze: trudnością było stworzenie wyrażenia regularnego, które poprawnie rozdziela słowa dla wszystkich alfabetów
