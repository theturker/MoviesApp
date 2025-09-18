# MoviesApp
# Film Uygulaması
##MoviesApp
Modern Android mimarisiyle yazılmış, film arama ve detay görüntüleme özelliklerine sahip örnek uygulama.

##Özellikler
🔎 Film arama (OMDb benzeri API ile metin arama)
🗂️ Sonuçların listelenmesi (grid, skeleton/shimmer yükleme)
🎬 Film detayları (başlık, poster, yıl, puan vb.)
⭐ Favorileme (Room ile kalıcı olarak saklanır)
❤️ Favoriler ekranı (ana ekrandaki kalp ikonundan erişilir)
🧭 Basit gezinme: Liste → Detay → (Favoriler)
🌚 Material 3 + koyu tema, Edge-to-Edge

##Mimari
Clean Architecture + MVVM
presentation: Compose ekranları, ViewModel’ler, UI state/event
domain: Use case’ler, domain modelleri, repository arayüzleri
data: Repository implementasyonları, DTO, mapper, kaynaklar
Modüler yapı
app
domain
data
core-network (Retrofit/OkHttp)
core-database (Room, DAO/Entity)
core-common (ortak yardımcılar)

Bu yapı, test edilebilirlik, bağımsız gelişim ve kolay genişletilebilirlik sağlar.

##Teknolojiler ve Araçlar
Kotlin, Coroutines + Flow
Jetpack Compose, Material 3
Navigation (Compose)
Hilt (DI) + KSP
Retrofit + OkHttp (Gson dönüştürücüsü ile)
Coil (görseller)
Room (yerel favoriler)
Firebase: Analytics, Performance Monitoring (opsiyonel: Crashlytics)
Gradle Kotlin DSL, Version Catalog (libs.versions.toml)
Kurulum

###1) Android Studio & SDK
Android Studio Giraffe+ (veya daha güncel)
compileSdk = 36, minSdk = 24

###2) Firebase
Firebase Console’da bir Android app oluştur.
app/ klasörüne google-services.json ekle.

###3) Room
core-database modülünde BookmarkEntity, BookmarkDao, AppDatabase tanımlı.
Şema değiştirirsen DB versiyonunu artır veya geliştirme aşamasında fallbackToDestructiveMigration() kullan.
Favoriler Flow<List<BookmarkEntity>> ile reaktif olarak gözlemlenir.

###4) Analytics ve Performance
Analytics: AnalyticsLogger benzeri bir sarmalayıcıyla ekran olaylarını tek noktadan logluyoruz.
Örnek: NavHost’ta currentDestination değiştikçe logScreenView("Movies") çağrılır.
Firebase Performance: BuildConfig.PERF_ENABLED ile release’te açık, debug’da kapalı.

##Kullanım
Keşfet/Discover: Arama yap, sonuçları gör.
Detay: Sağ üstteki yıldız/ikon ile favoriye ekle/çıkar.
Favoriler: Ana ekranın app bar’ındaki kalp ikonuna basarak listeye git.3) Room
core-database modülünde BookmarkEntity, BookmarkDao, AppDatabase tanımlı.
Şema değiştirirsen DB versiyonunu artır veya geliştirme aşamasında fallbackToDestructiveMigration() kullan.
Favoriler Flow<List<BookmarkEntity>> ile reaktif olarak gözlemlenir.

###4) Analytics ve Performance
Analytics: AnalyticsLogger benzeri bir sarmalayıcıyla ekran olaylarını tek noktadan logluyoruz.
Örnek: NavHost’ta currentDestination değiştikçe logScreenView("Movies") çağrılır.
Firebase Performance: BuildConfig.PERF_ENABLED ile release’te açık, debug’da kapalı.

##Kullanım
Keşfet/Discover: Arama yap, sonuçları gör.
Detay: Sağ üstteki yıldız/ikon ile favoriye ekle/çıkar.
Favoriler: Ana ekranın app bar’ındaki kalp ikonuna basarak listeye git.3) Room
core-database modülünde BookmarkEntity, BookmarkDao, AppDatabase tanımlı.
Şema değiştirirsen DB versiyonunu artır veya geliştirme aşamasında fallbackToDestructiveMigration() kullan.
Favoriler Flow<List<BookmarkEntity>> ile reaktif olarak gözlemlenir.

###4) Analytics ve Performance
Analytics: AnalyticsLogger benzeri bir sarmalayıcıyla ekran olaylarını tek noktadan logluyoruz.
Örnek: NavHost’ta currentDestination değiştikçe logScreenView("Movies") çağrılır.
Firebase Performance: BuildConfig.PERF_ENABLED ile release’te açık, debug’da kapalı.
Kullanım
Keşfet/Discover: Arama yap, sonuçları gör.
Detay: Sağ üstteki yıldız/ikon ile favoriye ekle/çıkar.
Favoriler: Ana ekranın app bar’ındaki kalp ikonuna basarak listeye git.
