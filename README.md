# MoviesApp
# Film Uygulaması
Kısa açıklama: Modern Android mimarisiyle yazılmış, film arama ve detay görüntüleme özelliklerine sahip örnek uygulama.
## Teknolojiler ve Araçlar
- Kotlin
- Jetpack Compose (UI)
- Material 3
- AndroidX Navigation (Compose)
- MVVM + Clean Architecture (presentation, domain, data katmanları)
- Use Case katmanı
- Repository deseni
- Hilt (Dependency Injection)
- Kotlin Coroutines (asenkron işlemler)
- Retrofit + OkHttp + JSON dönüştürücü (Gson/Moshi) ile REST API istemcisi
- Sealed class tabanlı UI state ve event yönetimi
- Gradle Kotlin DSL
- Network Security Config (gerekli durumlarda)
- Edge-to-Edge destekli tema/arayüz

Not: Ağ ve JSON kütüphaneleri için Retrofit/OkHttp/Gson/Moshi kombinasyonu yaygın olarak kullanılmaktadır. Projene uygun olanları Gradle üzerinden ekleyebilirsin.
## Mimari
- Presentation: Compose ekranları, ViewModel’ler, UI state/event’leri
- Domain: Use case’ler ve domain modelleri
- Data: Repository implementasyonu, remote DTO’lar, API arayüzü, mapper’lar
- Bağımlılık yönetimi: Hilt modülleri ile sağlama

Bu yapı, test edilebilirlik, bağımsız katmanlar ve kolay genişletilebilirlik sağlar.
## Özellikler
- Film arama (metinle arama)
- Sonuçların listelenmesi
- Film detay ekranı (ör. başlık, poster, puanlar)
- Yükleme ve hata durumları
- Basit gezinme: Liste → Detay
