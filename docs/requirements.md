# Generator fiszek i nie tylko

Celem projektu jest stworzenie aplikacji wspierającej naukę języków obcych
w zakresie m.in. słownictwa, morfologii i syntaksy.

Praca została podzielona na 3 kamienie milowe. W każdym z nich zespół ma
za zadanie dostarczyć aplikację spełniającą przedstawione wymagania.

## Kamień milowy 1
Podstawowa aplikacja, która przyjmie dłuższy tekst i na jego podstawie
wygeneruje zestaw fiszek w standardowym formacie, który można zaimportować
do różnych aplikacji.
### Wymagania funkcjonalne
* Eksport fiszek do formatu zgodnego z zewnętrznymi aplikacjami.
> Większość aplikacji akceptuje format CSV, gdzie w pierwszej kolumnie jest słowo, 
> a w drugiej jego tłumaczenie. Przykładowo:
> ```
> tree,drzewo
> apple,jabłko
> ```
* Przyjmowanie bloku tekstu w obcym języku.
* Odpytywanie użytkownika o tłumaczenia kolejnych słów.
* Interaktywny interfejs użytkownika.
* Wsparcie znaków Unicode (alfabety niełacińskie).
* Wsparcie języków pisanych od prawej do lewej (RTL).
* (Ręczne tworzenie pojedynczych fiszek.)
### Wymagania niefunkcjonalne
* Aplikacja napisana w obiektowym języku programowania (preferowana Java).
* Kod napisany zgodnie ze sztuką na rok 2024.
* Pokrycie testami jednostkowymi na poziomie _co najmniej_ 75%.
* Istnieją podstawowe testy funkcjonalne.
* Błędy spowodowane przez użytkownika lub stan systemu są obsługiwane.
* Kod korzysta z wzorców obiektowych i dobrych praktyk tam gdzie to możliwe.
* Statyczna analiza kodu nie wykazuje błędów, "zapachów" (ang. _smells_)
ani złych praktyk.
* Istnieje udokumentowany styl kodu.
* Sposób uruchomienia oraz obsługi jest udokumentowany i prosty.
* Zawiera podstwową dokumentację. Przykłady:
  * sensowny changelog,
  * diagram klas modelu danych,
  * diagram komponentów,
  * diagramy przepływu,
  * diagram sekwencji,
  * wysokopoziomowy opis architektury,
  * FAQ,
  * itp.

## Kamień milowy 2
W tym etapie dodajemy funkcjonalności związane z morfologią oraz fonetyką słów. 
Ponadto stworzymy podstawową warstwę persystencji. 

### Wymagania funkcjonalne:
* Podział słów na części mowy.
* Formy fleksyjne.
* Wyświetlanie tekstu w interlinii.
* (Transliteracja) i transkrypcja.
* Eksportowanie tego do csv w kolejnych kolumnach.
* Persystencja form słownikowych.

## Kamień milowy 3
Celem tego kroku jest umożliwienie wygenerowania pliku PDF lub graficznego na podstawie zadanego tekstu. 
W pliku tym znajdzie się przygotowany wcześniej (i rozbudowany o analizę syntaksy) tekst interlinearny 
wraz zaznaczeniem "metadanych" poszczególnych słów.

### Wymagania funkcjonalne
* Tekst generuje się do pliku PDF lub PNG.
* Po wprowadzeniu tekstu dla każdego słowa pytamy o dotychczasowe infomacje o nim oraz o to jaką jest częścią zdania.
* Na podstawie informacji o części mowy wybór części zdania jest ograniczony lub zbędny.
* (Wsparcie dla orzeczeń imiennych/nominalnych np. `Adam jest (łącznik) dyrektorem (orzecznik).`)
* Rozpoznawanie kropki jako końca zdania.
* Wsparcie dla zdań złożonych rozdzielonych spójnikami. Wystarczy obsługa zdań o niemalejącym poziomie zagnieżdzenia.
* Zdania podrzędne powinny być poprzedzone wcięciem, a współrzędne być na tym samym poziomie zagłębienia.
* Ograniczone do języków indoeupejskich.

### Wymagania niefunkcjonalne
* Wspierane języki mogą korzystać ze statycznych plików konfiguracyjnych określających występujące w nim części mowy, 
informację czy się deklinują (odmiana "rzeczownikopodobna") lub koniungują (odmiana "czasownikopodobna").
