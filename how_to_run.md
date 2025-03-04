## Instrukcja uruchomienia

Nasz projekt składa się z frontendu i backendu.

##### Backend:
Przed startem samego backendu uruchamiamy bazę danych, my korzystamy z <b>PostgreSQL</b> (download: https://www.postgresql.org/download/, zalecana jest wersja z interfejsem graficznym pgAdmin, zwykle instalowana domyślnie, dla wygodniejszej obsługi). 
Za pomocą PostgreSQL lokalnie tworzymy bazę danych przeznaczoną dla aplikacji i ewentualnie dodatkowego użytkownika do tej bazy (ale niekoniecznie, możemy też skorzystać z domyślnie stworzonego Superusera o nazwie `postgres`)
W katalogu `backend/src/main/resources` znajdujemy plik `user-credentials.properties` i wpisujemy dane naszej bazy danych do odpowiednich pól, na przykład:

```properties
# Port 5432 jest portem domyślnym dla baz Postgre3eSQL
db.port=5432
db.name=flashcards_db
db.user=postgres
db.password=my_secure_password
```

Jeden z testów w klasie `DeckControllerFunctionalTest` testuję persystencję. Lepiej coś takiego wykonywać na drugiej bazie. Stąd jest także konfiguracja do bazy testowej `user-credentials-test.properties`:

```properties
db.port=5432
db.name=flashcards_db_test
db.user=postgres
db.password=my_secure_password
```
Widok baz z perspektywy `pgAdmin4`.

!["databases"](img\databases.png)

**Przed uruchomieniem należy pamiętać, żeby pola plików `properties` dostosować do lokalnych ustawień postgreSQL.** 



Następnie backend można uruchomić w IntelliJ poprzez gotową konfigurację albo w  klasie `MainApplication`. Można też użyć komendy `./gradlew bootRun` będąc w folderze backend. 

##### Frontend:
Frontend uruchamiamy wpisując w folderze frontendu `npm install` i `npm run dev`. Teraz możemy zobaczyć naszą stronę pod adresem http://localhost:5173/.

