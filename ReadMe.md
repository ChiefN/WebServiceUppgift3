# Vad kan förbättras?
## Data
* Möjligtvis att implementera authorities. Detta om man skulle vilja ha en adminroll som skulle kunna radera alla poster. Kanske för att kunna ta bort olämpligt innehåll.

## Controller
* Istället för att ta emot plain text credentials vid login skulle vi kunna använda oss utav HttpBasic. Detta hade gjort att vi inte skulle behövt skapa
en anpassad AuthenticationManager. Detta hade också gjort att lösenordet och användarnamnet hade varit lite extra skyddat i själva requesten. Basic är väldigt simpelt, en vanlig standard,
och vill någon, kan de lätt avkoda en basicsträng. Men lösenordet skulle iallafall ha lite extra skydd om någon försöker intercepta requesten.
* I vår /register tar vi emot och omvandlar jsonbodyn till en map av object. Vi kallar mapen credentials och kan plocka ut användarnamn och lösenord genom keyn. Å ena sidan skulle vi kunna göra mapen 
till en map av strängar istället men rätt väg hade troligen varit att vi skulle lämnat över denna omvandlingen till Spring genom att skapa en credentials-model. Precis som vi gör i login-endpointen.
Då hade vi själva inte behövt felhantera/validera om rätt inputs fanns i bodyn. 

* I samma metod kollar vi även så att lösenordet och användarnamnet inte är null eller bara whitespace. Vi borde kanske validera att lösenordet/användarnamnet inte får innehålla whitespace alls och i samma veva
skapa validering för att se så att lösenordet är t.ex. minst 10tecken långt, innehåller små och stora lösenord samt ett specialtecken för att öka säkerheten.

* I postcontrollern har vi endpoints för att ta ut posterna i sin helhet samt ta ut "Utdrag". Detta för att smidigt kunna bygga en frontend där man kanske vill kunna lista alla blogposter. Då 
behöver vi inte hämta själva contenten innan någon aktivt vill läsa en specifik post. Dock är metodnamnen/urlerna inte speciellt bra. Ett RESTful api ska vara selfdescriptive. Det ska vara enkelt
att förstå vad endpoints gör och kommer leverera. Så är inte fallet här. All vad? och allTitle är lite intetsägande.

* I admin/update, samma som i en tidigare controller. Här tar vi emot en body. Sedan mappar vi den till en record av typen Content. Vi hade kunnat låta spring sköta detta genom att försöka ta emot
en Content direkt.

* Ett tydligare felmeddelande i admin/update om det inte är rätt ägare som vill uppdatera en post. Detta går att disskutera och i det i koden länkade resurser pratar de om olika strategier.
I vår kod har vi valt strategin att skicka statusen not found. Detta då Not_Authorized kan indikera att man på något sätt skulle kunna få begära åtkomst till denna resurs. Genom att
sätta not found ger vi inte information till en potentiell hacker att den specifika posten faktiskt finns.

## DTO/Model
* Uppdatera klass och filnamnen. Jag tycker att det skulle vara en ganska bra struktur att döpa det vi vill ta emot till model och det vi skickar ut till DTO. 
DTO står för datatransferobject vilket innebär att det inte är fel att använda benämningen dto. Men för tydlighetens skull.

* Vi använder våra DTO till att skicka med Hypermedia. Vi hade därför kunnat bygga våra dtos med som representationmodels. Detta för att öka skalbarheten och underlätta vid ändring av URLer/endpoints.

* Vi har valt att lägga våra "omvandlingsmetoder" som statiska metoder i dtoklassen. Man skulle kunna argumentera för att det skulle vara tydligare att skapa en PostDTOConverter-klass som ansvarade
för alla omvandlingar till och från en viss entitiet.

## Service

## Exception
* Vi skulle kunna bygga ut våra exceptions till att fungera som riktiga exceptions. Liknande UsernameNotFoundException

## Repository
* Dela på databasen som i detta fallet är en in memory(hashmap) Repositoryn ska inte ha ansvar för att skapa databasen. Kanske allra helst implementera en sql databas och aktivera JPA.

## Security
* Vi skulle kunna aktivera CSRF. Skulle vi byggt en MVC skulle vi kunna lagt till vår csrf token direkt i våra formulär. Iochmed att vi byggt ett rest-api
hade vi istället fått skicka med en CSRF token(som sparas i databas för att vara stateless) till frontenden som vi sedan skulle krävt tokenen i alla POST, PUT, DELETE and PATCH requests. Detta skulle förhindra
att requests kommer från någon annan klient än den vi tänkt. I vår app som vi har nu behövs dock CSRF inte då vi använder oss av JWT-tokens som endast sparas i klienten medans och försvinner när vi stänger ner hemsidan.

* Vi skulle kunna lyfta ut vår authenticationmanager till en annan configuration-fil. På det sättet hade vi kunnat sätta proxyBeanMethods till false. Detta innebär att vi endast kan använda metoderna 
vid autowire. Utan att veta säkert gissar jag på att det förbättrar prestandan och minneshanteringen. Det innebär att spring endast måste skapa alla rätta kontakter i buildtime?

# RESTful
## Self descriptive
Bättre och mer genomtänkta endpoints.
## Layered system
Vi har byggt vårt API i lager. Presentation, business, persistance. Det vi skulle kunna göra för att förbättra är att lyfta ut databasen ur persistancelagret för att ha ett databas lager.
Vi skulle också kunna lyfta ut vår autentisiering och jwt-generator till en egen server.
## Cache
Vi använder oss inte utav någon cache
## Hypermedia
Vi skickar med den hypermedia vi behöver. Vi skulle kunna använda oss utav RepresentationModels för att underlätta.
## Stateless
Vi använder oss inte utav states. Klienten begär någonting, får någonting, nästa request samma sak. 
